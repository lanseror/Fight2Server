package com.fight2.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fight2.model.Party;
import com.fight2.model.User;

@Repository
public class PartyDaoImpl extends BaseDaoImpl<Party> implements PartyDao {

    public PartyDaoImpl() {
        super(Party.class);
    }

    @Override
    public List<Party> listByUser(final User user) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("user", user));
        @SuppressWarnings("unchecked")
        final List<Party> list = criteria.addOrder(Order.asc("partyNumber")).list();
        return list;
    }

}
