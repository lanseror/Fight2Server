package com.fight2.dao;

import org.springframework.stereotype.Repository;

import com.fight2.model.UserQuestInfo;

@Repository
public class UserQuestInfoDaoImpl extends BaseDaoImpl<UserQuestInfo> implements UserQuestInfoDao {

    public UserQuestInfoDaoImpl() {
        super(UserQuestInfo.class);
    }

}
