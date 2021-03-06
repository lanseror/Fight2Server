package com.fight2.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.ArenaRankingDao;
import com.fight2.dao.CardDao;
import com.fight2.dao.CardImageDao;
import com.fight2.dao.PartyDao;
import com.fight2.dao.PartyInfoDao;
import com.fight2.dao.UserDao;
import com.fight2.model.Card;
import com.fight2.model.CardImage;
import com.fight2.model.CardTemplate;
import com.fight2.model.ComboSkill;
import com.fight2.model.Party;
import com.fight2.model.PartyGrid;
import com.fight2.model.PartyInfo;
import com.fight2.model.User;
import com.fight2.service.ComboSkillService;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

@Namespace("/party")
public class PartyAction extends BaseAction {
    private static final long serialVersionUID = -4473064014262040889L;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PartyDao partyDao;
    @Autowired
    private PartyInfoDao partyInfoDao;
    @Autowired
    private ArenaRankingDao arenaRankingDao;
    @Autowired
    private CardDao cardDao;
    @Autowired
    private CardImageDao cardImageDao;
    @Autowired
    private ComboSkillService comboSkillService;
    private Party party;
    private List<Party> datas;
    private int id;
    private String jsonMsg;

    @Action(value = "my-parties", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String myParties() {
        final User user = (User) this.getSession().get(LOGIN_USER);
        final PartyInfo poPartyInfo = partyInfoDao.getByUser(user);
        final List<Party> poParties = poPartyInfo.getParties();
        final PartyInfo voPartyInfo = new PartyInfo();
        voPartyInfo.setId(poPartyInfo.getId());
        voPartyInfo.setAtk(poPartyInfo.getAtk());
        voPartyInfo.setHp(poPartyInfo.getHp());
        final List<Party> voParties = Lists.newArrayList();
        for (final Party poParty : poParties) {
            final Party voParty = new Party();
            voParty.setId(poParty.getId());
            voParty.setPartyNumber(poParty.getPartyNumber());
            voParty.setAtk(poParty.getAtk());
            voParty.setHp(poParty.getHp());
            final List<Integer> cards = Lists.newArrayList();
            voParty.setCards(cards);
            final List<Integer> partyTemplateIds = Lists.newArrayList();
            for (final PartyGrid partyGrid : poParty.getPartyGrids()) {
                final Card card = partyGrid.getCard();
                if (card != null) {
                    cards.add(card.getId());
                    partyTemplateIds.add(card.getCardTemplate().getId());
                } else {
                    cards.add(-1);
                }
            }
            final List<ComboSkill> partyComboSkill = comboSkillService.getComboSkills(partyTemplateIds, false);
            if (partyComboSkill.size() > 0) {
                voParty.setComboSkills(partyComboSkill);
            }
            voParties.add(voParty);
        }
        voPartyInfo.setParties(voParties);
        jsonMsg = new Gson().toJson(voPartyInfo);
        return SUCCESS;
    }

    @Action(value = "user-parties", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String userParties() {
        final User user = userDao.get(id);
        final PartyInfo poPartyInfo = partyInfoDao.getByUser(user);
        final List<Party> poParties = poPartyInfo.getParties();
        final PartyInfo voPartyInfo = new PartyInfo();
        voPartyInfo.setId(poPartyInfo.getId());
        voPartyInfo.setAtk(poPartyInfo.getAtk());
        voPartyInfo.setHp(poPartyInfo.getHp());
        final List<Party> voParties = Lists.newArrayList();
        for (final Party poParty : poParties) {
            final Party voParty = new Party();
            voParty.setId(poParty.getId());
            voParty.setPartyNumber(poParty.getPartyNumber());
            voParty.setAtk(poParty.getAtk());
            voParty.setHp(poParty.getHp());
            final List<PartyGrid> voPartyGrids = Lists.newArrayList();
            voParty.setPartyGrids(voPartyGrids);
            final List<Integer> partyTemplateIds = Lists.newArrayList();
            for (final PartyGrid partyGrid : poParty.getPartyGrids()) {
                final PartyGrid voPartyGrid = new PartyGrid();
                voPartyGrid.setId(partyGrid.getId());
                voPartyGrid.setGridNumber(partyGrid.getGridNumber());
                final Card card = partyGrid.getCard();
                if (card != null) {
                    final CardTemplate cardTemplate = card.getCardTemplate();
                    final CardImage avatarObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_AVATAR, card.getTier(), cardTemplate);
                    final CardImage imageObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_THUMB, card.getTier(), cardTemplate);
                    final Card voCard = new Card(card);
                    voCard.setAvatar(avatarObj.getUrl());
                    voCard.setImage(imageObj.getUrl());
                    voPartyGrid.setCard(voCard);
                    partyTemplateIds.add(cardTemplate.getId());
                }
                voPartyGrids.add(voPartyGrid);
            }

            final List<ComboSkill> partyComboSkill = comboSkillService.getComboSkills(partyTemplateIds, false);
            if (partyComboSkill.size() > 0) {
                voParty.setComboSkills(partyComboSkill);
            }
            voParties.add(voParty);
        }
        voPartyInfo.setParties(voParties);
        jsonMsg = new Gson().toJson(voPartyInfo);
        return SUCCESS;
    }

