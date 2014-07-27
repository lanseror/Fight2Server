package com.fight2.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.fight2.model.CardTemplate;

@Repository
public class CardTemplateDaoImpl extends BaseDaoImpl<CardTemplate> implements CardTemplateDao {

    public CardTemplateDaoImpl() {
        super(CardTemplate.class);
    }

    @Override
    public List<CardTemplate> listMostProbabilityCard(final int i) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.addOrder(Order.desc("probability"));
        criteria.setMaxResults(i);
        @SuppressWarnings("unchecked")
        final List<CardTemplate> list = criteria.list();
        return list;
    }

}
