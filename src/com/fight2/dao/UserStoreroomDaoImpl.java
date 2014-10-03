package com.fight2.dao;

import org.springframework.stereotype.Repository;

import com.fight2.model.UserStoreroom;

@Repository
public class UserStoreroomDaoImpl extends BaseDaoImpl<UserStoreroom> implements UserStoreroomDao {

    public UserStoreroomDaoImpl() {
        super(UserStoreroom.class);
    }

}
