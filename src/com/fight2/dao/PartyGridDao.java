package com.fight2.dao;

import com.fight2.model.Card;
import com.fight2.model.PartyGrid;

public interface PartyGridDao extends BaseDao<PartyGrid> {

    public boolean isCardInParty(Card card);

}
