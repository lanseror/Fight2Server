package com.fight2.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.ArenaDao;
import com.fight2.dao.ArenaRewardDao;
import com.fight2.dao.ArenaRewardItemDao;
import com.fight2.dao.CardImageDao;
import com.fight2.dao.CardTemplateDao;
import com.fight2.model.Arena;
import com.fight2.model.ArenaReward;
import com.fight2.model.ArenaRewardItem;
import com.fight2.model.ArenaRewardItem.ArenaRewardItemType;
import com.fight2.model.BaseEntity;
import com.fight2.model.Card;
import com.fight2.model.CardImage;
import com.fight2.model.CardTemplate;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/arena-reward")
public class ArenaRewardAction extends BaseAction {
    private static final long serialVersionUID = -8367061939120159572L;
    @Autowired
    private ArenaRewardDao arenaRewardDao;
    @Autowired
    private ArenaRewardItemDao arenaRewardItemDao;
    @Autowired
    private ArenaDao arenaDao;
    @Autowired
    private CardImageDao cardImageDao;
    @Autowired
    private CardTemplateDao cardTemplateDao;
    private List<Card> cards;
    private List<ArenaReward> datas;
    private ArenaReward arenaReward;
    private Arena arena;
    private int id;
    private int arenaId;
    private ArenaRewardItemType[] rewardItemTypes;
    private int[] rewardItemAmounts;
    private int[] cardIds;

    @Action(value = "add", results = { @Result(name = SUCCESS, location = "arena_reward_form.ftl") })
    public String add() {
        loadCardData();
        return SUCCESS;
    }

