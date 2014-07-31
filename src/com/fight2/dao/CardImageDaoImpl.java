package com.fight2.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fight2.model.CardImage;
import com.fight2.model.CardTemplate;

@Repository
public class CardImageDaoImpl extends BaseDaoImpl<CardImage> implements CardImageDao {

    public CardImageDaoImpl() {
        super(CardImage.class);
    }

    @Override
    public List<CardImage> listByTypeAndCardTemplate(final String type, final CardTemplate cardTemplate) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("type", type));
        criteria.add(Restrictions.eq("cardTemplate", cardTemplate));
        @SuppressWarnings("unchecked")
        final List<CardImage> list = criteria.addOrder(Order.asc("tier")).list();
        return list;
    }

    @Override
    public CardImage getByTypeTierAndCardTemplate(final String type, final int tier, final CardTemplate cardTemplate) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("type", type));
        criteria.add(Restrictions.eq("tier", tier));
        criteria.add(Restrictions.eq("cardTemplate", cardTemplate));
        return (CardImage) criteria.uniqueResult();
    }

    @Override
    public List<CardImage> listByCardTemplate(final CardTemplate cardTemplate) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("cardTemplate", cardTemplate));
        @SuppressWarnings("unchecked")
        final List<CardImage> list = criteria.addOrder(Order.asc("tier")).list();
        return list;
    }

}
