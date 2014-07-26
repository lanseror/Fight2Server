package com.fight2.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.UserDao;
import com.fight2.model.BaseEntity;
import com.fight2.model.User;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@Namespace("/user")
public class UserAction extends ActionSupport {
    private static final long serialVersionUID = -4473064014262040889L;
    @Autowired
    private UserDao userDao;
    private User user;
    private List<User> datas;
    private int id;

    @Action(value = "list", results = { @Result(name = SUCCESS, location = "user_list.ftl") })
    public String list() {
        datas = userDao.list();
        return SUCCESS;
    }

    @Action(value = "list-json", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String listJson() {
        final ActionContext context = ActionContext.getContext();
        final List<User> list = userDao.list();
        context.put("jsonMsg", new Gson().toJson(list));
        return SUCCESS;
    }

    @Action(value = "summon", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String summon() {

        return SUCCESS;
    }

    @Action(value = "add", results = { @Result(name = SUCCESS, location = "user_form.ftl") })
    public String add() {
        return SUCCESS;
    }

    @Action(value = "edit", results = { @Result(name = SUCCESS, location = "user_form.ftl") })
    public String edit() {
        user = userDao.get(id);
        return SUCCESS;
    }

    @Action(value = "save", interceptorRefs = { @InterceptorRef("tokenSession"), @InterceptorRef("defaultStack") }, results = { @Result(
            name = SUCCESS, location = "list", type = "redirect") })
    public String save() {
        if (user.getId() == BaseEntity.EMPTY_ID) {
            return createSave();
        } else {
            return editSave();
        }
    }

    private String createSave() {
        userDao.add(user);
        return SUCCESS;
    }

    private String editSave() {
        userDao.update(user);
        return SUCCESS;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public List<User> getDatas() {
        return datas;
    }

    public void setDatas(final List<User> datas) {
        this.datas = datas;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

}