    @Action(value = "edit", results = { @Result(name = SUCCESS, location = "arena_reward_form.ftl") })
    public String edit() {
        arenaReward = arenaRewardDao.get(id);
        arenaId = arenaReward.getArena().getId();
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

    @Action(value = "delete",
            results = { @Result(name = SUCCESS, location = "list-by-arena", type = "redirect", params = { "arenaId", "${arenaId}" }) })
    public String delete() {
        arenaReward = arenaRewardDao.get(id);
        arenaId = arenaReward.getArena().getId();
        arenaRewardDao.delete(arenaReward);
        return SUCCESS;
    }

    @Action(value = "save", interceptorRefs = { @InterceptorRef("tokenSession"), @InterceptorRef("defaultStack") }, results = { @Result(
            name = SUCCESS, location = "list-by-arena", type = "redirect", params = { "arenaId", "${arenaId}" }) })
    public String save() {
        if (arenaReward.getId() == BaseEntity.EMPTY_ID) {
            return createSave();
        } else {
            return editSave();
        }
    }

    private String createSave() {
        final Arena arena = arenaDao.load(arenaId);
        arenaReward.setArena(arena);
        arenaRewardDao.add(arenaReward);
        saveRewardItems(arenaReward);
        return SUCCESS;
    }

    private void saveRewardItems(final ArenaReward arenaReward) {
        int cardIdIndex = 0;
        for (int i = 0; i < rewardItemTypes.length; i++) {
            final ArenaRewardItemType rewardItemType = rewardItemTypes[i];
            final int amount = rewardItemAmounts[i];
            final ArenaRewardItem arenaRewardItem = new ArenaRewardItem();
            arenaRewardItem.setArenaReward(arenaReward);
            arenaRewardItem.setAmount(amount);
            arenaRewardItem.setType(rewardItemType);
            if (rewardItemType == ArenaRewardItemType.Card) {
                final int cardId = cardIds[cardIdIndex++];
                final CardTemplate cardTemplate = cardTemplateDao.load(cardId);
                arenaRewardItem.setCardTemplate(cardTemplate);
            }
            arenaRewardItemDao.add(arenaRewardItem);
        }
    }

    private String editSave() {
        final ArenaReward arenaRewardPo = arenaRewardDao.load(arenaReward.getId());
        arenaRewardPo.setMax(arenaReward.getMax());
        arenaRewardPo.setMin(arenaReward.getMin());
        arenaRewardPo.setType(arenaReward.getType());
        arenaRewardDao.update(arenaRewardPo);
        // Delete all items first.
        final List<ArenaRewardItem> rewardItems = arenaRewardPo.getRewardItems();
        for (final ArenaRewardItem rewardItem : rewardItems) {
            arenaRewardItemDao.delete(rewardItem);
        }
        // Then save new items.
        saveRewardItems(arenaReward);
        return SUCCESS;
    }

    @Action(value = "list-by-arena", results = { @Result(name = SUCCESS, location = "arena_reward_list.ftl") })
    public String listByArena() {
        arena = arenaDao.load(arenaId);
        datas = arenaRewardDao.listByArena(arena);
        return SUCCESS;
    }

    @Action(value = "list-json", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String listJson() {
        arena = arenaDao.load(arenaId);
        datas = arenaRewardDao.listByArena(arena);
        final List<ArenaReward> rewards = Lists.newArrayList();
        for (final ArenaReward data : datas) {
            final ArenaReward arenaReward = new ArenaReward();
            arenaReward.setId(data.getId());
            arenaReward.setMax(data.getMax());
            arenaReward.setMin(data.getMin());
            arenaReward.setType(data.getType());
            final List<ArenaRewardItem> rewardItems = Lists.newArrayList();
            for (final ArenaRewardItem rewardItemTemp : data.getRewardItems()) {
                final ArenaRewardItem rewardItem = new ArenaRewardItem();
                rewardItem.setId(rewardItemTemp.getId());
                rewardItem.setAmount(rewardItemTemp.getAmount());
                rewardItem.setType(rewardItemTemp.getType());
                final CardTemplate cardTemplate = rewardItemTemp.getCardTemplate();
                if (cardTemplate != null) {
                    final CardImage thumbObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_THUMB, 1, cardTemplate);
                    final Card card = new Card();
                    card.setAtk(cardTemplate.getAtk());
                    card.setHp(cardTemplate.getHp());
                    card.setName(cardTemplate.getName());
                    card.setStar(cardTemplate.getStar());
                    card.setImage(thumbObj.getUrl());
                    card.setRace(cardTemplate.getRace());
                    final CardTemplate cardTemplateVo = new CardTemplate();
                    cardTemplateVo.setId(cardTemplate.getId());
                    card.setCardTemplate(cardTemplateVo);
                    rewardItem.setCard(card);
                }
                rewardItems.add(rewardItem);
            }
            arenaReward.setRewardItems(rewardItems);
            rewards.add(arenaReward);
        }

        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", new Gson().toJson(rewards));
        return SUCCESS;
    }

    public List<ArenaReward> getDatas() {
        return datas;
    }

    public void setDatas(final List<ArenaReward> datas) {
        this.datas = datas;
    }

    public ArenaReward getArenaReward() {
        return arenaReward;
    }

    public void setArenaReward(final ArenaReward arenaReward) {
        this.arenaReward = arenaReward;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getArenaId() {
        return arenaId;
    }

    public void setArenaId(final int arenaId) {
        this.arenaId = arenaId;
    }

    public ArenaRewardDao getArenaRewardDao() {
        return arenaRewardDao;
    }

    public void setArenaRewardDao(final ArenaRewardDao arenaRewardDao) {
        this.arenaRewardDao = arenaRewardDao;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public ArenaDao getArenaDao() {
        return arenaDao;
    }

    public void setArenaDao(final ArenaDao arenaDao) {
        this.arenaDao = arenaDao;
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(final Arena arena) {
        this.arena = arena;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(final List<Card> cards) {
        this.cards = cards;
    }

    public CardImageDao getCardImageDao() {
        return cardImageDao;
    }

    public void setCardImageDao(final CardImageDao cardImageDao) {
        this.cardImageDao = cardImageDao;
    }

    public CardTemplateDao getCardTemplateDao() {
        return cardTemplateDao;
    }

    public void setCardTemplateDao(final CardTemplateDao cardTemplateDao) {
        this.cardTemplateDao = cardTemplateDao;
    }

    public ArenaRewardItemType[] getRewardItemTypes() {
        return rewardItemTypes;
    }

    public void setRewardItemTypes(final ArenaRewardItemType[] rewardItemTypes) {
        this.rewardItemTypes = rewardItemTypes;
    }

    public int[] getRewardItemAmounts() {
        return rewardItemAmounts;
    }

    public void setRewardItemAmounts(final int[] rewardItemAmounts) {
        this.rewardItemAmounts = rewardItemAmounts;
    }

    public int[] getCardIds() {
        return cardIds;
    }

    public void setCardIds(final int[] cardIds) {
        this.cardIds = cardIds;
    }

    public ArenaRewardItemDao getArenaRewardItemDao() {
        return arenaRewardItemDao;
    }

    public void setArenaRewardItemDao(final ArenaRewardItemDao arenaRewardItemDao) {
        this.arenaRewardItemDao = arenaRewardItemDao;
    }

}
