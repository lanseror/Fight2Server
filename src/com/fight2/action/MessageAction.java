package com.fight2.action;

import java.util.Map;

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
import com.google.common.collect.Maps;
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
        final Map<String, Object> response = Maps.newHashMap();
        final User user = getLoginUser();
        final User userPo = userDao.load(user.getId());
        final UserQuestTask userTask = userQuestTaskDao.getUserCurrentTask(userPo);
        UserQuestTask userQuestTaskVo = null;
        if (userTask != null) {
            userQuestTaskVo = new UserQuestTask(userTask);
            response.put("status", 0);
        } else {
            final UserQuestTask userLatestTask = userQuestTaskDao.getUserLatestTask(userPo);
            if (userLatestTask == null) {
                final QuestTask questTask = questTaskDao.get(1);
                final UserQuestTask userQuestTask = new UserQuestTask();
                userQuestTask.setUser(userPo);
                userQuestTask.setStatus(UserTaskStatus.Ready);
                userQuestTask.setTask(questTask);
                userQuestTaskDao.add(userQuestTask);
                userQuestTaskVo = new UserQuestTask(userQuestTask);
                response.put("status", 0);
            } else {
                response.put("status", 1);
            }

        }

        response.put("task", userQuestTaskVo);
        jsonMsg = new Gson().toJson(response);
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
