package com.fight2.dao;

import java.util.List;

import com.fight2.model.Guild;
import com.fight2.model.User;

public interface GuildDao extends BaseDao<Guild> {

    public Guild getByPresident(User user);

    public List<Guild> listTopGuilds(final int maxResults);
}
