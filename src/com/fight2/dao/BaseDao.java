package com.fight2.dao;

import java.util.List;

import org.hibernate.SessionFactory;

public interface BaseDao<T> {
    public T load(int id);

    public T get(int id);

    public void delete(T obj);

    public void add(T obj);

    public void update(T obj);

    public List<T> list();

    public List<T> listByLimit(final int maxResults);

    public SessionFactory getSessionFactory();

}
