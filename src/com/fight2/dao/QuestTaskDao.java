package com.fight2.dao;

import com.fight2.model.QuestTask;

public interface QuestTaskDao extends BaseDao<QuestTask> {
    
    public QuestTask getNextTask(QuestTask task);

}
