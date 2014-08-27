package com.fight2.dao;

import java.util.List;

import com.fight2.model.Arena;
import com.fight2.model.ArenaRanking;
import com.fight2.model.User;

public interface ArenaRankingDao extends BaseDao<ArenaRanking> {

    public ArenaRanking getByUserArena(User user, Arena arena);

    public List<ArenaRanking> listByArenaRange(Arena arena, int min, int max);

    public List<ArenaRanking> listByUser(User user);

    public List<ArenaRanking> listByArena(Arena arena);

    public ArenaRanking getByArenaRank(Arena arena, int rank);

    public int getArenaMaxRank(Arena arena);
}
