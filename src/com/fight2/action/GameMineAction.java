package com.fight2.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.GameMineDao;
import com.fight2.dao.UserDao;
import com.fight2.model.BaseEntity;
import com.fight2.model.BattleResult;
import com.fight2.model.User;
import com.fight2.model.User.UserType;
import com.fight2.model.UserProperties;
import com.fight2.model.UserQuestInfo;
import com.fight2.model.quest.GameMine;
import com.fight2.model.quest.QuestTile;
import com.fight2.service.MineService;
import com.fight2.util.Constants;
import com.google.gson.Gson;

@Namespace("/mine")
public class GameMineAction extends BaseAction {
    private static final long serialVersionUID = -4473064014262040889L;
    @Autowired
    private UserDao userDao;
    @Autowired
    private GameMineDao gameMineDao;
    @Autowired
    private MineService mineService;
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
        mine.setOwnerId(npcs.get(0).getId());
        gameMineDao.add(mine);
        return SUCCESS;
    }

    private String editSave() {
        final GameMine mineUpdate = gameMineDao.load(mine.getId());
        mineUpdate.setType(mine.getType());
        mineUpdate.setCol(mine.getCol());
        mineUpdate.setRow(mine.getRow());
        gameMineDao.update(mineUpdate);
        return SUCCESS;
    }

    @Action(value = "delete", results = { @Result(name = SUCCESS, location = "list", type = "redirect") })
    public String delete() {
        mine = gameMineDao.load(id);
        gameMineDao.delete(mine);
        return SUCCESS;
    }

    @Action(value = "attack", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String attack() {
        final User loginUser = (User) this.getSession().get(LOGIN_USER);
        final User user = userDao.get(loginUser.getId());
        final UserQuestInfo userQuestInfo = user.getQuestInfo();

        final QuestTile mineTile = new QuestTile();
        mineTile.setCol(userQuestInfo.getCol());
        mineTile.setRow(userQuestInfo.getRow());

        final UserProperties userProps = user.getUserProperties();
        // Validate
        if (!mineService.check(mineTile)) {
            throw new RuntimeException("Mine position not match.");
        }
        if (userProps.getDiamon() < Constants.MINE_ATTACK_COST) {
            throw new RuntimeException("No enough diamon.");
        }

        final BattleResult battleResult = mineService.attack(user, mineTile);
        jsonMsg = new Gson().toJson(battleResult);
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

    public MineService getMineService() {
        return mineService;
    }

    public void setMineService(final MineService mineService) {
        this.mineService = mineService;
    }

}
