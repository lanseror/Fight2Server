package com.fight2.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fight2.model.QuestTask;
import com.fight2.model.TaskReward;

@Repository
public class TaskRewardDaoImpl extends BaseDaoImpl<TaskReward> implements TaskRewardDao {

    public TaskRewardDaoImpl() {
        super(TaskReward.class);
    }

    @Override
    public List<TaskReward> listByTask(final QuestTask task) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("task", task));
        @SuppressWarnings("unchecked")
        final List<TaskReward> list = criteria.list();
        return list;
    }

}
