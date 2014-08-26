package com.fight2.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fight2.model.Arena;
import com.fight2.model.ArenaRanking;
import com.fight2.model.User;

@Repository
public class ArenaRankingDaoImpl extends BaseDaoImpl<ArenaRanking> implements ArenaRankingDao {

    public ArenaRankingDaoImpl() {
        super(ArenaRanking.class);
    }

    @Override
    public ArenaRanking getByUserArena(final User user, final Arena arena) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("user", user));
        criteria.add(Restrictions.eq("arena", arena));
        final ArenaRanking arenaRanking = (ArenaRanking) criteria.uniqueResult();
        return arenaRanking;
    }

    @Override
    public List<ArenaRanking> listByArenaRange(final Arena arena, final int min, final int max) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("arena", arena));
        criteria.add(Restrictions.ge("rankNumber", min));
        criteria.add(Restrictions.le("rankNumber", max));
        criteria.addOrder(Order.asc("rankNumber"));
        @SuppressWarnings("unchecked")
        final List<ArenaRanking> list = criteria.list();
        return list;
    }

    @Override
    public ArenaRanking getByArenaRank(final Arena arena, final int rank) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("arena", arena));
        criteria.add(Restrictions.eq("rankNumber", rank));
        final ArenaRanking arenaRanking = (ArenaRanking) criteria.uniqueResult();
        return arenaRanking;
    }

    @Override
    public int getArenaMaxRank(final Arena arena) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        final Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
        if (count != null) {
            return count.intValue();
        } else {
            return 0;
        }
    }
}
