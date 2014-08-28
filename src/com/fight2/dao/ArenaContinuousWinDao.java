package com.fight2.dao;

import com.fight2.model.ArenaContinuousWin;
import com.fight2.model.User;

public interface ArenaContinuousWinDao extends BaseDao<ArenaContinuousWin> {

    public ArenaContinuousWin getByUser(User user);

}
