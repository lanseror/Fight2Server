package com.fight2.dao;

import java.util.List;

import com.fight2.model.Guild;
import com.fight2.model.User;

public interface UserDao extends BaseDao<User> {

    public User getByInstallUUID(String installUUID);

    public List<User> getAllNpc();

    public List<User> listByGuild(Guild guild);

}
