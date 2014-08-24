package com.fight2.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fight2.model.Arena;
import com.fight2.model.ArenaStatus;

@Repository
public class ArenaDaoImpl extends BaseDaoImpl<Arena> implements ArenaDao {
    private static ArenaStatus[] ALIVE_ARENA_STATUS = { ArenaStatus.Scheduled, ArenaStatus.Started };

    public ArenaDaoImpl() {
        super(Arena.class);
    }

    @Override
    public List<Arena> getAliveArenas() {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.in("status", ALIVE_ARENA_STATUS));
        @SuppressWarnings("unchecked")
        final List<Arena> list = criteria.list();
        return list;
    }
}
