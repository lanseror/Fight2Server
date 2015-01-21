package com.fight2.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.PartyInfoDao;
import com.fight2.dao.QuestTaskDao;
import com.fight2.dao.UserDao;
import com.fight2.dao.UserQuestTaskDao;
import com.fight2.model.BaseEntity;
import com.fight2.model.BattleResult;
import com.fight2.model.PartyInfo;
import com.fight2.model.QuestTask;
import com.fight2.model.User;
import com.fight2.model.User.UserType;
import com.fight2.model.UserQuestTask;
import com.fight2.model.UserQuestTask.UserTaskStatus;
import com.fight2.service.BattleService;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/task")
public class QuestTaskAction extends BaseAction {
    private static final long serialVersionUID = 184744772751526021L;
    @Autowired
    private UserDao userDao;
    @Autowired
    private QuestTaskDao questTaskDao;
    @Autowired
    private UserQuestTaskDao userQuestTaskDao;
    @Autowired
    private PartyInfoDao partyInfoDao;
    private QuestTask task;
    private List<QuestTask> datas;
    private int id;
    private List<User> bosses;
    private int bossId;

    @Action(value = "list", results = { @Result(name = SUCCESS, location = "quest_task_list.ftl") })
    public String list() {
        datas = questTaskDao.list();
        return SUCCESS;
    }

    @Action(value = "add", results = { @Result(name = SUCCESS, location = "quest_task_form.ftl") })
    public String add() {
        loadBossData();
        return SUCCESS;
    }

    @Action(value = "edit", results = { @Result(name = SUCCESS, location = "quest_task_form.ftl") })
    public String edit() {
        task = questTaskDao.get(id);
        loadBossData();
        return SUCCESS;
    }

    private void loadBossData() {
        bosses = userDao.listByType(UserType.Boss);
    }

    @Action(value = "save", interceptorRefs = { @InterceptorRef("tokenSession"), @InterceptorRef("defaultStack") }, results = {
            @Result(name = SUCCESS, location = "list", type = "redirect"), @Result(name = INPUT, location = "quest_task_form.ftl") })
    public String save() {
        if (task.getBoss().getId() == BaseEntity.EMPTY_ID) {
            task.setBoss(null);
        }
        if (task.getId() == BaseEntity.EMPTY_ID) {
            return createSave();
        } else {
            return editSave();
        }
    }

    @Action(value = "accept", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String accept() {
        final User loginUser = getLoginUser();
        final User user = userDao.load(loginUser.getId());
        final UserQuestTask userQuestTask = userQuestTaskDao.getUserCurrentTask(user);
        if (userQuestTask.getStatus() == UserTaskStatus.Ready) {
            userQuestTask.setStatus(UserTaskStatus.Started);
            userQuestTaskDao.update(userQuestTask);
        } else {
            throw new RuntimeException("Cannot start a task if it's not in ready status!");
        }
        final Map<String, Object> response = Maps.newHashMap();
        response.put("status", 0);
        jsonMsg = new Gson().toJson(response);
        return SUCCESS;
    }

    @Action(value = "test", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String revert() {
        final List<UserQuestTask> userQuestTasks = userQuestTaskDao.list();
        for (final UserQuestTask userQuestTask : userQuestTasks) {
            userQuestTask.setStatus(UserTaskStatus.Ready);
            userQuestTaskDao.update(userQuestTask);
        }
        final Map<String, Object> response = Maps.newHashMap();
        response.put("status", 0);
        jsonMsg = new Gson().toJson(response);
        return SUCCESS;
    }

    @Action(value = "attack", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String attack() {
        final User attacker = (User) this.getSession().get(LOGIN_USER);
        final UserQuestTask userQuestTask = userQuestTaskDao.getUserCurrentTask(attacker);
        if (userQuestTask.getStatus() != UserTaskStatus.Started) {
            return INPUT;
        }

        final User defender = userQuestTask.getTask().getBoss();
        final PartyInfo attackerPartyInfo = partyInfoDao.getByUser(attacker);
        final PartyInfo defenderPartyInfo = partyInfoDao.getByUser(defender);

        final BattleService battleService = new BattleService(attacker, defender, attackerPartyInfo, defenderPartyInfo, null);
        final BattleResult battleResult = battleService.fight(0);
        if (battleResult.isWinner()) {
            userQuestTask.setStatus(UserTaskStatus.Finished);
            userQuestTaskDao.update(userQuestTask);
        }
        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", new Gson().toJson(battleResult));
        return SUCCESS;
    }

    private String editSave() {
        questTaskDao.update(task);
        return SUCCESS;
    }

    private String createSave() {
        questTaskDao.add(task);
        return SUCCESS;
    }

    public QuestTask getTask() {
        return task;
    }

    public void setTask(final QuestTask task) {
        this.task = task;
    }

    public List<QuestTask> getDatas() {
        return datas;
    }

    public void setDatas(final List<QuestTask> datas) {
        this.datas = datas;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }

    public QuestTaskDao getQuestTaskDao() {
        return questTaskDao;
    }

    public void setQuestTaskDao(final QuestTaskDao questTaskDao) {
        this.questTaskDao = questTaskDao;
    }

    public UserQuestTaskDao getUserQuestTaskDao() {
        return userQuestTaskDao;
    }

    public void setUserQuestTaskDao(final UserQuestTaskDao userQuestTaskDao) {
        this.userQuestTaskDao = userQuestTaskDao;
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

    public List<User> getBosses() {
        return bosses;
    }

    public void setBosses(final List<User> bosses) {
        this.bosses = bosses;
    }

    public int getBossId() {
        return bossId;
    }

    public void setBossId(final int bossId) {
        this.bossId = bossId;
    }

    public PartyInfoDao getPartyInfoDao() {
        return partyInfoDao;
    }

    public void setPartyInfoDao(final PartyInfoDao partyInfoDao) {
        this.partyInfoDao = partyInfoDao;
    }

}
