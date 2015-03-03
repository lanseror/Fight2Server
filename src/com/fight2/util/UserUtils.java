package com.fight2.util;

import java.util.List;

import org.hibernate.SessionFactory;

import com.fight2.dao.UserDao;
import com.fight2.model.User;
import com.fight2.model.User.UserType;

public class UserUtils {

    public static Runnable createRefreshUserCardPriceSchedule(final UserDao userDao) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final SessionFactory sessionFactory = userDao.getSessionFactory();
                HibernateUtils.openSession(sessionFactory);
                final List<User> users = userDao.listByType(UserType.User);
                for (final User user : users) {
                    final List<Integer> cardPrices = userDao.getUserCardPrices(user.getId());
                    int sumCardPrices = 0;
                    for (final int cardPrice : cardPrices) {
                        sumCardPrices += cardPrice;
                    }
                    user.setCardPrice(sumCardPrices);
                    userDao.update(user);
                }
                HibernateUtils.closeSession(sessionFactory);
            }

        };
        return runnable;
    }

}
