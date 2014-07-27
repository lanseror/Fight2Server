package com.fight2.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.CardDao;
import com.fight2.model.Card;
import com.fight2.model.CardTemplate;
import com.fight2.model.User;
import com.fight2.util.SummonHelper;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/card")
public class CardAction extends BaseAction {
    private static final long serialVersionUID = -4473064014262040889L;
    @Autowired
    private CardDao cardDao;
    @Autowired
    private SummonHelper summonHelper;
    private Card card;
    private List<Card> datas;
    private int id;

    @Action(value = "summon", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String summon() {
        final CardTemplate cardTemplate = summonHelper.summon();
        final String avatar = cardTemplate.getAvatars().get(0).getUrl();
        final String image = cardTemplate.getThumbImages().get(0).getUrl();
        final User user = (User) this.getSession().get(LOGIN_USER);
        final Card card = new Card();
        card.setAtk(cardTemplate.getAtk());
        card.setAvatar(avatar);
        card.setHp(cardTemplate.getHp());
        card.setImage(image);
        card.setName(cardTemplate.getName());
        card.setSkill(cardTemplate.getSkill());

        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", new Gson().toJson(card));

        card.setCardTemplate(cardTemplate);
        card.setUser(user);
        cardDao.add(card);
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

}
