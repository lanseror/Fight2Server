package com.fight2.dao;

import org.springframework.stereotype.Repository;

import com.fight2.model.GuildPoll;

@Repository
public class GuildPollDaoImpl extends BaseDaoImpl<GuildPoll> implements GuildPollDao {

    public GuildPollDaoImpl() {
        super(GuildPoll.class);
    }

}
