package com.fight2.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
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
        final String hql = String.format("select max(rankNumber) from %s where arena=:arena", getMyType().getSimpleName());
        final Query query = getSession().createQuery(hql);
        query.setParameter("arena", arena);
        final Number id = (Number) query.uniqueResult();
        if (id != null) {
            return id.intValue();
        } else {
            return 0;
        }
    }

    @Override
    public List<ArenaRanking> listByUser(final User user) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("user", user));
        @SuppressWarnings("unchecked")
        final List<ArenaRanking> list = criteria.list();
        return list;
    }

    @Override
    public List<ArenaRanking> listByArena(final Arena arena) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("arena", arena));
        criteria.addOrder(Order.asc("rankNumber"));
        @SuppressWarnings("unchecked")
        final List<ArenaRanking> list = criteria.list();
        return list;
    }
}
