package com.fight2.dao;

import java.util.List;

import com.fight2.model.Guild;
import com.fight2.model.User;
import com.fight2.model.User.UserType;

public interface UserDao extends BaseDao<User> {

    public User getByInstallUUID(String installUUID);

    public List<User> getAllArenaGuardians();
    
    public List<User> getAllNpc();
    
    public List<User> listByType(UserType type);

    public List<User> listByGuild(Guild guild);

}
