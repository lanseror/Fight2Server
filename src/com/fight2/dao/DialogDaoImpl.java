package com.fight2.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fight2.model.Dialog;
import com.fight2.model.Dialog.OrderType;
import com.fight2.model.Dialog.Speaker;

@Repository
public class DialogDaoImpl extends BaseDaoImpl<Dialog> implements DialogDao {

    public DialogDaoImpl() {
        super(Dialog.class);
    }

    @Override
    public List<Dialog> listByType(final OrderType orderType) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("orderType", orderType));
        @SuppressWarnings("unchecked")
        final List<Dialog> list = criteria.list();
        return list;
    }

    @Override
    public List<Dialog> listByTypeAndSpeakers(final OrderType orderType, final Speaker... speakers) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.eq("orderType", orderType));
        criteria.add(Restrictions.in("speaker", speakers));
        @SuppressWarnings("unchecked")
        final List<Dialog> list = criteria.list();
        return list;
    }

}
