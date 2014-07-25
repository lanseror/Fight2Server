package com.fight2.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.CardDao;
import com.fight2.model.BaseEntity;
import com.fight2.model.Card;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@Namespace("/card")
public class CardAction extends ActionSupport {
    private static final long serialVersionUID = -4473064014262040889L;
    @Autowired
    private CardDao cardDao;
    private Card card;
    private List<Card> datas;
    private int id;

    @Action(value = "list", results = { @Result(name = SUCCESS, location = "card_list.ftl") })
    public String list() {
        datas = cardDao.list();
        return SUCCESS;
    }

    @Action(value = "list-json", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String listJson() {
        final ActionContext context = ActionContext.getContext();
        final List<Card> list = cardDao.list();
        context.put("jsonMsg", new Gson().toJson(list));
        return SUCCESS;
    }

    @Action(value = "add", results = { @Result(name = SUCCESS, location = "card_form.ftl") })
    public String add() {
        return SUCCESS;
    }

    @Action(value = "edit", results = { @Result(name = SUCCESS, location = "card_form.ftl") })
    public String edit() {
        card = cardDao.get(id);
        return SUCCESS;
    }

    @Action(value = "save", interceptorRefs = { @InterceptorRef("tokenSession"), @InterceptorRef("defaultStack") }, results = { @Result(
            name = SUCCESS, location = "list", type = "redirect") })
    public String save() {
        if (card.getId() == BaseEntity.EMPTY_ID) {
            return createSave();
        } else {
            return editSave();
        }
    }

    private String createSave() {
        cardDao.add(card);
        return SUCCESS;
    }

    private String editSave() {
        cardDao.update(card);
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

}
