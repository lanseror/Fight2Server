package com.fight2.dao;

import java.util.List;

import com.fight2.model.Arena;
import com.fight2.model.ArenaReward;
import com.fight2.model.ArenaReward.ArenaRewardType;

public interface ArenaRewardDao extends BaseDao<ArenaReward> {

    public List<ArenaReward> listByArena(Arena arena);

    public List<ArenaReward> listByArenaAndType(Arena arena, ArenaRewardType type);
}
