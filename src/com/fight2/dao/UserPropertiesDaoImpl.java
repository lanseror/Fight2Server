package com.fight2.dao;

import org.springframework.stereotype.Repository;

import com.fight2.model.UserProperties;

@Repository
public class UserPropertiesDaoImpl extends BaseDaoImpl<UserProperties> implements UserPropertiesDao {

    public UserPropertiesDaoImpl() {
        super(UserProperties.class);
    }

}
