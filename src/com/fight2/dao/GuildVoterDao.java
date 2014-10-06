package com.fight2.dao;

import com.fight2.model.Guild;
import com.fight2.model.GuildVoter;
import com.fight2.model.User;

public interface GuildVoterDao extends BaseDao<GuildVoter> {
    public boolean hasVoted(Guild guild, User voter);

}
