package com.fight2.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fight2.model.Guild;
import com.fight2.model.User;

@Repository
public class GuildDaoImpl extends BaseDaoImpl<Guild> implements GuildDao {

    public GuildDaoImpl() {
        super(Guild.class);
    }

    @Override
    public Guild getByPresident(final User user) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("president", user));
        return (Guild) criteria.uniqueResult();
    }

}
