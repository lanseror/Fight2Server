package com.fight2.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fight2.model.User;
import com.fight2.model.UserQuestTask;

@Repository
public class UserQuestTaskDaoImpl extends BaseDaoImpl<UserQuestTask> implements UserQuestTaskDao {

    public UserQuestTaskDaoImpl() {
        super(UserQuestTask.class);
    }

    @Override
    public List<UserQuestTask> listByUser(final User user) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("user", user));
        @SuppressWarnings("unchecked")
        final List<UserQuestTask> list = criteria.list();
        return list;
    }

}
