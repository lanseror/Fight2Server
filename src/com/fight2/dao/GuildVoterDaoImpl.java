package com.fight2.dao;

import org.springframework.stereotype.Repository;

import com.fight2.model.GuildVoter;

@Repository
public class GuildVoterDaoImpl extends BaseDaoImpl<GuildVoter> implements GuildVoterDao {

    public GuildVoterDaoImpl() {
        super(GuildVoter.class);
    }

}
