package com.fight2.dao;

import java.util.List;

import com.fight2.model.Guild;
import com.fight2.model.GuildArenaUser;
import com.fight2.model.User;

public interface GuildArenaUserDao extends BaseDao<GuildArenaUser> {

    public List<GuildArenaUser> listByGuild(Guild guild);
    
    public GuildArenaUser getByUser(User user);

}
