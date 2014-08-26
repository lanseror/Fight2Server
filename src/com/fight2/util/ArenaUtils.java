package com.fight2.util;

import java.util.Map;
import java.util.WeakHashMap;

import javax.servlet.http.HttpSession;

import com.fight2.model.UserArenaInfo;
import com.google.common.collect.Maps;

public class ArenaUtils {
    private static final Map<Integer, Map<HttpSession, Integer>> ONLINE_USERS = Maps.newHashMap();
    private static final Map<Integer, Integer> ENTERED_ARENA = Maps.newHashMap();
    private static final Map<Integer, Map<Integer, UserArenaInfo>> ARENA_USERINFO = Maps.newHashMap();

    public static void enter(final int arenaId, final HttpSession httpSession, final int userId) {
        if (!ONLINE_USERS.containsKey(arenaId)) {
            final Map<HttpSession, Integer> sessionMap = new WeakHashMap<HttpSession, Integer>();
            ONLINE_USERS.put(arenaId, sessionMap);
        }

        final Map<HttpSession, Integer> sessionMap = ONLINE_USERS.get(arenaId);
        sessionMap.put(httpSession, userId);
        ENTERED_ARENA.put(userId, arenaId);
    }

    public static void exit(final int arenaId, final HttpSession httpSession) {
        if (ONLINE_USERS.containsKey(arenaId)) {
            final Map<HttpSession, Integer> sessionMap = ONLINE_USERS.get(arenaId);
            sessionMap.remove(httpSession);
        }
    }

    public static int getOnlineNumber(final int arenaId) {
        if (ONLINE_USERS.containsKey(arenaId)) {
            final Map<HttpSession, Integer> sessionMap = ONLINE_USERS.get(arenaId);
            return sessionMap.size();
        } else {
            return 0;
        }
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
}
