package com.fight2.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.PartyDao;
import com.fight2.dao.PartyGridDao;
import com.fight2.dao.PartyInfoDao;
import com.fight2.dao.UserDao;
import com.fight2.model.BaseEntity;
import com.fight2.model.Party;
import com.fight2.model.PartyGrid;
import com.fight2.model.PartyInfo;
import com.fight2.model.User;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/user")
public class UserAction extends BaseAction {
    private static final long serialVersionUID = -4473064014262040889L;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PartyInfoDao partyInfoDao;
    @Autowired
    private PartyDao partyDao;
    @Autowired
    private PartyGridDao partyGridDao;
    private User user;
    private List<User> datas;
    private int id;
    private String installUUID;

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

    @Action(value = "register", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String register() {
        final User checkUser = userDao.getByInstallUUID(installUUID);
        if (checkUser == null) {
            user = new User();
            user.setInstallUUID(installUUID);
            user.setName("User" + System.currentTimeMillis());
            userDao.add(user);
            final PartyInfo partyInfo = new PartyInfo();
            partyInfo.setUser(user);
            partyInfoDao.add(partyInfo);
            for (int i = 1; i < 4; i++) {
                final Party party = new Party();
                party.setPartyNumber(i);
                party.setPartyInfo(partyInfo);
                partyDao.add(party);
                for (int gridIndex = 1; gridIndex < 5; gridIndex++) {
                    final PartyGrid partyGrid = new PartyGrid();
                    partyGrid.setGridNumber(gridIndex);
                    partyGrid.setParty(party);
                    partyGridDao.add(partyGrid);
                }
            }

        }
        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", new Gson().toJson(user));
        return SUCCESS;
    }

    @Action(value = "login", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String login() {
        user = userDao.getByInstallUUID(installUUID);
        final User voUser = new User();
        voUser.setId(user.getId());
        voUser.setAvatar(user.getAvatar());
        voUser.setCardCount(user.getCardCount());
        voUser.setLevel(user.getLevel());
        voUser.setName(user.getName());
        voUser.setInstallUUID(user.getInstallUUID());
        voUser.setUsername(user.getUsername());

        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", new Gson().toJson(voUser));
        this.getSession().put(LOGIN_USER, user);
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

    public String getInstallUUID() {
        return installUUID;
    }

    public void setInstallUUID(final String installUUID) {
        this.installUUID = installUUID;
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

    public PartyDao getPartyDao() {
        return partyDao;
    }

    public void setPartyDao(final PartyDao partyDao) {
        this.partyDao = partyDao;
    }

    public PartyGridDao getPartyGridDao() {
        return partyGridDao;
    }

    public void setPartyGridDao(final PartyGridDao partyGridDao) {
        this.partyGridDao = partyGridDao;
    }

}
