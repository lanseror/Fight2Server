package com.fight2.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fight2.model.Bid;
import com.fight2.model.Guild;

@Repository
public class BidDaoImpl extends BaseDaoImpl<Bid> implements BidDao {

    public BidDaoImpl() {
        super(Bid.class);
    }

    @Override
    public List<Bid> listGuildOpenBids(final Guild guild) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("guild", guild));
        @SuppressWarnings("unchecked")
        final List<Bid> bids = criteria.list();
        return bids;
    }

}
