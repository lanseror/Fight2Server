package com.fight2.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.QuestTaskDao;
import com.fight2.dao.UserDao;
import com.fight2.dao.UserQuestTaskDao;
import com.fight2.model.QuestTask;
import com.fight2.model.User;
import com.fight2.model.UserQuestTask;
import com.fight2.model.UserQuestTask.UserTaskStatus;
import com.google.gson.Gson;

@Namespace("/msg")
public class MessageAction extends BaseAction {
    private static final long serialVersionUID = 1514171827851300722L;
    @Autowired
    private QuestTaskDao questTaskDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserQuestTaskDao userQuestTaskDao;

    @Action(value = "get", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String get() {
        final User user = getLoginUser();
        final User userPo = userDao.load(user.getId());
        final List<UserQuestTask> userTasks = userQuestTaskDao.listByUser(userPo);
        UserQuestTask userQuestTaskVo;
        if (userTasks.size() > 0) {
            userQuestTaskVo = new UserQuestTask(userTasks.get(0));
        } else {
            final QuestTask questTask = questTaskDao.get(1);
            final UserQuestTask userQuestTask = new UserQuestTask();
            userQuestTask.setUser(userPo);
            userQuestTask.setStatus(UserTaskStatus.Ready);
            userQuestTask.setTask(questTask);
            userQuestTaskDao.add(userQuestTask);
            userQuestTaskVo = new UserQuestTask(userQuestTask);
        }
        jsonMsg = new Gson().toJson(userQuestTaskVo);
        return SUCCESS;
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

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
