package com.fight2.dao;

import org.springframework.stereotype.Repository;

import com.fight2.model.Party;

@Repository
public class PartyDaoImpl extends BaseDaoImpl<Party> implements PartyDao {

    public PartyDaoImpl() {
        super(Party.class);
    }

}
