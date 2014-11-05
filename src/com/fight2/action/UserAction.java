package com.fight2.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.ArenaRankingDao;
import com.fight2.dao.CardDao;
import com.fight2.dao.CardImageDao;
import com.fight2.dao.PartyDao;
import com.fight2.dao.PartyGridDao;
import com.fight2.dao.PartyInfoDao;
import com.fight2.dao.UserDao;
import com.fight2.dao.UserStoreroomDao;
import com.fight2.model.ArenaRanking;
import com.fight2.model.BaseEntity;
import com.fight2.model.Card;
import com.fight2.model.Card.CardStatus;
import com.fight2.model.CardImage;
import com.fight2.model.Party;
import com.fight2.model.PartyGrid;
import com.fight2.model.PartyInfo;
import com.fight2.model.User;
import com.fight2.model.User.UserType;
import com.fight2.model.UserStoreroom;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
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
    @Autowired
    private UserStoreroomDao userStoreroomDao;
    @Autowired
    private CardImageDao cardImageDao;
    @Autowired
    private CardDao cardDao;
    @Autowired
    private ArenaRankingDao arenaRankingDao;
    private User user;
    private List<User> datas;
    private int id;
    private String installUUID;
    private String jsonMsg;

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
            user.setName("User" + user.getId());
            userDao.update(user);
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
            final UserStoreroom userStoreroom = new UserStoreroom();
            userStoreroom.setUser(user);
            userStoreroomDao.add(userStoreroom);
        }
        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", new Gson().toJson(user));
        return SUCCESS;
    }

    @Action(value = "parties", results = { @Result(name = SUCCESS, location = "user_parties.ftl") })
    public String parties() {
        user = userDao.get(id);
        final Set<Card> partyCards = Sets.newHashSet();
        final PartyInfo partyInfo = partyInfoDao.getByUser(user);
        final List<Party> parties = partyInfo.getParties();
        for (final Party party : parties) {
            for (final PartyGrid partyGrid : party.getPartyGrids()) {
                final Card card = partyGrid.getCard();
                if (card != null) {
                    final CardImage avatarObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_AVATAR, card.getTier(),
                            card.getCardTemplate());
                    card.setAvatar(avatarObj.getUrl());
                    partyCards.add(card);
                }
            }
        }
        final List<Card> cardpackCards = cardDao.listByUserAndStatus(user, CardStatus.InCardPack);
        final List<Card> nonPartyCards = Lists.newArrayList();
        for (final Card cardpackCard : cardpackCards) {
            if (!partyCards.contains(cardpackCard)) {
                final CardImage imageObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_THUMB, cardpackCard.getTier(),
                        cardpackCard.getCardTemplate());
                cardpackCard.setImage(imageObj.getUrl());
                nonPartyCards.add(cardpackCard);
            }
        }
        user.setCards(nonPartyCards);
        final ActionContext context = ActionContext.getContext();
        context.put("partyInfo", partyInfo);
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

    @Action(value = "save-user-info", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String saveUserInfo() {
        final User loginUser = this.getLoginUser();
        final User updateUser = userDao.get(loginUser.getId());
        final User user = new Gson().fromJson(jsonMsg, User.class);
        try {
            updateUser.setName(URLDecoder.decode(user.getName(), "UTF-8"));
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        userDao.update(updateUser);
        final Map<String, Integer> response = Maps.newHashMap();
        response.put("status", 0);
        jsonMsg = new Gson().toJson(response);
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

    @Action(value = "delete", results = { @Result(name = SUCCESS, location = "list", type = "redirect") })
    public String delete() {
        user = userDao.load(id);
        final User user = userDao.get(id);
        final PartyInfo partyInfo = partyInfoDao.getByUser(user);
        if (partyInfo != null) {
            final List<Party> parties = partyInfo.getParties();
            for (final Party party : parties) {
                for (final PartyGrid partyGrid : party.getPartyGrids()) {
                    partyGridDao.delete(partyGrid);
                }
                partyDao.delete(party);
            }
            partyInfoDao.delete(partyInfo);
        }
        final List<Card> cards = cardDao.listByUser(user);
        for (final Card card : cards) {
            cardDao.delete(card);
        }

        final List<ArenaRanking> arenaRankings = arenaRankingDao.listByUser(user);
        for (final ArenaRanking arenaRanking : arenaRankings) {
            arenaRankingDao.delete(arenaRanking);
        }
        final UserStoreroom userStorRoom = user.getStoreroom();
        if (userStorRoom != null) {
            userStoreroomDao.delete(userStorRoom);
        }
        userDao.delete(user);
        return SUCCESS;
    }

    @Action(value = "disable", results = { @Result(name = SUCCESS, location = "list", type = "redirect") })
    public String disable() {
        user = userDao.get(id);
        user.setDisabled(true);
        userDao.update(user);
        return SUCCESS;
    }

    @Action(value = "enable", results = { @Result(name = SUCCESS, location = "list", type = "redirect") })
    public String enable() {
        user = userDao.get(id);
        user.setDisabled(false);
        userDao.update(user);
        return SUCCESS;
    }

    @Action(value = "setnpc", results = { @Result(name = SUCCESS, location = "list", type = "redirect") })
    public String setNpc() {
        user = userDao.get(id);
        user.setType(UserType.ArenaGuardian);
        user.setName("Arena Guardian");
        userDao.update(user);
        return SUCCESS;
    }

    @Action(value = "notnpc", results = { @Result(name = SUCCESS, location = "list", type = "redirect") })
    public String notNpc() {
        user = userDao.get(id);
        user.setType(UserType.User);
        userDao.update(user);
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

    public CardImageDao getCardImageDao() {
        return cardImageDao;
    }

    public void setCardImageDao(final CardImageDao cardImageDao) {
        this.cardImageDao = cardImageDao;
    }

    public CardDao getCardDao() {
        return cardDao;
    }

    public void setCardDao(final CardDao cardDao) {
        this.cardDao = cardDao;
    }

    public ArenaRankingDao getArenaRankingDao() {
        return arenaRankingDao;
    }

    public void setArenaRankingDao(final ArenaRankingDao arenaRankingDao) {
        this.arenaRankingDao = arenaRankingDao;
    }

    public String getJsonMsg() {
        return jsonMsg;
    }

    public void setJsonMsg(final String jsonMsg) {
        this.jsonMsg = jsonMsg;
    }

    public UserStoreroomDao getUserStoreroomDao() {
        return userStoreroomDao;
    }

    public void setUserStoreroomDao(final UserStoreroomDao userStoreroomDao) {
        this.userStoreroomDao = userStoreroomDao;
    }

}
