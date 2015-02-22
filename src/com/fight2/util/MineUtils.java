package com.fight2.util;

import java.util.List;

import org.hibernate.SessionFactory;

import com.fight2.dao.GameMineDao;
import com.fight2.model.quest.GameMine;

public class MineUtils {

    public static Runnable createPruduceMineSchedule(final GameMineDao gameMineDao) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final SessionFactory sessionFactory = gameMineDao.getSessionFactory();
                HibernateUtils.openSession(sessionFactory);
                final List<GameMine> mines = gameMineDao.list();
                for (final GameMine mine : mines) {
                    if (mine.getAmount() < GameMine.MAX_AMOUNT) {
                        mine.setAmount(mine.getAmount() + 1);
                        gameMineDao.update(mine);
                    }
                }
                HibernateUtils.closeSession(sessionFactory);
            }

        };
        return runnable;
    }

}
