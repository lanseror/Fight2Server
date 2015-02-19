package com.fight2.dao;

import com.fight2.model.quest.GameMine;

public interface GameMineDao extends BaseDao<GameMine> {

    public GameMine getByPosition(int row, int col);

}
