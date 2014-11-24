package com.fight2.action;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.CardDao;
import com.fight2.dao.CardImageDao;
import com.fight2.dao.UserDao;
import com.fight2.dao.UserStoreroomDao;
import com.fight2.model.Card;
import com.fight2.model.Card.CardStatus;
import com.fight2.model.CardImage;
import com.fight2.model.CardTemplate;
import com.fight2.model.User;
import com.fight2.model.UserStoreroom;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/user-storeroom")
public class UserStoreroomAction extends BaseAction {
    private static final long serialVersionUID = -4473064014262040889L;
    @Autowired
    private UserStoreroomDao userStoreroomDao;
    @Autowired
    private CardDao cardDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CardImageDao cardImageDao;
    private List<UserStoreroom> datas;
    private int id;
    private UserStoreroom userStoreroom;

    @Action(value = "view", results = { @Result(name = SUCCESS, location = "user_storeroom_form.ftl") })
    public String view() {
        userStoreroom = userStoreroomDao.get(id);
        final List<Card> cards = cardDao.listByUserAndStatus(userStoreroom.getUser(), CardStatus.InStoreroom);
        for (final Card card : cards) {
            final CardTemplate template = card.getCardTemplate();
            final CardImage imageObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_THUMB, card.getTier(), template);
            card.setImage(imageObj.getUrl());
            card.setRace(template.getRace());
        }
        userStoreroom.setCards(cards);
        return SUCCESS;
    }

    @Action(value = "get", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String getByLoginUser() {
        final User loginUser = this.getLoginUser();
        final User userPo = userDao.get(loginUser.getId());
        final UserStoreroom userStoreroomPo = userPo.getStoreroom();
        final List<Card> cards = cardDao.listByUserAndStatus(userPo, CardStatus.InStoreroom);
        final Map<CardTemplate, Integer> cardTemplates = Maps.newLinkedHashMap();
        for (final Card card : cards) {
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
            final CardImage imageObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_THUMB, 1, template);
            cardVo.setImage(imageObj.getUrl());
            cardVo.setRace(template.getRace());
            cardVo.setAmount(count);
            cardVos.add(cardVo);
        }

        final UserStoreroom userStoreroomVo = new UserStoreroom();
        userStoreroomVo.setId(userStoreroomPo.getId());
        userStoreroomVo.setCoin(userStoreroomPo.getCoin());
        userStoreroomVo.setStamina(userStoreroomPo.getStamina());
        userStoreroomVo.setTicket(userStoreroomPo.getTicket());
        userStoreroomVo.setCards(cardVos);

        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", new Gson().toJson(userStoreroomVo));
        return SUCCESS;
    }

    @Action(value = "receive-card", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String receiveCard() {
        final User loginUser = this.getLoginUser();
        final User userPo = userDao.get(loginUser.getId());
        final List<Card> cardpackCards = cardDao.listByUserAndStatus(userPo, CardStatus.InCardPack);
        final int cardpackSize = cardpackCards.size();
        final List<Card> cards = cardDao.listByUserAndStatus(userPo, CardStatus.InStoreroom);
        final int templateId = id;
        int receiveSize = 0;
        for (final Card card : cards) {
            final CardTemplate cardTemplate = card.getCardTemplate();
            if (cardTemplate.getId() == templateId && cardpackSize + receiveSize < User.USER_CARDPACK_SIZE) {
                receiveSize++;
                card.setStatus(CardStatus.InCardPack);
                cardDao.update(card);
            }
        }

        final Map<String, Integer> response = Maps.newHashMap();
        response.put("size", receiveSize);
        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", new Gson().toJson(response));
        return SUCCESS;
    }

    public UserStoreroomDao getUserStoreroomDao() {
        return userStoreroomDao;
    }

    public void setUserStoreroomDao(final UserStoreroomDao userStoreroomDao) {
        this.userStoreroomDao = userStoreroomDao;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public List<UserStoreroom> getDatas() {
        return datas;
    }

    public void setDatas(final List<UserStoreroom> datas) {
        this.datas = datas;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public UserStoreroom getUserStoreroom() {
        return userStoreroom;
    }

    public void setUserStoreroom(final UserStoreroom userStoreroom) {
        this.userStoreroom = userStoreroom;
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

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }

}
