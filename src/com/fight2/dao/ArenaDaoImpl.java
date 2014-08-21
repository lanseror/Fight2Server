package com.fight2.dao;

import org.springframework.stereotype.Repository;

import com.fight2.model.Arena;

@Repository
public class ArenaDaoImpl extends BaseDaoImpl<Arena> implements ArenaDao {

    public ArenaDaoImpl() {
        super(Arena.class);
    }

}
