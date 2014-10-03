package com.fight2.util;

import java.util.Map;

import org.hibernate.SessionFactory;

import com.fight2.dao.ArenaDao;
import com.fight2.model.Arena;
import com.fight2.model.ArenaStatus;
import com.fight2.model.UserArenaInfo;
import com.fight2.service.ArenaService;
import com.google.common.collect.Maps;

public class ArenaUtils {
    private static final Map<Integer, Integer> ENTERED_ARENA = Maps.newHashMap();
    private static final Map<Integer, Map<Integer, UserArenaInfo>> ARENA_USERINFO = Maps.newHashMap();

    public static void enter(final int arenaId, final int userId) {
        ENTERED_ARENA.put(userId, arenaId);
    }

    public static int getEnteredArena(final int userId) {
        return ENTERED_ARENA.get(userId);
    }

    public static UserArenaInfo getUserArenaInfo(final int arenaId, final int userId) {
        if (!ARENA_USERINFO.containsKey(arenaId)) {
            final Map<Integer, UserArenaInfo> userArenaInfoMap = Maps.newHashMap();
            ARENA_USERINFO.put(arenaId, userArenaInfoMap);
        }
        final Map<Integer, UserArenaInfo> userArenaInfoMap = ARENA_USERINFO.get(arenaId);
        if (!userArenaInfoMap.containsKey(userId)) {
            final UserArenaInfo userArenaInfo = new UserArenaInfo();
            userArenaInfoMap.put(userId, userArenaInfo);
        }

        return userArenaInfoMap.get(userId);
    }

    public static Runnable createStartSchedule(final int id, final ArenaDao arenaDao) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final SessionFactory sessionFactory = arenaDao.getSessionFactory();
                HibernateUtils.openSession(sessionFactory);
                final Arena arena = arenaDao.get(id);
                if (arena != null && arena.getStatus() == ArenaStatus.Scheduled) {
                    arena.setStatus(ArenaStatus.Started);
                    arenaDao.update(arena);
                }
                HibernateUtils.closeSession(sessionFactory);
            }

        };
        return runnable;
    }

    public static Runnable createStopSchedule(final int id, final ArenaService arenaService) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                arenaService.stopArena(id);
            }

        };
        return runnable;
    }
}
