package com.fight2.dao;

import com.fight2.model.PartyInfo;
import com.fight2.model.User;

public interface PartyInfoDao extends BaseDao<PartyInfo> {

    public PartyInfo getByUser(User user);

}
