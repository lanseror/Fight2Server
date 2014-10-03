package com.fight2.dao;

import java.util.List;

import com.fight2.model.Card;
import com.fight2.model.Card.CardStatus;
import com.fight2.model.User;

public interface CardDao extends BaseDao<Card> {

    public List<Card> listByUserAndStatus(User user, CardStatus status);

    public List<Card> listByUser(User user);
}
