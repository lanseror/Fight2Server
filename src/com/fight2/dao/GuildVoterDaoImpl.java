package com.fight2.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fight2.model.Guild;
import com.fight2.model.GuildVoter;
import com.fight2.model.User;

@Repository
public class GuildVoterDaoImpl extends BaseDaoImpl<GuildVoter> implements GuildVoterDao {

    public GuildVoterDaoImpl() {
        super(GuildVoter.class);
    }

    @Override
    public boolean hasVoted(final Guild guild, final User voter) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("guild", guild));
        criteria.add(Restrictions.eq("voter", voter));
        return criteria.uniqueResult() != null;
    }

}
