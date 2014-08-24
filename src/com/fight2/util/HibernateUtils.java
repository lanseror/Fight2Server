package com.fight2.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class HibernateUtils {

    public static Session openSession(final SessionFactory sessionFactory) {
        final Session session = sessionFactory.openSession();
        final SessionHolder sessionHolder = new SessionHolder(session);
        TransactionSynchronizationManager.bindResource(sessionFactory, sessionHolder);
        return session;
    }

    public static void closeSession(final SessionFactory sessionFactory) {
        final SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
        SessionFactoryUtils.closeSession(sessionHolder.getSession());
    }
}
