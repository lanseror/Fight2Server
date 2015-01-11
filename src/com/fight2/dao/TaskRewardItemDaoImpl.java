package com.fight2.dao;

import org.springframework.stereotype.Repository;

import com.fight2.model.TaskRewardItem;

@Repository
public class TaskRewardItemDaoImpl extends BaseDaoImpl<TaskRewardItem> implements TaskRewardItemDao {

    public TaskRewardItemDaoImpl() {
        super(TaskRewardItem.class);
    }

}
