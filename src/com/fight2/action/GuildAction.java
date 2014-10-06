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
import com.fight2.dao.GuildPollDao;
import com.fight2.dao.GuildVoterDao;
import com.fight2.dao.UserDao;
import com.fight2.model.Guild;
import com.fight2.model.GuildPoll;
import com.fight2.model.GuildVoter;
import com.fight2.model.User;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/guild")
public class GuildAction extends BaseAction {
    private static final long serialVersionUID = 184744772751526021L;
    @Autowired
    private UserDao userDao;
    @Autowired
    private GuildDao guildDao;
    @Autowired
    private GuildPollDao guildPollDao;
    @Autowired
    private GuildVoterDao guildVoterDao;
    private Guild guild;
    private List<Guild> datas;
    private int id;
    private String jsonMsg;

    // @Action(value = "list", results = { @Result(name = SUCCESS, location = "user_list.ftl") })
    // public String list() {
    // datas = guildDao.list();
    // return SUCCESS;
    // }

    @Action(value = "list-tops", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String listTopGuilds() {
        final List<Guild> list = guildDao.listTopGuilds(100);
        datas = Lists.newArrayList();
        for (final Guild guildPo : list) {
            final User president = guildPo.getPresident();
            final Guild guildVo = new Guild();
            guildVo.setId(guildPo.getId());
            guildVo.setName(guildPo.getName());
            final User presidentVo = new User();
            presidentVo.setId(president.getId());
            presidentVo.setName(president.getName());
            guildVo.setPresident(presidentVo);
            datas.add(guildVo);
        }
        jsonMsg = new Gson().toJson(datas);
        return SUCCESS;
    }

    @Action(value = "list", results = { @Result(name = SUCCESS, location = "guild_list.ftl") })
    public String list() {
        datas = guildDao.list();
        return SUCCESS;
    }

    @Action(value = "members", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String members() {
        final Guild guild = guildDao.load(id);
        final List<User> usersPos = userDao.listByGuild(guild);
        final List<User> users = Lists.newArrayList();
        for (final User userPo : usersPos) {
            final User user = new User();
            user.setId(userPo.getId());
            user.setName(userPo.getName());
            users.add(user);
        }
        jsonMsg = new Gson().toJson(users);
        return SUCCESS;
    }

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

    @Action(value = "join", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String joinGuild() {
        final Map<String, Integer> response = Maps.newHashMap();
        final User loginUser = this.getLoginUser();
        final User loginUserPo = userDao.get(loginUser.getId());
        final Guild guild = guildDao.load(id);
        if (loginUserPo.getGuild() == null) {
            loginUserPo.setGuild(guild);
            userDao.update(loginUserPo);
            response.put("status", 0);
        } else {
            response.put("status", 1);
        }

        jsonMsg = new Gson().toJson(response);
        return SUCCESS;
    }

    @Action(value = "view", results = { @Result(name = SUCCESS, location = "guild_form.ftl") })
    public String viewGuild() {
        final Guild guild = guildDao.load(id);
        final List<GuildPoll> polls = guildPollDao.listByGuild(guild);
        final ActionContext context = ActionContext.getContext();
        context.put("polls", polls);
        return SUCCESS;
    }

    @Action(value = "enable-poll", results = { @Result(name = SUCCESS, location = "list", type = "redirect") })
    public String enablePoll() {
        final Guild guild = guildDao.load(id);
        guild.setPollEnabled(true);
        guildDao.update(guild);
        final List<GuildPoll> polls = guildPollDao.list();
        for (final GuildPoll poll : polls) {
            guildPollDao.delete(poll);
        }
        final List<GuildVoter> voters = guildVoterDao.list();
        for (final GuildVoter voter : voters) {
            guildVoterDao.delete(voter);
        }
        return SUCCESS;

    }

    @Action(value = "disable-poll", results = { @Result(name = SUCCESS, location = "list", type = "redirect") })
    public String disablePoll() {
        final Guild guild = guildDao.load(id);
        guild.setPollEnabled(false);
        guildDao.update(guild);
        return SUCCESS;
    }

    @Action(value = "quit", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String quitGuild() {
        final Map<String, Integer> response = Maps.newHashMap();
        final User loginUser = this.getLoginUser();
        final User loginUserPo = userDao.get(loginUser.getId());
        final Guild userGuild = loginUserPo.getGuild();
        if (userGuild != null && userGuild.getPresident().getId() != loginUserPo.getId()) {
            loginUserPo.setGuild(null);
            userDao.update(loginUserPo);
            response.put("status", 0);
        } else {
            response.put("status", 1);
        }

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

    @Action(value = "vote", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String vote() {
        final Map<String, Integer> response = Maps.newHashMap();
        final User loginUser = this.getLoginUser();
        final User voter = userDao.load(loginUser.getId());
        final User candidate = userDao.load(id);
        final Guild guild = voter.getGuild();

        if (guild != candidate.getGuild()) {
            response.put("status", 2);
        } else if (guildVoterDao.hasVoted(guild, voter)) {
            response.put("status", 1);
        } else {
            response.put("status", 0);
            final GuildPoll guildPoll = guildPollDao.getByGuildAndCandidate(guild, candidate);
            if (guildPoll == null) {
                final GuildPoll guildPollNew = new GuildPoll();
                guildPollNew.setCandidate(candidate);
                guildPollNew.setGuild(guild);
                guildPollNew.setVotes(1);
                guildPollDao.add(guildPollNew);
            } else {
                guildPoll.setVotes(guildPoll.getVotes() + 1);
                guildPollDao.update(guildPoll);
            }
            final GuildVoter guildVoter = new GuildVoter();
            guildVoter.setGuild(guild);
            guildVoter.setVoter(voter);
            guildVoterDao.add(guildVoter);
        }

        jsonMsg = new Gson().toJson(response);
        return SUCCESS;
    }

    @Action(value = "has-voted", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String hasVoted() {
        final Map<String, Integer> response = Maps.newHashMap();
        final User loginUser = this.getLoginUser();
        final User voter = userDao.load(loginUser.getId());
        final Guild guild = voter.getGuild();
        response.put("status", guildVoterDao.hasVoted(guild, voter) ? 1 : 0);
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

    public GuildPollDao getGuildPollDao() {
        return guildPollDao;
    }

    public void setGuildPollDao(final GuildPollDao guildPollDao) {
        this.guildPollDao = guildPollDao;
    }

    public GuildVoterDao getGuildVoterDao() {
        return guildVoterDao;
    }

    public void setGuildVoterDao(final GuildVoterDao guildVoterDao) {
        this.guildVoterDao = guildVoterDao;
    }

}
