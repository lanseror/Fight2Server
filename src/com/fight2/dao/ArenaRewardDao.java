package com.fight2.dao;

import java.util.List;

import com.fight2.model.Arena;
import com.fight2.model.ArenaReward;

public interface ArenaRewardDao extends BaseDao<ArenaReward> {
    public List<ArenaReward> listByArena(Arena arena);
}
