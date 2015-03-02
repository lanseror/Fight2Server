package com.fight2.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.GameMineDao;
import com.fight2.dao.PartyInfoDao;
import com.fight2.dao.UserDao;
import com.fight2.dao.UserPropertiesDao;
import com.fight2.model.BaseEntity;
import com.fight2.model.BattleResult;
import com.fight2.model.PartyInfo;
import com.fight2.model.User;
import com.fight2.model.User.UserType;
import com.fight2.model.UserProperties;
import com.fight2.model.UserQuestInfo;
import com.fight2.model.quest.GameMine;
import com.fight2.model.quest.GameMine.MineType;
import com.fight2.model.quest.QuestTile;
import com.fight2.service.BattleService;
import com.fight2.service.ComboSkillService;
import com.fight2.util.CostConstants;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

@Namespace("/mine")
public class GameMineAction extends BaseAction {
    private static final long serialVersionUID = -4473064014262040889L;
    @Autowired
    private PartyInfoDao partyInfoDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserPropertiesDao userPropertiesDao;
    @Autowired
    private GameMineDao gameMineDao;
    @Autowired
    private ComboSkillService comboSkillService;
    private GameMine mine;
    private List<GameMine> datas;
    private int id;

    @Action(value = "list", results = { @Result(name = SUCCESS, location = "mine_list.ftl") })
    public String list() {
        datas = gameMineDao.list();
        return SUCCESS;
    }

    @Action(value = "add", results = { @Result(name = SUCCESS, location = "mine_form.ftl") })
    public String add() {
        return SUCCESS;
    }

    @Action(value = "edit", results = { @Result(name = SUCCESS, location = "mine_form.ftl") })
    public String edit() {
        mine = gameMineDao.get(id);
        return SUCCESS;
    }

    @Action(value = "save", interceptorRefs = { @InterceptorRef("tokenSession"), @InterceptorRef("defaultStack") }, results = { @Result(
            name = SUCCESS, location = "list", type = "redirect") })
    public String save() {
        if (mine.getId() == BaseEntity.EMPTY_ID) {
            return createSave();
        } else {
            return editSave();
        }
    }

    private String createSave() {
        final List<User> npcs = userDao.listByType(UserType.QuestNpc);
        mine.setOwner(npcs.get(0));
        final MineType mineType = mine.getType();
        mine.setHeroCol(mine.getCol() + mineType.getxOffset());
        mine.setHeroRow(mine.getRow() + mineType.getyOffset());
        gameMineDao.add(mine);
        return SUCCESS;
    }

    private String editSave() {
        final GameMine mineUpdate = gameMineDao.load(mine.getId());
        final MineType mineType = mine.getType();
        mineUpdate.setType(mineType);
        mineUpdate.setCol(mine.getCol());
        mineUpdate.setRow(mine.getRow());
        mineUpdate.setHeroCol(mine.getCol() + mineType.getxOffset());
        mineUpdate.setHeroRow(mine.getRow() + mineType.getyOffset());
        gameMineDao.update(mineUpdate);
        return SUCCESS;
    }

    @Action(value = "delete", results = { @Result(name = SUCCESS, location = "list", type = "redirect") })
    public String delete() {
        mine = gameMineDao.load(id);
        gameMineDao.delete(mine);
        return SUCCESS;
    }

