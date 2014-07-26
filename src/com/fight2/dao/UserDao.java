package com.fight2.dao;

import com.fight2.model.User;



public interface UserDao  extends BaseDao<User> {
    
    public User getByInstallUUID(String installUUID);

}
