package com.fight2.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fight2.model.Card;
import com.fight2.model.PartyGrid;

@Repository
public class PartyGridDaoImpl extends BaseDaoImpl<PartyGrid> implements PartyGridDao {

    public PartyGridDaoImpl() {
        super(PartyGrid.class);
    }

    @Override
    public boolean isCardInParty(final Card card) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("card", card));
        return criteria.uniqueResult() != null;
    }
}
