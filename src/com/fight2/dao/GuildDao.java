package com.fight2.dao;

import com.fight2.model.Guild;
import com.fight2.model.User;

public interface GuildDao extends BaseDao<Guild> {
    
    public Guild getByPresident(User user);

}
