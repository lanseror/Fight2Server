package com.fight2.dao;

import org.springframework.stereotype.Repository;

import com.fight2.model.ArenaRewardItem;

@Repository
public class ArenaRewardItemDaoImpl extends BaseDaoImpl<ArenaRewardItem> implements ArenaRewardItemDao {

    public ArenaRewardItemDaoImpl() {
        super(ArenaRewardItem.class);
    }

}
