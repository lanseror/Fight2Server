package com.fight2.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.BidDao;
import com.fight2.dao.CardImageDao;
import com.fight2.dao.GuildDao;
import com.fight2.dao.GuildStoreroomDao;
import com.fight2.dao.UserDao;
import com.fight2.model.Bid;
import com.fight2.model.Guild;
import com.fight2.model.User;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

@Namespace("/bid")
public class BidAction extends BaseAction {
    private static final long serialVersionUID = 184744772751526021L;
    @Autowired
    private UserDao userDao;
    @Autowired
    private GuildDao guildDao;
    @Autowired
    private BidDao bidDao;
    @Autowired
    private GuildStoreroomDao guildStoreroomDao;
    @Autowired
    private CardImageDao cardImageDao;

    private Bid bid;
    private List<Bid> datas;
    private int id;
    private String jsonMsg;

    @Action(value = "list-by-guild", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String listByGuild() {
        final User loginUser = this.getLoginUser();
        final User userPo = userDao.get(loginUser.getId());
        final Guild guild = userPo.getGuild();
        final List<Bid> bids = bidDao.listGuildOpenBids(guild);
        final List<Bid> bidVos = Lists.newArrayList();
        for (final Bid bid : bids) {
            final Bid bidVo = new Bid();
            bidVo.setId(bid.getId());
            bidVo.setAmount(bid.getAmount());
            bidVo.setPrice(bid.getPrice());
            bidVo.setStatus(bid.getStatus());
            bidVo.setType(bid.getType());
            bidVos.add(bidVo);
        }
        jsonMsg = new Gson().toJson(bidVos);
        return SUCCESS;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }

    public GuildDao getGuildDao() {
        return guildDao;
    }

    public void setGuildDao(final GuildDao guildDao) {
        this.guildDao = guildDao;
    }

    public BidDao getBidDao() {
        return bidDao;
    }

    public void setBidDao(final BidDao bidDao) {
        this.bidDao = bidDao;
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

    public Bid getBid() {
        return bid;
    }

    public void setBid(final Bid bid) {
        this.bid = bid;
    }

    public List<Bid> getDatas() {
        return datas;
    }

    public void setDatas(final List<Bid> datas) {
        this.datas = datas;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getJsonMsg() {
        return jsonMsg;
    }

    public void setJsonMsg(final String jsonMsg) {
        this.jsonMsg = jsonMsg;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
