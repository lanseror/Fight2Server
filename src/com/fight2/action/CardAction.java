package com.fight2.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.CardDao;
import com.fight2.dao.CardImageDao;
import com.fight2.dao.UserDao;
import com.fight2.model.Card;
import com.fight2.model.CardImage;
import com.fight2.model.CardTemplate;
import com.fight2.model.User;
import com.fight2.util.SummonHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/card")
public class CardAction extends BaseAction {
    private static final long serialVersionUID = -4473064014262040889L;
    @Autowired
    private CardDao cardDao;
    @Autowired
    private SummonHelper summonHelper;
    @Autowired
    private CardImageDao cardImageDao;
    @Autowired
    private UserDao userDao;
    private Card card;
    private List<Card> datas;
    private int id;

    @Action(value = "summon", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String summon() {
        final ActionContext context = ActionContext.getContext();
        final User sessionUser = (User) this.getSession().get(LOGIN_USER);
        final User user = userDao.get(sessionUser.getId());
        final Map<String, Object> jsonMap = Maps.newHashMap();
        if (user.getCardCount() >= 500) {
            jsonMap.put("status", 1);
            context.put("jsonMsg", new Gson().toJson(jsonMap));
            return SUCCESS;
        }

        final CardTemplate cardTemplate = summonHelper.summon();
        final String avatar = cardTemplate.getAvatars().get(0).getUrl();
        final String image = cardTemplate.getThumbImages().get(0).getUrl();
        final Card card = new Card();
        card.setAtk(cardTemplate.getAtk());
        card.setAvatar(avatar);
        card.setHp(cardTemplate.getHp());
        card.setImage(image);
        card.setName(cardTemplate.getName());
        card.setStar(cardTemplate.getStar());
        card.setCardTemplate(cardTemplate);
        card.setUser(user);
        cardDao.add(card);

        final Card cardVo = new Card();
        cardVo.setId(card.getId());
        cardVo.setAtk(card.getAtk());
        cardVo.setAvatar(avatar);
        cardVo.setHp(card.getHp());
        cardVo.setImage(image);
        cardVo.setName(card.getName());
        cardVo.setSkill(card.getSkill());
        cardVo.setStar(card.getStar());
        final CardTemplate cardTemplateVo = new CardTemplate();
        cardTemplateVo.setId(cardTemplate.getId());
        cardVo.setCardTemplate(cardTemplateVo);
        jsonMap.put("status", 0);
        jsonMap.put("card", cardVo);
        context.put("jsonMsg", new Gson().toJson(jsonMap));

        final List<Card> cards = cardDao.listByUser(user);

        user.setCardCount(cards.size());
        userDao.update(user);

        return SUCCESS;
    }

    @Action(value = "my-cards", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String myCards() {
        final User user = (User) this.getSession().get(LOGIN_USER);
        final List<Card> cards = cardDao.listByUser(user);
        final List<Card> voCards = Lists.newArrayList();
        for (final Card card : cards) {
            final CardImage avatarObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_AVATAR, card.getTier(), card.getCardTemplate());
            final String avatar = avatarObj.getUrl();
            final CardImage imageObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_THUMB, card.getTier(), card.getCardTemplate());
            final String image = imageObj.getUrl();
            final Card voCard = new Card();
            voCard.setId(card.getId());
            voCard.setAtk(card.getAtk());
            voCard.setAvatar(avatar);
            voCard.setHp(card.getHp());
            voCard.setImage(image);
            voCard.setName(card.getName());
            voCard.setSkill(card.getSkill());
            final CardTemplate cardTemplate = card.getCardTemplate();
            final CardTemplate cardTemplateVo = new CardTemplate();
            cardTemplateVo.setId(cardTemplate.getId());
            voCard.setCardTemplate(cardTemplateVo);
            voCards.add(voCard);
        }

        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", new Gson().toJson(voCards));
        return SUCCESS;
    }

    public CardDao getCardDao() {
        return cardDao;
    }

    public void setCardDao(final CardDao cardDao) {
        this.cardDao = cardDao;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(final Card card) {
        this.card = card;
    }

    public List<Card> getDatas() {
        return datas;
    }

    public void setDatas(final List<Card> datas) {
        this.datas = datas;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public SummonHelper getSummonHelper() {
        return summonHelper;
    }

    public void setSummonHelper(final SummonHelper summonHelper) {
        this.summonHelper = summonHelper;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public CardImageDao getCardImageDao() {
        return cardImageDao;
    }

    public void setCardImageDao(final CardImageDao cardImageDao) {
        this.cardImageDao = cardImageDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }

}
