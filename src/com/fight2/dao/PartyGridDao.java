package com.fight2.dao;

import java.util.List;

import com.fight2.model.Party;
import com.fight2.model.User;

public interface PartyGridDao extends BaseDao<Party> {

    public List<Party> listByUser(User user);

}
