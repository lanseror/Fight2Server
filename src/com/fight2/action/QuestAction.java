package com.fight2.action;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;

import com.fight2.dao.CardDao;
import com.fight2.dao.PartyInfoDao;
import com.fight2.dao.UserDao;
import com.fight2.dao.UserPropertiesDao;
import com.fight2.dao.UserQuestInfoDao;
import com.fight2.dao.UserQuestTaskDao;
import com.fight2.dao.UserStoreroomDao;
import com.fight2.model.BattleResult;
import com.fight2.model.Card;
import com.fight2.model.Card.CardStatus;
import com.fight2.model.CardTemplate;
import com.fight2.model.PartyInfo;
import com.fight2.model.User;
import com.fight2.model.User.UserType;
import com.fight2.model.UserProperties;
import com.fight2.model.UserQuestInfo;
import com.fight2.model.UserQuestTask;
import com.fight2.model.UserQuestTask.UserTaskStatus;
import com.fight2.model.UserStoreroom;
import com.fight2.model.quest.QuestTile;
import com.fight2.model.quest.QuestTile.TileItem;
import com.fight2.model.quest.QuestTreasureData;
import com.fight2.service.BattleService;
import com.fight2.service.ComboSkillService;
import com.fight2.util.QuestUtils;
import com.fight2.util.SummonHelper;
import com.fight2.util.TmxUtils;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/quest")
public class QuestAction extends BaseAction {
    private static final long serialVersionUID = -9079446757823452597L;
    @Autowired
    private UserQuestInfoDao userQuestInfoDao;
    @Autowired
    private CardDao cardDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PartyInfoDao partyInfoDao;
    @Autowired
    private UserQuestTaskDao userQuestTaskDao;
    @Autowired
    private UserStoreroomDao userStoreroomDao;
    @Autowired
    private UserPropertiesDao userPropertiesDao;
    @Autowired
    private SummonHelper summonHelper;
    @Autowired
    private TaskScheduler taskScheduler;
    @Autowired
    private ComboSkillService comboSkillService;
    private int id;
    private int row;
    private int col;
    private long version;

