package com.fight2.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fight2.model.ChatMessage;

@Repository
public class ChatMessageDaoImpl extends BaseDaoImpl<ChatMessage> implements ChatMessageDao {

    public ChatMessageDaoImpl() {
        super(ChatMessage.class);
    }

    @Override
    public List<ChatMessage> listFromId(final int id, final int maxResults) {
        final Criteria criteria = getSession().createCriteria(getMyType());
        criteria.add(Restrictions.gt("id", id));
        criteria.setMaxResults(maxResults);
        @SuppressWarnings("unchecked")
        final List<ChatMessage> list = criteria.list();
        return list;
    }

    @Override
    public int getMaxId() {
        final String hql = String.format("select max(id) from %s", getMyType().getSimpleName());
        final Query query = getSession().createQuery(hql);
        final Number id = (Number) query.uniqueResult();
        if (id != null) {
            return id.intValue();
        } else {
            return -1;
        }

    }

}
