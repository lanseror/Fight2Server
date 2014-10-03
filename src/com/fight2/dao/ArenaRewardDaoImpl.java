package com.fight2.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fight2.model.Arena;
import com.fight2.model.ArenaReward;
import com.fight2.model.ArenaReward.ArenaRewardType;

@Repository
public class ArenaRewardDaoImpl extends BaseDaoImpl<ArenaReward> implements ArenaRewardDao {

    public ArenaRewardDaoImpl() {
        super(ArenaReward.class);
    }

    @Override
    public List<ArenaReward> listByArena(final Arena arena) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("arena", arena));
        @SuppressWarnings("unchecked")
        final List<ArenaReward> list = criteria.list();
        return list;
    }

    @Override
    public List<ArenaReward> listByArenaAndType(final Arena arena, final ArenaRewardType type) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("arena", arena));
        criteria.add(Restrictions.eq("type", type));
        @SuppressWarnings("unchecked")
        final List<ArenaReward> list = criteria.list();
        return list;
    }

}
