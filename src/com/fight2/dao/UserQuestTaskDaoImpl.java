package com.fight2.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fight2.model.User;
import com.fight2.model.UserQuestTask;
import com.fight2.model.UserQuestTask.UserTaskStatus;

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

    @Override
    public UserQuestTask getUserCurrentTask(final User user) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("user", user));
        final UserTaskStatus[] inProgressStatuses = { UserTaskStatus.Ready, UserTaskStatus.Started, UserTaskStatus.Finished };
        criteria.add(Restrictions.in("status", inProgressStatuses));
        return (UserQuestTask) criteria.uniqueResult();
    }

    @Override
    public UserQuestTask getUserLatestTask(final User user) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("user", user));
        criteria.addOrder(Order.desc("id"));
        @SuppressWarnings("unchecked")
        final List<UserQuestTask> list = criteria.list();
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }

    }
}
