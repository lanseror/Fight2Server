package com.fight2.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fight2.model.Card;
import com.fight2.model.Card.CardStatus;
import com.fight2.model.User;

@Repository
public class CardDaoImpl extends BaseDaoImpl<Card> implements CardDao {

    public CardDaoImpl() {
        super(Card.class);
    }

    @Override
    public List<Card> listByUserAndStatus(final User user, final CardStatus status) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("user", user));
        criteria.add(Restrictions.eq("status", status));
        @SuppressWarnings("unchecked")
        final List<Card> list = criteria.list();
        return list;
    }

    @Override
    public List<Card> listByUser(final User user) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("user", user));
        @SuppressWarnings("unchecked")
        final List<Card> list = criteria.list();
        return list;
    }

}
