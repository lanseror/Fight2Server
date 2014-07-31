package com.fight2.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fight2.model.PartyInfo;
import com.fight2.model.User;

@Repository
public class PartyInfoDaoImpl extends BaseDaoImpl<PartyInfo> implements PartyInfoDao {

    public PartyInfoDaoImpl() {
        super(PartyInfo.class);
    }

    @Override
    public PartyInfo getByUser(final User user) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("user", user));
        return (PartyInfo) criteria.uniqueResult();
    }

}
