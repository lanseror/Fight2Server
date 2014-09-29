package com.fight2.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.GuildDao;
import com.fight2.dao.UserDao;
import com.fight2.model.Guild;
import com.fight2.model.User;
import com.google.common.collect.Maps;
import com.google.gson.Gson;

@Namespace("/guild")
public class GuildAction extends BaseAction {
    private static final long serialVersionUID = 184744772751526021L;
    @Autowired
    private UserDao userDao;
    @Autowired
    private GuildDao guildDao;
    private Guild guild;
    private List<Guild> datas;
    private int id;
    private String jsonMsg;

    // @Action(value = "list", results = { @Result(name = SUCCESS, location = "user_list.ftl") })
    // public String list() {
    // datas = guildDao.list();
    // return SUCCESS;
    // }
    //
    // @Action(value = "list-json", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    // public String listJson() {
    // final ActionContext context = ActionContext.getContext();
    // final List<User> list = userDao.list();
    // context.put("jsonMsg", new Gson().toJson(list));
    // return SUCCESS;
    // }

    @Action(value = "apply", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String applyGuild() {
        final User loginUser = this.getLoginUser();
        final User loginUserPo = userDao.get(loginUser.getId());
        final Guild guild = new Gson().fromJson(jsonMsg, Guild.class);
        try {
            if (guildDao.getByPresident(loginUserPo) == null) {
                guild.setName(URLDecoder.decode(guild.getName(), "UTF-8"));
                guild.setPresident(loginUserPo);
                guild.setCreateDate(new Date());
                guildDao.add(guild);
            }

        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        loginUserPo.setGuild(guild);
        userDao.update(loginUserPo);
        final Map<String, Integer> response = Maps.newHashMap();
        response.put("status", 0);
        jsonMsg = new Gson().toJson(response);
        return SUCCESS;
    }

    @Action(value = "president-edit", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String presidentEdit() {
        final User loginUser = this.getLoginUser();
        final Guild guild = new Gson().fromJson(jsonMsg, Guild.class);
        try {
            final Guild guildPo = guildDao.get(guild.getId());
            if (guildPo.getPresident().getId() == loginUser.getId()) {
                guildPo.setNotice(URLDecoder.decode(guild.getNotice(), "UTF-8"));
                guildPo.setQq(guild.getQq());
                guildDao.update(guildPo);
            }
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        final Map<String, Integer> response = Maps.newHashMap();
        response.put("status", 0);
        jsonMsg = new Gson().toJson(response);
        return SUCCESS;
    }

    @Action(value = "get-user-guild", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String getUserGuild() {
        final User loginUser = this.getLoginUser();
        final User loginUserPo = userDao.get(loginUser.getId());
        final Guild guild = loginUserPo.getGuild();
        final Map<String, Object> response = Maps.newHashMap();
        if (guild == null) {
            response.put("status", -1);
        } else {
            final User president = guild.getPresident();
            final boolean isPresident = (president.getId() == loginUser.getId());
            response.put("status", isPresident ? 1 : 0);
            final Guild guildVo = new Guild();
            guildVo.setId(guild.getId());
            guildVo.setName(guild.getName());
            guildVo.setNotice(guild.getNotice());
            guildVo.setQq(guild.getQq());
            final User presidentVo = new User();
            presidentVo.setId(president.getId());
            presidentVo.setName(president.getName());
            guildVo.setPresident(presidentVo);
            response.put("guild", guildVo);
        }

        jsonMsg = new Gson().toJson(response);
        return SUCCESS;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(final Guild guild) {
        this.guild = guild;
    }

    public List<Guild> getDatas() {
        return datas;
    }

    public void setDatas(final List<Guild> datas) {
        this.datas = datas;
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

    public GuildDao getGuildDao() {
        return guildDao;
    }

    public void setGuildDao(final GuildDao guildDao) {
        this.guildDao = guildDao;
    }

    public String getJsonMsg() {
        return jsonMsg;
    }

    public void setJsonMsg(final String jsonMsg) {
        this.jsonMsg = jsonMsg;
    }

}
