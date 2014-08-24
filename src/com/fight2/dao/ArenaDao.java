package com.fight2.dao;

import java.util.List;

import com.fight2.model.Arena;

public interface ArenaDao extends BaseDao<Arena> {

    public List<Arena> getAliveArenas();

}
