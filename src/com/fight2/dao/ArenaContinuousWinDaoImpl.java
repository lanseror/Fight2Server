package com.fight2.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fight2.model.ArenaContinuousWin;
import com.fight2.model.User;

@Repository
public class ArenaContinuousWinDaoImpl extends BaseDaoImpl<ArenaContinuousWin> implements ArenaContinuousWinDao {

    public ArenaContinuousWinDaoImpl() {
        super(ArenaContinuousWin.class);
    }

    @Override
    public ArenaContinuousWin getByUser(final User user) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("user", user));
        final ArenaContinuousWin arenaContinuousWin = (ArenaContinuousWin) criteria.uniqueResult();
        return arenaContinuousWin;
    }
}
