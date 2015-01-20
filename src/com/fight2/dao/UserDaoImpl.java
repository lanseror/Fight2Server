package com.fight2.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fight2.model.Guild;
import com.fight2.model.User;
import com.fight2.model.User.UserType;

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
    public List<User> getAllArenaGuardians() {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("type", UserType.ArenaGuardian));
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

    @Override
    public List<User> getAllNpc() {
        final Criteria criteria = getSession().createCriteria(getMyType());
        final UserType[] npcTypes = { UserType.QuestNpc, UserType.ArenaGuardian, UserType.Boss };
        criteria.add(Restrictions.in("type", npcTypes));
        @SuppressWarnings("unchecked")
        final List<User> users = criteria.list();
        return users;
    }

    @Override
    public List<User> listByType(final UserType type) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.or(Restrictions.eq("type", type)));
        @SuppressWarnings("unchecked")
        final List<User> users = criteria.list();
        return users;
    }

}
