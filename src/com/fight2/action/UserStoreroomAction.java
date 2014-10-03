package com.fight2.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.CardDao;
import com.fight2.dao.CardImageDao;
import com.fight2.dao.UserStoreroomDao;
import com.fight2.model.Card;
import com.fight2.model.Card.CardStatus;
import com.fight2.model.CardImage;
import com.fight2.model.UserStoreroom;

@Namespace("/user-storeroom")
public class UserStoreroomAction extends BaseAction {
    private static final long serialVersionUID = -4473064014262040889L;
    @Autowired
    private UserStoreroomDao userStoreroomDao;
    @Autowired
    private CardDao cardDao;
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
            final CardImage imageObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_THUMB, card.getTier(), card.getCardTemplate());
            card.setImage(imageObj.getUrl());
        }
        userStoreroom.setCards(cards);
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

}
