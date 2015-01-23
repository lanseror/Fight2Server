package com.fight2.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fight2.model.QuestTask;

@Repository
public class QuestTaskDaoImpl extends BaseDaoImpl<QuestTask> implements QuestTaskDao {

    public QuestTaskDaoImpl() {
        super(QuestTask.class);
    }

    @Override
    public QuestTask getNextTask(final QuestTask task) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.gt("id", task.getId()));
        criteria.setFetchSize(1);
       
        @SuppressWarnings("unchecked")
        final List<QuestTask> tasks = criteria.list();
        if(tasks.size()>0){
            return tasks.get(0);
        }else{
            return null;
        }
    }

}
