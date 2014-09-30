package com.fight2.dao;

import java.util.List;

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

    @Override
    public List<Guild> listTopGuilds(final int maxResults) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.setMaxResults(maxResults);
        @SuppressWarnings("unchecked")
        final List<Guild> guilds = criteria.list();
        return guilds;
    }

}
