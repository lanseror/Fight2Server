package com.fight2.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.fight2.model.Bid.BidItemType;
import com.fight2.model.Bid.BidStatus;
import com.fight2.model.Card;
import com.fight2.model.CardImage;
import com.fight2.model.CardTemplate;
import com.fight2.model.Guild;
import com.fight2.model.User;
import com.fight2.util.DateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
    private int version;
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
            final BidItemType itemType = bid.getType();
            if (itemType == BidItemType.Card) {
                final Card card = bid.getGuildCard().getCard();
                final Card cardVo = new Card(card);
                final CardTemplate cardTemplate = card.getCardTemplate();
                final CardImage thumbObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_THUMB, card.getTier(), cardTemplate);
                cardVo.setImage(thumbObj.getUrl());
                cardVo.setRace(cardTemplate.getRace());
                bidVo.setCard(cardVo);
            }
            bidVo.setType(itemType);
            final User bidUser = bid.getUser();
            if (bidUser != null && bidUser.getId() == userPo.getId()) {
                bidVo.setMyBid(true);
            }
            bidVo.setVersion(bid.getVersion());
            final Date endDate = bid.getEndDate();
            if (endDate != null) {
                bidVo.setRemainTime(DateUtils.getRemainTimeInSecond(endDate));
            }

            bidVos.add(bidVo);
        }
        jsonMsg = new Gson().toJson(bidVos);
        return SUCCESS;
    }

    @Action(value = "check-my-bid", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String checkMyBid() {
        final Map<String, Object> response = Maps.newHashMap();
        final User loginUser = this.getLoginUser();
        final User userPo = userDao.get(loginUser.getId());
        final Guild guild = userPo.getGuild();
        final Bid bid = bidDao.get(id);

        if (guild != bid.getGuild()) {
            response.put("status", 3);
        } else if (bid.getStatus() != BidStatus.Closed) {
            response.put("status", 2);
        } else {
            final User bidUser = bid.getUser();
            if (bidUser != null && bidUser.getId() == userPo.getId()) {
                response.put("status", 0);
            } else {
                response.put("status", 1);
            }
        }

        jsonMsg = new Gson().toJson(response);
        return SUCCESS;
    }

    @Action(value = "upgrade", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String upgrade() {
        final List<Bid> bids = bidDao.list();
        for (final Bid bid : bids) {
            final Date now = new Date();
            bid.setStartDate(now);
            bid.setEndDate(org.apache.commons.lang3.time.DateUtils.addMinutes(now, 5));
            bidDao.update(bid);
        }

        final Map<String, Integer> response = Maps.newHashMap();
        response.put("status", 0);
        jsonMsg = new Gson().toJson(response);
        return SUCCESS;
    }

    @Action(value = "bid", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String bid() {
        final Map<String, Object> response = Maps.newHashMap();
        final User loginUser = this.getLoginUser();
        final User userPo = userDao.get(loginUser.getId());
        final Guild guild = userPo.getGuild();
        final Bid bid = bidDao.get(id);

        if (guild != bid.getGuild()) {
            response.put("status", 3);
        } else if (bid.getStatus() != BidStatus.Started) {
            response.put("status", 4);
        } else {
            performBid(response, userPo, bidDao, id, version);
        }

        jsonMsg = new Gson().toJson(response);
        return SUCCESS;
    }

    private static synchronized void performBid(final Map<String, Object> response, final User user, final BidDao bidDao, final int id,
            final int version) {
        final Bid bid = bidDao.get(id);
        final Bid bidVo = new Bid();
        final Date endDate = bid.getEndDate();
        if (endDate != null) {
            bidVo.setRemainTime(DateUtils.getRemainTimeInSecond(endDate));
        }
        if (version != bid.getVersion()) {
            response.put("status", 2);
        } else {
            bid.setPrice(bid.getPrice() + 5);
            bid.setVersion(bid.getVersion() + 1);
            bid.setUser(user);
            bidDao.update(bid);
            bidVo.setMyBid(true);
            response.put("status", 0);
        }
        bidVo.setId(bid.getId());
        bidVo.setPrice(bid.getPrice());
        bidVo.setVersion(bid.getVersion());
        bidVo.setStatus(bid.getStatus());
        response.put("bid", bidVo);
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

    public int getVersion() {
        return version;
    }

    public void setVersion(final int version) {
        this.version = version;
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
