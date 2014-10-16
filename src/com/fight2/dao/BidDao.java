package com.fight2.dao;

import java.util.List;

import com.fight2.model.Bid;
import com.fight2.model.Guild;

public interface BidDao extends BaseDao<Bid> {

    public List<Bid> listGuildOpenBids(Guild guild);
    
    public List<Bid> listOpenBids();
}
