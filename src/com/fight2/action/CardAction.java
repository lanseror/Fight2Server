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
import com.fight2.model.Card.CardStatus;
import com.fight2.model.CardImage;
import com.fight2.model.CardTemplate;
import com.fight2.model.User;
import com.fight2.service.PartyService;
import com.fight2.util.CardUtils;
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
    private PartyService partyService;
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
        final List<Card> cardpackCards = cardDao.listByUserAndStatus(user, CardStatus.InCardPack);
        final int cardpackSize = cardpackCards.size();
        final CardTemplate cardTemplate = summonHelper.summon(4, 6);
        final String avatar = cardTemplate.getAvatars().get(0).getUrl();
        final String image = cardTemplate.getThumbImages().get(0).getUrl();

        final Card card = new Card();
        card.setAtk(cardTemplate.getAtk());
        card.setAvatar(avatar);
        card.setHp(cardTemplate.getHp());
        card.setImage(image);
        card.setRace(cardTemplate.getRace());
        card.setName(cardTemplate.getName());
        card.setStar(cardTemplate.getStar());
        card.setCardTemplate(cardTemplate);
        if (cardpackSize < User.USER_CARDPACK_SIZE) {
            card.setStatus(CardStatus.InCardPack);
        } else {
            card.setStatus(CardStatus.InStoreroom);
        }
        card.setUser(user);
        cardDao.add(card);

        final Card cardVo = new Card();
        cardVo.setId(card.getId());
        cardVo.setAtk(card.getAtk());
        cardVo.setAvatar(avatar);
        cardVo.setHp(card.getHp());
        cardVo.setImage(image);
        cardVo.setRace(cardTemplate.getRace());
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

    @Action(value = "delete",
            results = { @Result(name = SUCCESS, location = "../user/parties.action", type = "redirect", params = { "id", "${id}" }) })
    public String delete() {
        card = cardDao.load(id);
        final User user = card.getUser();
        cardDao.delete(card);
        final List<Card> cards = cardDao.listByUser(user);
        user.setCardCount(cards.size());
        userDao.update(user);
        id = user.getId();
        return SUCCESS;
    }

    @Action(value = "my-cards", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String myCards() {
        final User user = (User) this.getSession().get(LOGIN_USER);
        final List<Card> cards = cardDao.listByUserAndStatus(user, CardStatus.InCardPack);
        final List<Card> voCards = Lists.newArrayList();
        for (final Card card : cards) {
            final CardTemplate cardTemplate = card.getCardTemplate();
            final CardImage avatarObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_AVATAR, card.getTier(), cardTemplate);
            final String avatar = avatarObj.getUrl();
            final CardImage imageObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_THUMB, card.getTier(), cardTemplate);
            final String image = imageObj.getUrl();
            final Card voCard = new Card();
            voCard.setId(card.getId());
            voCard.setStar(card.getStar());
            voCard.setLevel(card.getLevel());
            voCard.setTier(card.getTier());
            voCard.setAtk(card.getAtk());
            voCard.setBaseExp(card.getBaseExp());
            voCard.setExp(card.getExp());
            voCard.setAvatar(avatar);
            voCard.setHp(card.getHp());
            voCard.setImage(image);
            voCard.setName(card.getName());
            voCard.setSkill(card.getSkill());
            final CardTemplate cardTemplateVo = new CardTemplate();
            cardTemplateVo.setId(cardTemplate.getId());
            voCard.setCardTemplate(cardTemplateVo);
            voCard.setRace(cardTemplate.getRace());
            voCards.add(voCard);
        }

        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", new Gson().toJson(voCards));
        return SUCCESS;
    }

    @Action(value = "upgrade", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String upgrade() {
        final User user = (User) this.getSession().get(LOGIN_USER);
        @SuppressWarnings("unchecked")
        final List<Double> cardIds = new Gson().fromJson(jsonMsg, List.class);

        final List<Card> cards = Lists.newArrayList();
        for (final Double cardIdDouble : cardIds) {
            final int cardId = cardIdDouble.intValue();
            final Card card = cardDao.get(cardId);
            if (card.getUser().getId() != user.getId()) {
                return INPUT;
            }
            cards.add(card);
        }

        final Card mainCard = cards.get(0);
        cards.remove(0);// SupportCards left.

        for (final Card supportCard : cards) {
            final int baseExp = CardUtils.getBaseExp(supportCard);
            final int exp = supportCard.getExp() / 2;
            final int addExp = baseExp + exp;
            mainCard.setExp(mainCard.getExp() + addExp);
            cardDao.delete(supportCard);
        }

        CardUtils.upgrade(mainCard);
        cardDao.update(mainCard);

        final Map<String, Object> jsonMap = Maps.newHashMap();
        jsonMap.put("status", 0);
        final Card card = new Card(mainCard);
        jsonMap.put("card", card);

        jsonMsg = new Gson().toJson(jsonMap);

        return SUCCESS;
    }

    @Action(value = "evo", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String evolution() {
        final User user = (User) this.getSession().get(LOGIN_USER);
        @SuppressWarnings("unchecked")
        final List<Double> cardIds = new Gson().fromJson(jsonMsg, List.class);

        final List<Card> cards = Lists.newArrayList();
        for (final Double cardIdDouble : cardIds) {
            final int cardId = cardIdDouble.intValue();
            final Card card = cardDao.get(cardId);
            if (card.getUser().getId() != user.getId()) {
                return INPUT;
            }
            cards.add(card);
        }

        if (cards.size() != 2) {
            return INPUT;
        }

        final Card card1 = cards.get(0);
        final Card card2 = cards.get(1);

        if (cards.size() != 2) {
            return INPUT;
        }
        if (card1.getCardTemplate().getId() != card2.getCardTemplate().getId()) {
            return INPUT;
        }
        if (CardUtils.getMaxEvoTier(card1) == card1.getTier() || CardUtils.getMaxEvoTier(card2) == card2.getTier()) {
            return INPUT;
        }

        CardUtils.evolution(card1, card2);

        final boolean isCard1InParty = partyService.isCardInParty(card1);
        final Card mainCard = isCard1InParty ? card1 : card2;
        final Card supportCard = isCard1InParty ? card2 : card1;
        cardDao.delete(supportCard);
        cardDao.update(mainCard);

        partyService.refreshPartyHpAtk(user.getId());

        final Map<String, Object> jsonMap = Maps.newHashMap();
        jsonMap.put("status", 0);
        final Card card = new Card(mainCard);
        final CardImage avatarObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_AVATAR, card.getTier(), card1.getCardTemplate());
        final String avatar = avatarObj.getUrl();
        card.setAvatar(avatar);
        final CardImage imageObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_THUMB, card.getTier(), card1.getCardTemplate());
        final String image = imageObj.getUrl();
        card.setImage(image);
        jsonMap.put("card", card);

        jsonMsg = new Gson().toJson(jsonMap);

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

    public PartyService getPartyService() {
        return partyService;
    }

    public void setPartyService(final PartyService partyService) {
        this.partyService = partyService;
    }

}