    @Action(value = "edit", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String edit() {
        final User loginUser = (User) this.getSession().get(LOGIN_USER);
        final User user = userDao.get(loginUser.getId());
        final PartyInfo poPartyInfo = partyInfoDao.getByUser(user);
        final List<Party> poParties = poPartyInfo.getParties();
        @SuppressWarnings("unchecked")
        final List<List<Double>> parties = new Gson().fromJson(jsonMsg, List.class);
        if (parties.get(0).get(0).intValue() == -1) {
            throw new RuntimeException("You must have a leader.");
        }

        for (final Party poParty : poParties) {
            for (final PartyGrid partyGrid : poParty.getPartyGrids()) {
                partyGrid.setCard(null);
            }
            partyDao.update(poParty);
        }
        int partyInfoAtk = 0;
        int partyInfoHp = 0;
        String avatar = null;
        for (int partyIndex = 0; partyIndex < parties.size(); partyIndex++) {
            final List<Double> party = parties.get(partyIndex);
            final Party poParty = poParties.get(partyIndex);
            final List<PartyGrid> partyGrids = poParty.getPartyGrids();

            int partyAtk = 0;
            int partyHp = 0;
            for (int cardIndex = 0; cardIndex < party.size(); cardIndex++) {
                final int cardId = party.get(cardIndex).intValue();
                final PartyGrid partyGrid = partyGrids.get(cardIndex);
                if (cardId != -1) {
                    final Card card = cardDao.get(cardId);
                    partyGrid.setCard(card);
                    partyAtk += card.getAtk();
                    partyHp += card.getHp();
                    if (avatar == null) {
                        final CardImage avatarObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_AVATAR, card.getTier(),
                                card.getCardTemplate());
                        avatar = avatarObj.getUrl();
                    }
                } else {
                    partyGrid.setCard(null);
                }
            }
            poParty.setAtk(partyAtk);
            poParty.setHp(partyHp);
            partyDao.update(poParty);
            partyInfoAtk += partyAtk;
            partyInfoHp += partyHp;
        }
        poPartyInfo.setAtk(partyInfoAtk);
        poPartyInfo.setHp(partyInfoHp);
        partyInfoDao.update(poPartyInfo);
        final User userUpdate = userDao.get(user.getId());
        userUpdate.setAvatar(avatar);
        userDao.update(userUpdate);
        return myParties();
    }

    // @Action(value = "fix", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    // public String fixParties() {
    // final List<ArenaRanking> arenaRankings = arenaRankingDao.list();
    // for (final ArenaRanking arenaRanking : arenaRankings) {
    // arenaRankingDao.delete(arenaRanking);
    // }
    //
    // final List<Party> parties = partyDao.list();
    // for (final Party party : parties) {
    // final List<PartyGrid> partyGrids = party.getPartyGrids();
    // int partyAtk = 0;
    // int partyHp = 0;
    // for (final PartyGrid partyGrid : partyGrids) {
    // final Card card = partyGrid.getCard();
    // if (card == null) {
    // continue;
    // }
    // partyAtk += card.getAtk();
    // partyHp += card.getHp();
    // }
    // party.setAtk(partyAtk);
    // party.setHp(partyHp);
    // partyDao.update(party);
    // }
    //
    // jsonMsg = new Gson().toJson("ok");
    // return SUCCESS;
    // }

    public PartyDao getPartyDao() {
        return partyDao;
    }

    public void setPartyDao(final PartyDao partyDao) {
        this.partyDao = partyDao;
    }

    public PartyInfoDao getPartyInfoDao() {
        return partyInfoDao;
    }

    public void setPartyInfoDao(final PartyInfoDao partyInfoDao) {
        this.partyInfoDao = partyInfoDao;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(final Party party) {
        this.party = party;
    }

    public List<Party> getDatas() {
        return datas;
    }

    public void setDatas(final List<Party> datas) {
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

    @Override
    public String getJsonMsg() {
        return jsonMsg;
    }

    @Override
    public void setJsonMsg(final String jsonMsg) {
        this.jsonMsg = jsonMsg;
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

    public CardImageDao getCardImageDao() {
        return cardImageDao;
    }

    public void setCardImageDao(final CardImageDao cardImageDao) {
        this.cardImageDao = cardImageDao;
    }

    public ArenaRankingDao getArenaRankingDao() {
        return arenaRankingDao;
    }

    public void setArenaRankingDao(final ArenaRankingDao arenaRankingDao) {
        this.arenaRankingDao = arenaRankingDao;
    }

    public ComboSkillService getComboSkillService() {
        return comboSkillService;
    }

    public void setComboSkillService(final ComboSkillService comboSkillService) {
        this.comboSkillService = comboSkillService;
    }

}
