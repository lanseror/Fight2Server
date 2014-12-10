package com.fight2.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;

import com.fight2.dao.BidDao;
import com.fight2.dao.CardImageDao;
import com.fight2.dao.GuildArenaUserDao;
import com.fight2.dao.GuildCardDao;
import com.fight2.dao.GuildDao;
import com.fight2.dao.GuildPollDao;
import com.fight2.dao.GuildStoreroomDao;
import com.fight2.dao.GuildVoterDao;
import com.fight2.dao.UserDao;
import com.fight2.model.Bid;
import com.fight2.model.Bid.BidItemType;
import com.fight2.model.Bid.BidStatus;
import com.fight2.model.Card;
import com.fight2.model.CardImage;
import com.fight2.model.CardTemplate;
import com.fight2.model.Guild;
import com.fight2.model.GuildArenaUser;
import com.fight2.model.GuildCard;
import com.fight2.model.GuildPoll;
import com.fight2.model.GuildStoreroom;
import com.fight2.model.GuildVoter;
import com.fight2.model.User;
import com.fight2.service.BidService;
import com.fight2.util.BidUtils;
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
    @Autowired
    private GuildArenaUserDao guildArenaUserDao;
    @Autowired
    private GuildStoreroomDao guildStoreroomDao;
    @Autowired
    private CardImageDao cardImageDao;
    @Autowired
    private GuildCardDao guildCardDao;
    @Autowired
    private BidDao bidDao;
    @Autowired
    private BidService bidService;
    @Autowired
    private TaskScheduler taskScheduler;

    private Guild guild;
    private List<Guild> datas;
    private int id;
    private BidItemType itemType;
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
            if (guildDao.getByPresident(loginUserPo) == null && loginUserPo.getGuild() == null) {
                guild.setName(URLDecoder.decode(guild.getName(), "UTF-8"));
                guild.setPresident(loginUserPo);
                guild.setCreateDate(new Date());
                guildDao.add(guild);
                final GuildStoreroom guildStoreroom = new GuildStoreroom();
                guildStoreroom.setGuild(guild);
                guildStoreroomDao.add(guildStoreroom);
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

    @Action(value = "get-storeroom", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String getStoreroom() {
        final User loginUser = this.getLoginUser();
        final User userPo = userDao.get(loginUser.getId());
        final Guild guild = userPo.getGuild();
        final GuildStoreroom storeroom = guild.getStoreroom();

        final List<GuildCard> guildCards = storeroom.getGuildCards();
        final Map<CardTemplate, Integer> cardTemplates = Maps.newLinkedHashMap();
        for (final GuildCard guildCard : guildCards) {
            final Card card = guildCard.getCard();
            final CardTemplate cardTemplate = card.getCardTemplate();
            if (cardTemplates.containsKey(cardTemplate)) {
                final Integer count = cardTemplates.get(cardTemplate);
                cardTemplates.put(cardTemplate, count + 1);
            } else {
                cardTemplates.put(cardTemplate, 1);
            }
        }

        final List<Card> cardVos = Lists.newArrayList();
        for (final Entry<CardTemplate, Integer> entry : cardTemplates.entrySet()) {
            final CardTemplate template = entry.getKey();
            final int count = entry.getValue();
            final Card cardVo = new Card();
            cardVo.setId(template.getId());
            cardVo.setHp(template.getHp());
            cardVo.setAtk(template.getAtk());
            cardVo.setName(template.getName());
            cardVo.setStar(template.getStar());
            final CardImage avatarObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_AVATAR, 1, template);
            cardVo.setAvatar(avatarObj.getUrl());
            final CardImage imageObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_THUMB, 1, template);
            cardVo.setImage(imageObj.getUrl());
            cardVo.setRace(template.getRace());
            cardVo.setAmount(count);
            cardVos.add(cardVo);
        }

        final GuildStoreroom storeroomVo = new GuildStoreroom();
        storeroomVo.setId(storeroom.getId());
        storeroomVo.setCoin(storeroom.getCoin());
        storeroomVo.setStamina(storeroom.getStamina());
        storeroomVo.setTicket(storeroom.getTicket());
        storeroomVo.setCards(cardVos);

        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", new Gson().toJson(storeroomVo));
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

    @Action(value = "view", results = { @Result(name = SUCCESS, location = "guild_form.ftl") })
    public String viewGuild() {
        final Guild guild = guildDao.load(id);
        final List<GuildPoll> polls = guildPollDao.listByGuild(guild);
        final ActionContext context = ActionContext.getContext();
        context.put("polls", polls);
        return SUCCESS;
    }

    @Action(value = "open-poll", results = { @Result(name = SUCCESS, location = "list", type = "redirect") })
    public String openPoll() {
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

    @Action(value = "close-poll", results = { @Result(name = SUCCESS, location = "list", type = "redirect") })
    public String closePoll() {
        final Guild guild = guildDao.load(id);
        guild.setPollEnabled(false);
        guildDao.update(guild);
        final List<GuildPoll> polls = guildPollDao.list();
        int maxVotes = 0;
        int presidentVotes = 0;
        User maxPollCandidate = guild.getPresident();
        for (final GuildPoll poll : polls) {
            final int votes = poll.getVotes();
            final User candidate = poll.getCandidate();
            if (votes > maxVotes) {
                maxVotes = votes;
                maxPollCandidate = candidate;
            }
            if (candidate == guild.getPresident()) {
                presidentVotes = votes;
            }
        }

        if (maxVotes > presidentVotes) {
            guild.setPresident(maxPollCandidate);
            guildDao.update(guild);
        }
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
            guildVo.setPollEnabled(guild.isPollEnabled());
            final User presidentVo = new User();
            presidentVo.setId(president.getId());
            presidentVo.setName(president.getName());
            guildVo.setPresident(presidentVo);
            final List<GuildArenaUser> guildArenaUsers = guildArenaUserDao.listByGuild(guild);
            final List<GuildArenaUser> arenaUserVos = Lists.newArrayList();
            for (final GuildArenaUser guildArenaUser : guildArenaUsers) {
                final GuildArenaUser guildArenaUserVo = new GuildArenaUser();
                final User arenaUser = guildArenaUser.getUser();
                guildArenaUserVo.setId(arenaUser.getId());
                guildArenaUserVo.setLocked(guildArenaUser.isLocked());
                arenaUserVos.add(guildArenaUserVo);
            }
            guildVo.setArenaUsers(arenaUserVos);

            response.put("guild", guildVo);
            response.put("guildContribution", loginUserPo.getGuildContribution());
        }

        jsonMsg = new Gson().toJson(response);
        return SUCCESS;
    }

    @Action(value = "add-arena-user", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String addArenaUser() {
        final Map<String, Integer> response = Maps.newHashMap();
        final User loginUser = this.getLoginUser();
        final User loginUserPo = userDao.load(loginUser.getId());
        final Guild guild = loginUserPo.getGuild();
        if (guild.getPresident() == loginUserPo) {
            final User arenaUser = userDao.load(id);
            final GuildArenaUser guildArenaUser = new GuildArenaUser();
            guildArenaUser.setGuild(guild);
            guildArenaUser.setUser(arenaUser);
            guildArenaUserDao.add(guildArenaUser);
            response.put("status", 0);
        } else {
            response.put("status", 1);
        }

        jsonMsg = new Gson().toJson(response);
        return SUCCESS;
    }

    @Action(value = "remove-arena-user", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String removeArenaUser() {
        final Map<String, Integer> response = Maps.newHashMap();
        final User loginUser = this.getLoginUser();
        final User loginUserPo = userDao.load(loginUser.getId());
        final Guild guild = loginUserPo.getGuild();
        if (guild.getPresident() == loginUserPo) {
            final User arenaUser = userDao.load(id);
            final GuildArenaUser guildArenaUser = guildArenaUserDao.getByUser(arenaUser);
            if (guildArenaUser.isLocked()) {
                response.put("status", 2);
            } else {
                guildArenaUserDao.delete(guildArenaUser);
                response.put("status", 0);
            }

        } else {
            response.put("status", 1);
        }

        jsonMsg = new Gson().toJson(response);
        return SUCCESS;
    }

    @Action(value = "sent-card-to-bid", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String sentCardToBid() {
        final Map<String, Integer> response = Maps.newHashMap();
        final User loginUser = this.getLoginUser();
        final User loginUserPo = userDao.load(loginUser.getId());
        final Guild guild = loginUserPo.getGuild();
        final GuildStoreroom storeroom = guild.getStoreroom();
        final List<Bid> bids = bidDao.listGuildOpenBids(guild);

        if (guild.getPresident() != loginUserPo) {
            response.put("status", 3);
        } else if (bids.size() >= 3) {
            response.put("status", 2);
        } else {
            response.put("status", 1);
            final List<GuildCard> guildCards = storeroom.getGuildCards();
            for (final GuildCard guildCard : guildCards) {
                if (guildCard.getCard().getCardTemplate().getId() == id) {
                    guildCard.setGuildStoreroom(null);
                    final Bid bid = new Bid();
                    bid.setAmount(1);
                    bid.setGuild(guild);
                    bid.setGuildCard(guildCard);
                    bid.setPrice(1);
                    bid.setStatus(BidStatus.Started);
                    bid.setType(BidItemType.Card);
                    final Date now = new Date();
                    bid.setStartDate(now);
                    bid.setEndDate(DateUtils.addMinutes(now, 5));
                    bidDao.add(bid);
                    scheduleStopTask(bid);
                    guildCardDao.update(guildCard);
                    response.put("status", 0);
                    break;
                }
            }
        }
        jsonMsg = new Gson().toJson(response);
        return SUCCESS;
    }

    @Action(value = "sent-item-to-bid", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String sentItemToBid() {
        final Map<String, Integer> response = Maps.newHashMap();
        final User loginUser = this.getLoginUser();
        final User loginUserPo = userDao.load(loginUser.getId());
        final Guild guild = loginUserPo.getGuild();
        final List<Bid> bids = bidDao.listGuildOpenBids(guild);

        if (guild.getPresident() != loginUserPo) {
            response.put("status", 3);
        } else if (bids.size() >= 3) {
            response.put("status", 2);
        } else {
            response.put("status", 1);
            final Bid bid = new Bid();
            bid.setAmount(5);
            bid.setGuild(guild);
            bid.setPrice(1);
            bid.setStatus(BidStatus.Started);
            bid.setType(itemType);
            final Date now = new Date();
            bid.setStartDate(now);
            bid.setEndDate(DateUtils.addMinutes(now, 5));
            final GuildStoreroom storeroom = guild.getStoreroom();
            if (itemType == BidItemType.ArenaTicket && storeroom.getTicket() >= 5) {
                bidDao.add(bid);
                scheduleStopTask(bid);
                response.put("status", 0);
                storeroom.setTicket(storeroom.getTicket() - 5);
                guildStoreroomDao.update(storeroom);
            } else if (itemType == BidItemType.Stamina && storeroom.getStamina() >= 5) {
                bidDao.add(bid);
                scheduleStopTask(bid);
                response.put("status", 0);
                storeroom.setStamina(storeroom.getStamina() - 5);
                guildStoreroomDao.update(storeroom);
            }
        }
        jsonMsg = new Gson().toJson(response);
        return SUCCESS;
    }

    private void scheduleStopTask(final Bid bid) {
        final Runnable stopSchedule = BidUtils.createCloseSchedule(bid.getId(), bidService);
        taskScheduler.schedule(stopSchedule, bid.getEndDate());
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

    @Override
    public String getJsonMsg() {
        return jsonMsg;
    }

    @Override
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

    public GuildArenaUserDao getGuildArenaUserDao() {
        return guildArenaUserDao;
    }

    public void setGuildArenaUserDao(final GuildArenaUserDao guildArenaUserDao) {
        this.guildArenaUserDao = guildArenaUserDao;
    }

    public GuildStoreroomDao getGuildStoreroomDao() {
        return guildStoreroomDao;
    }

    public void setGuildStoreroomDao(final GuildStoreroomDao guildStoreroomDao) {
        this.guildStoreroomDao = guildStoreroomDao;
    }

    public CardImageDao getCardImageDao() {
        return cardImageDao;
    }

    public void setCardImageDao(final CardImageDao cardImageDao) {
        this.cardImageDao = cardImageDao;
    }

    public GuildCardDao getGuildCardDao() {
        return guildCardDao;
    }

    public void setGuildCardDao(final GuildCardDao guildCardDao) {
        this.guildCardDao = guildCardDao;
    }

    public BidDao getBidDao() {
        return bidDao;
    }

    public void setBidDao(final BidDao bidDao) {
        this.bidDao = bidDao;
    }

    public BidItemType getItemType() {
        return itemType;
    }

    public void setItemType(final BidItemType itemType) {
        this.itemType = itemType;
    }

    public TaskScheduler getTaskScheduler() {
        return taskScheduler;
    }

    public void setTaskScheduler(final TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public BidService getBidService() {
        return bidService;
    }

    public void setBidService(final BidService bidService) {
        this.bidService = bidService;
    }

}
