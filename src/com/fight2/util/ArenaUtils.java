package com.fight2.util;

import java.util.Map;

import com.fight2.model.UserArenaInfo;
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
}
