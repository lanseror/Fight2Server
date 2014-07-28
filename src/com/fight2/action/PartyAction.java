package com.fight2.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.CardDao;
import com.fight2.dao.PartyDao;
import com.fight2.model.Card;
import com.fight2.model.Party;
import com.fight2.model.PartyGrid;
import com.fight2.model.User;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

@Namespace("/party")
public class PartyAction extends BaseAction {
    private static final long serialVersionUID = -4473064014262040889L;
    @Autowired
    private PartyDao partyDao;
    @Autowired
    private CardDao cardDao;
    private Party party;
    private List<Party> datas;
    private int id;
    private String jsonMsg;

    @Action(value = "my-parties", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String myParties() {
        final User user = (User) this.getSession().get(LOGIN_USER);
        final List<Party> poParties = partyDao.listByUser(user);
        final List<List<Integer>> voParties = Lists.newArrayList();
        for (final Party poParty : poParties) {
            final List<Integer> cards = Lists.newArrayList();
            for (final PartyGrid partyGrid : poParty.getPartyGrids()) {
                final Card card = partyGrid.getCard();
                if (card != null) {
                    cards.add(card.getId());
                } else {
                    cards.add(-1);
                }

            }
            voParties.add(cards);
        }

        jsonMsg = new Gson().toJson(voParties);
        return SUCCESS;
    }

    @Action(value = "edit", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String edit() {
        final User user = (User) this.getSession().get(LOGIN_USER);
        final List<Party> poParties = partyDao.listByUser(user);
        @SuppressWarnings("unchecked")
        final List<List<Double>> parties = new Gson().fromJson(jsonMsg, List.class);
        for (final Party poParty : poParties) {
            for (final PartyGrid partyGrid : poParty.getPartyGrids()) {
                partyGrid.setCard(null);
            }
            partyDao.update(poParty);
        }
        for (int partyIndex = 0; partyIndex < parties.size(); partyIndex++) {
            final List<Double> party = parties.get(partyIndex);
            final Party poParty = poParties.get(partyIndex);
            final List<PartyGrid> partyGrids = poParty.getPartyGrids();
            for (int cardIndex = 0; cardIndex < party.size(); cardIndex++) {
                final int cardId = party.get(cardIndex).intValue();
                final PartyGrid partyGrid = partyGrids.get(cardIndex);
                if (cardId != -1) {
                    final Card card = cardDao.get(cardId);
                    partyGrid.setCard(card);
                } else {
                    partyGrid.setCard(null);
                }
            }
            partyDao.update(poParty);
        }
        return SUCCESS;
    }

    public PartyDao getPartyDao() {
        return partyDao;
    }

    public void setPartyDao(final PartyDao partyDao) {
        this.partyDao = partyDao;
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

    public String getJsonMsg() {
        return jsonMsg;
    }

    public void setJsonMsg(final String jsonMsg) {
        this.jsonMsg = jsonMsg;
    }

    public CardDao getCardDao() {
        return cardDao;
    }

    public void setCardDao(final CardDao cardDao) {
        this.cardDao = cardDao;
    }

}
