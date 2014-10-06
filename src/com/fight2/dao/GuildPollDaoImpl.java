package com.fight2.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fight2.model.Guild;
import com.fight2.model.GuildPoll;
import com.fight2.model.User;

@Repository
public class GuildPollDaoImpl extends BaseDaoImpl<GuildPoll> implements GuildPollDao {

    public GuildPollDaoImpl() {
        super(GuildPoll.class);
    }

    @Override
    public GuildPoll getByGuildAndCandidate(final Guild guild, final User candidate) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("guild", guild));
        criteria.add(Restrictions.eq("candidate", candidate));
        return (GuildPoll) criteria.uniqueResult();
    }

    @Override
    public List<GuildPoll> listByGuild(final Guild guild) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("guild", guild));
        @SuppressWarnings("unchecked")
        final List<GuildPoll> guildPolls = criteria.list();
        return guildPolls;
    }

}
