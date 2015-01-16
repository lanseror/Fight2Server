package com.fight2.dao;

import java.util.List;

import com.fight2.model.User;
import com.fight2.model.UserQuestTask;

public interface UserQuestTaskDao extends BaseDao<UserQuestTask> {

    public List<UserQuestTask> listByUser(User user);

}
