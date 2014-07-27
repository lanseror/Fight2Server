package com.fight2.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.PartyDao;
import com.fight2.model.Card;
import com.fight2.model.Party;
import com.fight2.model.PartyGrid;
import com.fight2.model.User;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/party")
public class PartyAction extends BaseAction {
    private static final long serialVersionUID = -4473064014262040889L;
    @Autowired
    private PartyDao partyDao;
    private Party party;
    private List<Party> datas;
    private int id;

    @Action(value = "my-parties", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String myParties() {
        final User user = (User) this.getSession().get(LOGIN_USER);
        final List<Party> poParties = partyDao.listByUser(user);
        final List<List<Card>> voParties = Lists.newArrayList();
        for (final Party poParty : poParties) {
            final List<Card> cards = Lists.newArrayList();
            for (final PartyGrid partyGrid : poParty.getPartyGrids()) {
                cards.add(partyGrid.getCard());
            }
            voParties.add(cards);
        }

        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", new Gson().toJson(voParties));
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

}