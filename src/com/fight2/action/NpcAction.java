package com.fight2.action;

import java.util.List;
import java.util.Set;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.CardDao;
import com.fight2.dao.CardImageDao;
import com.fight2.dao.CardTemplateDao;
import com.fight2.dao.PartyDao;
import com.fight2.dao.PartyGridDao;
import com.fight2.dao.PartyInfoDao;
import com.fight2.dao.UserDao;
import com.fight2.dao.UserStoreroomDao;
import com.fight2.model.BaseEntity;
import com.fight2.model.Card;
import com.fight2.model.Card.CardStatus;
import com.fight2.model.CardImage;
import com.fight2.model.CardTemplate;
import com.fight2.model.Party;
import com.fight2.model.PartyGrid;
import com.fight2.model.PartyInfo;
import com.fight2.model.User;
import com.fight2.util.CardUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/npc")
public class NpcAction extends BaseAction {
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
    private CardTemplateDao cardTemplateDao;
    private List<Card> cards;
    private User user;
    private List<User> datas;
    private int id;
    private String jsonMsg;
    private int[] cardIds;
    private int[] partyIds;
    private int[] levels;
    private int[] tiers;

    @Action(value = "list", results = { @Result(name = SUCCESS, location = "npc_list.ftl") })
    public String list() {
        datas = userDao.getAllNpc();
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
                final CardTemplate template = cardpackCard.getCardTemplate();
                final CardImage imageObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_THUMB, cardpackCard.getTier(), template);
                cardpackCard.setImage(imageObj.getUrl());
                cardpackCard.setRace(template.getRace());
                nonPartyCards.add(cardpackCard);
            }
        }
        user.setCards(nonPartyCards);
        final ActionContext context = ActionContext.getContext();
        context.put("partyInfo", partyInfo);
        return SUCCESS;
    }

    @Action(value = "add", results = { @Result(name = SUCCESS, location = "npc_form.ftl") })
    public String add() {
        loadCardData();
        return SUCCESS;
    }

    @Action(value = "edit", results = { @Result(name = SUCCESS, location = "npc_form.ftl") })
    public String edit() {
        user = userDao.get(id);
        loadCardData();
        return SUCCESS;
    }

    private void loadCardData() {
        final List<CardTemplate> cardTemplates = cardTemplateDao.list();
        cards = Lists.newArrayList();
        for (final CardTemplate cardTemplate : cardTemplates) {
            final Card card = new Card();
            card.setId(cardTemplate.getId());
            card.setName(cardTemplate.getName());
            final CardImage avatarObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_AVATAR, 1, cardTemplate);
            card.setAvatar(avatarObj.getUrl());
            cards.add(card);
        }
    }

    @Action(value = "save", interceptorRefs = { @InterceptorRef("tokenSession"), @InterceptorRef("defaultStack") }, results = {
            @Result(name = SUCCESS, location = "list", type = "redirect"), @Result(name = INPUT, location = "npc_form.ftl") })
    public String save() {
        if (partyIds[0] != 0) {
            user = null;
            loadCardData();
            this.addActionError("你必须要有一个领军人物");
            return INPUT;
        }
        // final Set<Integer> cardIdSet = Sets.newHashSet();
        // for (final int cardId : cardIds) {
        // cardIdSet.add(cardId);
        // }
        // if (cardIds.length != cardIdSet.size()) {
        // user = null;
        // loadCardData();
        // this.addActionError("发现有重复的卡牌");
        // return INPUT;
        // }
        // final CardImage avatarObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_AVATAR, card.getTier(), card.getCardTemplate());
        if (user.getId() == BaseEntity.EMPTY_ID) {
            return createSave();
        } else {
            return editSave();
        }
    }

    private String createSave() {
        userDao.add(user);
        final PartyInfo partyInfoAdd = new PartyInfo();
        partyInfoAdd.setUser(user);
        partyInfoDao.add(partyInfoAdd);
        final List<Party> parties = Lists.newArrayList();
        for (int i = 1; i < 4; i++) {
            final Party party = new Party();
            party.setPartyNumber(i);
            party.setPartyInfo(partyInfoAdd);
            partyDao.add(party);
            parties.add(party);
            final List<PartyGrid> partyGrids = Lists.newArrayList();
            party.setPartyGrids(partyGrids);
            for (int gridIndex = 1; gridIndex < 5; gridIndex++) {
                final PartyGrid partyGrid = new PartyGrid();
                partyGrid.setGridNumber(gridIndex);
                partyGrid.setParty(party);
                partyGridDao.add(partyGrid);
                partyGrids.add(partyGrid);
            }
        }
        for (int i = 0; i < cardIds.length; i++) {
            final int cardId = cardIds[i];
            final int partyId = partyIds[i];
            final int level = levels[i];
            final int tier = tiers[i];
            final Party party = parties.get(partyId / 4);
            final List<PartyGrid> partyGrids = party.getPartyGrids();
            final PartyGrid partyGrid = partyGrids.get(partyId % 4);
            final CardTemplate cardTemplate = cardTemplateDao.load(cardId);
            final Card card = new Card();
            card.setAtk(cardTemplate.getAtk());
            card.setHp(cardTemplate.getHp());
            card.setName(cardTemplate.getName());
            card.setStar(cardTemplate.getStar());
            card.setCardTemplate(cardTemplate);
            card.setUser(user);
            card.setStatus(CardStatus.InCardPack);
            upgradeCard(card, tier, level);
            cardDao.add(card);
            partyGrid.setCard(card);
            partyGridDao.update(partyGrid);
            party.setAtk(party.getAtk() + card.getAtk());
            party.setHp(party.getHp() + card.getHp());
            partyInfoAdd.setAtk(partyInfoAdd.getAtk() + card.getAtk());
            partyInfoAdd.setHp(partyInfoAdd.getHp() + card.getHp());
            if (i == 0) {
                final CardImage avatarObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_AVATAR, card.getTier(), card.getCardTemplate());
                user.setAvatar(avatarObj.getUrl());
                userDao.update(user);
            }
        }
        for (final Party party : parties) {
            partyDao.update(party);
        }
        partyInfoDao.update(partyInfoAdd);

        return SUCCESS;
    }

    private void upgradeCard(final Card card, final int tier, final int level) {
        if (tier != 1) {
            final int maxTier = CardUtils.getMaxEvoTier(card);
            for (int i = 2; i <= maxTier; i++) {
                final int maxLevel = CardUtils.getMaxLevel(card);
                final int maxLevelExp = CardUtils.getLevelExp(card, maxLevel);
                card.setExp(maxLevelExp);
                CardUtils.upgrade(card);
                final Card supportCard = new Card(card);
                CardUtils.evolution(card, supportCard);
            }
        }

        final int levelExp = CardUtils.getLevelExp(card, level);
        card.setExp(levelExp);
        CardUtils.upgrade(card);
    }

    private String editSave() {
        final User userUpdate = userDao.load(user.getId());
        userUpdate.setName(user.getName());
        userUpdate.setSalary(user.getSalary());
        userUpdate.setType(user.getType());
        userDao.update(userUpdate);
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

    public CardTemplateDao getCardTemplateDao() {
        return cardTemplateDao;
    }

    public void setCardTemplateDao(final CardTemplateDao cardTemplateDao) {
        this.cardTemplateDao = cardTemplateDao;
    }

    @Override
    public String getJsonMsg() {
        return jsonMsg;
    }

    @Override
    public void setJsonMsg(final String jsonMsg) {
        this.jsonMsg = jsonMsg;
    }

    public UserStoreroomDao getUserStoreroomDao() {
        return userStoreroomDao;
    }

    public void setUserStoreroomDao(final UserStoreroomDao userStoreroomDao) {
        this.userStoreroomDao = userStoreroomDao;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(final List<Card> cards) {
        this.cards = cards;
    }

    public int[] getCardIds() {
        return cardIds;
    }

    public void setCardIds(final int[] cardIds) {
        this.cardIds = cardIds;
    }

    public int[] getPartyIds() {
        return partyIds;
    }

    public void setPartyIds(final int[] partyIds) {
        this.partyIds = partyIds;
    }

    public int[] getLevels() {
        return levels;
    }

    public void setLevels(final int[] levels) {
        this.levels = levels;
    }

    public int[] getTiers() {
        return tiers;
    }

    public void setTiers(final int[] tiers) {
        this.tiers = tiers;
    }

}
