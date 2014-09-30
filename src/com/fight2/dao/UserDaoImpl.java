package com.fight2.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fight2.model.Guild;
import com.fight2.model.User;

@Repository
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

    public UserDaoImpl() {
        super(User.class);
    }

    @Override
    public User getByInstallUUID(final String installUUID) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("installUUID", installUUID));
        return (User) criteria.uniqueResult();
    }

    @Override
    public List<User> getAllNpc() {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("npc", true));
        @SuppressWarnings("unchecked")
        final List<User> users = criteria.list();
        return users;
    }

    @Override
    public List<User> listByGuild(final Guild guild) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("guild", guild));
        @SuppressWarnings("unchecked")
        final List<User> users = criteria.list();
        return users;
    }

}
