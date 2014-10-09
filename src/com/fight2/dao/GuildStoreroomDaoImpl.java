package com.fight2.dao;

import org.springframework.stereotype.Repository;

import com.fight2.model.GuildStoreroom;

@Repository
public class GuildStoreroomDaoImpl extends BaseDaoImpl<GuildStoreroom> implements GuildStoreroomDao {

    public GuildStoreroomDaoImpl() {
        super(GuildStoreroom.class);
    }

}