    @Action(value = "owner", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String getMineOwner() {
        final User loginUser = (User) this.getSession().get(LOGIN_USER);
        final User user = userDao.get(loginUser.getId());
        final UserQuestInfo userQuestInfo = user.getQuestInfo();

        final QuestTile mineTile = new QuestTile();
        mineTile.setCol(userQuestInfo.getCol());
        mineTile.setRow(userQuestInfo.getRow());

        // Validate
        final GameMine gameMine = gameMineDao.getByHeroPosition(mineTile.getRow(), mineTile.getCol());
        if (gameMine == null) {
            throw new RuntimeException("Mine position not match.");
        }

        final User owner = gameMine.getOwner();
        final User ownerVo = new User();
        ownerVo.setId(owner.getId());
        ownerVo.setName(owner.getName());
        jsonMsg = new Gson().toJson(ownerVo);
        return SUCCESS;
    }

    @Action(value = "mines", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String mines() {
        final List<GameMine> mines = gameMineDao.list();
        final List<GameMine> mineVos = Lists.newArrayList();
        for (final GameMine mine : mines) {
            final GameMine mineVo = new GameMine();
            mineVo.setId(mine.getId());
            mineVo.setAmount(mine.getAmount());
            mineVo.setCol(mine.getCol());
            mineVo.setRow(mine.getRow());
            mineVo.setHeroCol(mine.getHeroCol());
            mineVo.setHeroRow(mine.getHeroRow());
            mineVo.setType(mine.getType());
            final User owner = mine.getOwner();
            final User ownerVo = new User();
            ownerVo.setId(owner.getId());
            ownerVo.setName(owner.getName());
            mineVo.setOwner(ownerVo);
            mineVos.add(mineVo);
        }
        jsonMsg = new Gson().toJson(mineVos);
        return SUCCESS;
    }

    @Action(value = "attack", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String attack() {
        final User loginUser = (User) this.getSession().get(LOGIN_USER);
        final User user = userDao.get(loginUser.getId());
        final UserQuestInfo userQuestInfo = user.getQuestInfo();

        final UserProperties userProps = user.getUserProperties();
        // Validate
        final GameMine gameMine = gameMineDao.getByHeroPosition(userQuestInfo.getRow(), userQuestInfo.getCol());
        if (gameMine == null) {
            throw new RuntimeException("Mine position not match.");
        }
        if (userProps.getDiamon() < CostConstants.MINE_ATTACK_COST) {
            throw new RuntimeException("No enough diamon.");
        }

        final User attacker = user;
        final User defender = gameMine.getOwner();
        final PartyInfo attackerPartyInfo = partyInfoDao.getByUser(attacker);
        final PartyInfo defenderPartyInfo = partyInfoDao.getByUser(defender);

        final BattleService battleService = new BattleService(attacker, defender, attackerPartyInfo, defenderPartyInfo, null);
        battleService.setComboSkillService(comboSkillService);
        final BattleResult battleResult = battleService.fight(0);
        final boolean isWinner = battleResult.isWinner();
        if (isWinner) {
            gameMine.setOwner(attacker);
            gameMineDao.update(gameMine);
        }
        userProps.setDiamon(userProps.getDiamon() - CostConstants.MINE_ATTACK_COST);
        userPropertiesDao.update(userProps);
        jsonMsg = new Gson().toJson(battleResult);
        return SUCCESS;
    }

    @Action(value = "gather", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String gather() {
        final User loginUser = (User) this.getSession().get(LOGIN_USER);
        final User user = userDao.get(loginUser.getId());
        final UserQuestInfo userQuestInfo = user.getQuestInfo();

        final UserProperties userProps = user.getUserProperties();
        // Validate
        final GameMine gameMine = gameMineDao.getByHeroPosition(userQuestInfo.getRow(), userQuestInfo.getCol());
        if (gameMine == null) {
            throw new RuntimeException("Mine position not match.");
        }
        if (gameMine.getOwner().getId() != user.getId()) {
            throw new RuntimeException("You are not the mine owner.");
        }
        final MineType type = gameMine.getType();
        final int amount = gameMine.getAmount();
        if (type == MineType.Crystal) {
            userProps.setGuildContrib(userProps.getGuildContrib() + amount * 10);
        } else if (type == MineType.Wood) {
            userProps.setGuildContrib(userProps.getGuildContrib() + amount * 5);
        } else if (type == MineType.Mineral) {
            userProps.setGuildContrib(userProps.getGuildContrib() + amount * 5);
        } else if (type == MineType.Diamon) {
            userProps.setDiamon(userProps.getDiamon() + amount);
        }
        gameMine.setAmount(0);
        userDao.update(user);
        gameMineDao.update(gameMine);
        final Map<String, String> response = Maps.newHashMap();
        response.put("status", "0");
        jsonMsg = new Gson().toJson(response);
        return SUCCESS;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public GameMine getMine() {
        return mine;
    }

    public void setMine(final GameMine mine) {
        this.mine = mine;
    }

    public GameMineDao getGameMineDao() {
        return gameMineDao;
    }

    public void setGameMineDao(final GameMineDao gameMineDao) {
        this.gameMineDao = gameMineDao;
    }

    public List<GameMine> getDatas() {
        return datas;
    }

    public void setDatas(final List<GameMine> datas) {
        this.datas = datas;
    }

    public PartyInfoDao getPartyInfoDao() {
        return partyInfoDao;
    }

    public void setPartyInfoDao(final PartyInfoDao partyInfoDao) {
        this.partyInfoDao = partyInfoDao;
    }

    public UserPropertiesDao getUserPropertiesDao() {
        return userPropertiesDao;
    }

    public void setUserPropertiesDao(final UserPropertiesDao userPropertiesDao) {
        this.userPropertiesDao = userPropertiesDao;
    }

    public ComboSkillService getComboSkillService() {
        return comboSkillService;
    }

    public void setComboSkillService(final ComboSkillService comboSkillService) {
        this.comboSkillService = comboSkillService;
    }

}
