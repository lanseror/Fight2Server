package com.fight2.dao;

import java.util.List;

import com.fight2.model.Guild;
import com.fight2.model.GuildPoll;
import com.fight2.model.User;

public interface GuildPollDao extends BaseDao<GuildPoll> {
    public GuildPoll getByGuildAndCandidate(Guild guild, User candidate);

    public List<GuildPoll> listByGuild(Guild guild);
}
