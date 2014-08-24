package com.fight2.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class BaseDaoImpl<T> implements BaseDao<T> {
    private final Class<T> type;

    protected SessionFactory sessionFactory;

    public Class<T> getMyType() {
        return this.type;
    }

    public BaseDaoImpl(final Class<T> type) {
        this.type = type;
    }

    public void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(final int id) {
        return (T) getSession().get(type, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T load(final int id) {
        return (T) getSession().load(type, id);
    }

    @Override
    public void delete(final T obj) {
        final Session session = getSession();
        session.delete(obj);
        session.flush();
    }

    @Override
    public void add(final T obj) {
        final Session session = getSession();
        session.save(obj);
        session.flush();
    }

    @Override
    public void update(final T obj) {
        final Session session = getSession();
        session.update(obj);
        session.flush();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> list() {
        return getSession().createCriteria(type).list();
    }
}
