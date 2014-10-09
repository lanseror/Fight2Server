package com.fight2.dao;

import org.springframework.stereotype.Repository;

import com.fight2.model.GuildCard;

@Repository
public class GuildCardDaoImpl extends BaseDaoImpl<GuildCard> implements GuildCardDao {

    public GuildCardDaoImpl() {
        super(GuildCard.class);
    }

}
