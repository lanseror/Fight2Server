package com.fight2.dao;

import org.springframework.stereotype.Repository;

import com.fight2.model.ArenaRanking;

@Repository
public class ArenaRankingDaoImpl extends BaseDaoImpl<ArenaRanking> implements ArenaRankingDao {

    public ArenaRankingDaoImpl() {
        super(ArenaRanking.class);
    }

}
