package com.fight2.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.QuestTaskDao;
import com.fight2.dao.UserDao;
import com.fight2.dao.UserQuestTaskDao;
import com.fight2.model.BaseEntity;
import com.fight2.model.QuestTask;
import com.fight2.model.User;
import com.fight2.model.UserQuestTask;
import com.fight2.model.UserQuestTask.UserTaskStatus;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

@Namespace("/task")
public class QuestTaskAction extends BaseAction {
    private static final long serialVersionUID = 184744772751526021L;
    @Autowired
    private UserDao userDao;
    @Autowired
    private QuestTaskDao questTaskDao;
    @Autowired
    private UserQuestTaskDao userQuestTaskDao;
    private QuestTask task;
    private List<QuestTask> datas;
    private int id;

    @Action(value = "list", results = { @Result(name = SUCCESS, location = "quest_task_list.ftl") })
    public String list() {
        datas = questTaskDao.list();
        return SUCCESS;
    }

    @Action(value = "add", results = { @Result(name = SUCCESS, location = "quest_task_form.ftl") })
    public String add() {
        return SUCCESS;
    }

    @Action(value = "edit", results = { @Result(name = SUCCESS, location = "quest_task_form.ftl") })
    public String edit() {
        task = questTaskDao.get(id);
        return SUCCESS;
    }

    @Action(value = "save", interceptorRefs = { @InterceptorRef("tokenSession"), @InterceptorRef("defaultStack") }, results = {
            @Result(name = SUCCESS, location = "list", type = "redirect"), @Result(name = INPUT, location = "quest_task_form.ftl") })
    public String save() {
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

}
