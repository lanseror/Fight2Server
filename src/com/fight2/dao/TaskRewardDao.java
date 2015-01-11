package com.fight2.dao;

import java.util.List;

import com.fight2.model.QuestTask;
import com.fight2.model.TaskReward;

public interface TaskRewardDao extends BaseDao<TaskReward> {

    public List<TaskReward> listByTask(QuestTask task);

}
