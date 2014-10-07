package com.fight2.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fight2.model.Guild;
import com.fight2.model.GuildArenaUser;
import com.fight2.model.User;

@Repository
public class GuildArenaUserDaoImpl extends BaseDaoImpl<GuildArenaUser> implements GuildArenaUserDao {

    public GuildArenaUserDaoImpl() {
        super(GuildArenaUser.class);
    }

    @Override
    public List<GuildArenaUser> listByGuild(final Guild guild) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("guild", guild));
        @SuppressWarnings("unchecked")
        final List<GuildArenaUser> arenaUsers = criteria.list();
        return arenaUsers;
    }

    @Override
    public GuildArenaUser getByUser(final User user) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("user", user));
        return (GuildArenaUser) criteria.uniqueResult();
    }

}
