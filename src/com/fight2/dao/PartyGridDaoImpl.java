package com.fight2.dao;

import org.springframework.stereotype.Repository;

import com.fight2.model.PartyGrid;

@Repository
public class PartyGridDaoImpl extends BaseDaoImpl<PartyGrid> implements PartyGridDao {

    public PartyGridDaoImpl() {
        super(PartyGrid.class);
    }

}
