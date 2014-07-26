package com.fight2.dao;

import org.springframework.stereotype.Repository;

import com.fight2.model.User;

@Repository
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

    public UserDaoImpl() {
        super(User.class);
    }

}