    @Action(value = "go", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String go() {
        final Map<String, Object> response = Maps.newHashMap();
        final User currentUser = (User) this.getSession().get(LOGIN_USER);
        final User user = userDao.get(currentUser.getId());
        UserQuestInfo userQuestInfo = user.getQuestInfo();
        if (userQuestInfo == null) {
            userQuestInfo = new UserQuestInfo();
            userQuestInfo.setUser(user);
            userQuestInfoDao.add(userQuestInfo);
        }

        final QuestTile startTile = new QuestTile();
        startTile.setCol(userQuestInfo.getCol());
        startTile.setRow(userQuestInfo.getRow());
        final QuestTile desTile = new QuestTile();
        desTile.setCol(col);
        desTile.setRow(row);
        final Stack<QuestTile> path = TmxUtils.findPath(startTile, desTile);
        final boolean staminaEnough = useStamina(path, user);

        int status = 0;
        final QuestTreasureData questTreasureData = QuestUtils.getUserData(currentUser.getId());
        if (questTreasureData.getVersion() > version) {
            response.put("treasureUpdate", true);
            response.put("treasure", questTreasureData);
        } else {
            response.put("treasureUpdate", false);
        }

        final UserQuestTask userQuestTask = userQuestTaskDao.getUserCurrentTask(user);
        if (!staminaEnough) {
            status = 4;
        } else if (userQuestTask != null && userQuestTask.getStatus() == UserTaskStatus.Started && userQuestTask.getTask().getX() == col
                && userQuestTask.getTask().getY() == row) {
            status = 3;
            final User boss = userQuestTask.getTask().getBoss();
            final User bossVo = new User();
            bossVo.setId(boss.getId());
            bossVo.setName(boss.getName());
            response.put("enemy", bossVo);
        } else {
            final List<QuestTile> treasures = questTreasureData.getQuestTiles();
            final Iterator<QuestTile> it = treasures.iterator();
            int treasureIndex = 0;
            boolean hasTreasure = false;
            while (it.hasNext()) {
                final QuestTile treasure = it.next();
                if (treasure.getRow() == row && treasure.getCol() == col) {
                    status = 1;
                    final TileItem tileItem = treasure.getItem();
                    response.put("treasureItem", tileItem);

                    if (tileItem == TileItem.Card) {
                        summonTreasure(response, user);
                    } else {
                        final UserStoreroom userStoreroom = user.getStoreroom();
                        if (tileItem == TileItem.CoinBag) {
                            userStoreroom.setCoinBag(userStoreroom.getCoinBag() + 1);
                        } else if (tileItem == TileItem.Stamina) {
                            userStoreroom.setStamina(userStoreroom.getStamina() + 1);
                        } else if (tileItem == TileItem.Ticket) {
                            userStoreroom.setTicket(userStoreroom.getTicket() + 1);
                        }
                        userStoreroomDao.update(userStoreroom);
                    }
                    response.put("treasureIndex", treasureIndex);
                    hasTreasure = true;
                    it.remove();
                    break;
                }
                treasureIndex++;
            }
            if (!hasTreasure) {
                final Random random = new Random();
                final int randomNum = random.nextInt(40);
                if (randomNum < 2) {
                    status = 2;
                    final List<User> users = userDao.listByType(UserType.User);
                    final User enemy = users.get(random.nextInt(users.size()));
                    final User enemyVo = new User();
                    enemyVo.setId(enemy.getId());
                    enemyVo.setName(enemy.getName());
                    response.put("enemy", enemyVo);
                } else if (randomNum >= 2 && randomNum < 10) {
                    status = 2;
                    final List<User> npcs = userDao.listByType(UserType.QuestNpc);
                    final User enemy = npcs.get(random.nextInt(npcs.size()));
                    final User enemyVo = new User();
                    enemyVo.setId(enemy.getId());
                    enemyVo.setName(enemy.getName());
                    response.put("enemy", enemyVo);
                }
            }
        }
        if (staminaEnough) {
            userQuestInfo.setRow(row);
            userQuestInfo.setCol(col);
            userQuestInfoDao.update(userQuestInfo);
        }
        response.put("stamina", user.getUserProperties().getStamina());
        response.put("status", status);
        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", new Gson().toJson(response));
        return SUCCESS;
    }

    private boolean useStamina(final Stack<QuestTile> path, final User user) {
        final int costStamina = path.size() - 1;
        final UserProperties userProperties = user.getUserProperties();
        calculateStamina(userProperties);
        // System.out.println(user.getName() + ":" + costStamina);
        if (costStamina > userProperties.getStamina()) {
            userPropertiesDao.update(userProperties);
            return false;
        }
        userProperties.setStamina(userProperties.getStamina() - costStamina);
        userPropertiesDao.update(userProperties);
        return true;
    }

    private void calculateStamina(final UserProperties userProperties) {
        final long now = System.currentTimeMillis();
        final long timeDiff = now - userProperties.getStaminaTime();
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff);
        final int recoverStamina = (int) (minutes * 1) + userProperties.getStamina();
        if (recoverStamina > UserProperties.MAX_STAMINA) {
            userProperties.setStamina(UserProperties.MAX_STAMINA);
            userProperties.setStaminaTime(now);
        } else {
            userProperties.setStamina(recoverStamina);
            if (minutes > 1) {
                final long remainCoverTime = timeDiff - TimeUnit.MINUTES.toMillis(minutes);
                userProperties.setStaminaTime(now - remainCoverTime);
            }
        }

    }

    private void summonTreasure(final Map<String, Object> response, final User user) {
        final Random random = new Random();
        final int randomNum = random.nextInt(100);
        int addNum = 0;
        if (randomNum == 0) {
            addNum = 2;
        } else if (randomNum > 0 && randomNum < 6) {
            addNum = 1;
        }
        final CardTemplate cardTemplate = summonHelper.summon(1, 2 + addNum);
        final String avatar = cardTemplate.getAvatars().get(0).getUrl();
        final String image = cardTemplate.getThumbImages().get(0).getUrl();

        final Card card = new Card();
        card.setAtk(cardTemplate.getAtk());
        card.setAvatar(avatar);
        card.setHp(cardTemplate.getHp());
        card.setImage(image);
        card.setRace(cardTemplate.getRace());
        card.setName(cardTemplate.getName());
        card.setStar(cardTemplate.getStar());
        card.setCardTemplate(cardTemplate);
        card.setStatus(CardStatus.InStoreroom);
        card.setUser(user);
        cardDao.add(card);

        final Card cardVo = new Card();
        cardVo.setId(card.getId());
        cardVo.setAtk(card.getAtk());
        cardVo.setAvatar(avatar);
        cardVo.setHp(card.getHp());
        cardVo.setImage(image);
        cardVo.setName(card.getName());
        cardVo.setSkill(card.getSkill());
        cardVo.setStar(card.getStar());
        cardVo.setRace(card.getRace());
        final CardTemplate cardTemplateVo = new CardTemplate();
        cardTemplateVo.setId(cardTemplate.getId());
        cardVo.setCardTemplate(cardTemplateVo);
        response.put("card", cardVo);
        final List<Card> cards = cardDao.listByUser(user);
        user.setCardCount(cards.size());
        userDao.update(user);
    }

