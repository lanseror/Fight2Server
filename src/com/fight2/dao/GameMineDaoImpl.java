package com.fight2.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fight2.model.quest.GameMine;

@Repository
public class GameMineDaoImpl extends BaseDaoImpl<GameMine> implements GameMineDao {

    public GameMineDaoImpl() {
        super(GameMine.class);
    }

    @Override
    public GameMine getByHeroPosition(final int row, final int col) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("heroRow", row));
        criteria.add(Restrictions.eq("heroCol", col));
        return (GameMine) criteria.uniqueResult();
    }

}
