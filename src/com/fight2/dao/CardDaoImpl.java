package com.fight2.dao;

import org.springframework.stereotype.Repository;

import com.fight2.model.Card;

@Repository
public class CardDaoImpl extends BaseDaoImpl<Card> implements CardDao {

    public CardDaoImpl() {
        super(Card.class);
    }

}