    @Action(value = "treasure", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String getUserQuestTreasure() {
        final User currentUser = (User) this.getSession().get(LOGIN_USER);
        final QuestTreasureData questTreasureData = QuestUtils.getUserData(currentUser.getId());
        final ActionContext context = ActionContext.getContext();
        if (questTreasureData.getVersion() > version) {
            context.put("jsonMsg", new Gson().toJson(questTreasureData));
        } else {
            final QuestTreasureData oldData = new QuestTreasureData();
            oldData.setVersion(version);
            context.put("jsonMsg", new Gson().toJson(oldData));
        }

        return SUCCESS;
    }

    @Action(value = "user-props", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String getUserProperties() {
        final User currentUser = (User) this.getSession().get(LOGIN_USER);
        final User user = userDao.get(currentUser.getId());
        final UserProperties userProperties = user.getUserProperties();
        calculateStamina(userProperties);
        userPropertiesDao.update(userProperties);
        final UserProperties userPropertiesVo = new UserProperties();
        userPropertiesVo.setCoin(userProperties.getCoin());
        userPropertiesVo.setStamina(userProperties.getStamina());
        userPropertiesVo.setTicket(userProperties.getTicket());
        jsonMsg = new Gson().toJson(userPropertiesVo);
        return SUCCESS;
    }

    @Action(value = "attack", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String attack() {
        final User attacker = (User) this.getSession().get(LOGIN_USER);
        final User defender = userDao.get(id);

        final PartyInfo attackerPartyInfo = partyInfoDao.getByUser(attacker);
        final PartyInfo defenderPartyInfo = partyInfoDao.getByUser(defender);

        final BattleService battleService = new BattleService(attacker, defender, attackerPartyInfo, defenderPartyInfo, null);
        battleService.setComboSkillService(comboSkillService);
        final BattleResult battleResult = battleService.fight(0);

        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", new Gson().toJson(battleResult));
        return SUCCESS;
    }

    public UserQuestInfoDao getUserQuestInfoDao() {
        return userQuestInfoDao;
    }

    public void setUserQuestInfoDao(final UserQuestInfoDao userQuestInfoDao) {
        this.userQuestInfoDao = userQuestInfoDao;
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

    public int getRow() {
        return row;
    }

    public void setRow(final int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(final int col) {
        this.col = col;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(final long version) {
        this.version = version;
    }

    public SummonHelper getSummonHelper() {
        return summonHelper;
    }

    public void setSummonHelper(final SummonHelper summonHelper) {
        this.summonHelper = summonHelper;
    }

    public CardDao getCardDao() {
        return cardDao;
    }

    public void setCardDao(final CardDao cardDao) {
        this.cardDao = cardDao;
    }

    public TaskScheduler getTaskScheduler() {
        return taskScheduler;
    }

    public void setTaskScheduler(final TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public PartyInfoDao getPartyInfoDao() {
        return partyInfoDao;
    }

    public void setPartyInfoDao(final PartyInfoDao partyInfoDao) {
        this.partyInfoDao = partyInfoDao;
    }

    public UserQuestTaskDao getUserQuestTaskDao() {
        return userQuestTaskDao;
    }

    public void setUserQuestTaskDao(final UserQuestTaskDao userQuestTaskDao) {
        this.userQuestTaskDao = userQuestTaskDao;
    }

    public UserStoreroomDao getUserStoreroomDao() {
        return userStoreroomDao;
    }

    public void setUserStoreroomDao(final UserStoreroomDao userStoreroomDao) {
        this.userStoreroomDao = userStoreroomDao;
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
