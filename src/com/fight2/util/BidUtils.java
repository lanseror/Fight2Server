package com.fight2.util;

import org.hibernate.SessionFactory;

import com.fight2.dao.BidDao;
import com.fight2.model.Bid;
import com.fight2.model.Bid.BidStatus;

public class BidUtils {

    public static Runnable createCloseSchedule(final int id, final BidDao bidDao) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final SessionFactory sessionFactory = bidDao.getSessionFactory();
                HibernateUtils.openSession(sessionFactory);
                final Bid bid = bidDao.get(id);
                if (bid != null && bid.getStatus() == BidStatus.Started) {
                    bid.setStatus(BidStatus.Closed);
                    bidDao.update(bid);
                }
                HibernateUtils.closeSession(sessionFactory);
            }

        };
        return runnable;
    }

}
