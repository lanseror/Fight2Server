package com.fight2.dao;

import org.springframework.stereotype.Repository;

import com.fight2.model.QuestTask;

@Repository
public class QuestTaskDaoImpl extends BaseDaoImpl<QuestTask> implements QuestTaskDao {

    public QuestTaskDaoImpl() {
        super(QuestTask.class);
    }

}
