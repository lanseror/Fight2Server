package com.fight2.util;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import tiled.core.Tile;
import tiled.core.TileLayer;

import com.fight2.model.quest.QuestTile;
import com.fight2.model.quest.QuestTile.TileItem;
import com.fight2.model.quest.QuestTreasureData;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class QuestUtils {
    private static final List<QuestTile> ROAD_TILES = Lists.newArrayList();
    private static final Map<Integer, QuestTreasureData> USER_QUEST_DATA = Maps.newHashMap();
    private static final Random RANDOM = new Random();

    public static void init() {
        final tiled.core.Map map = TmxUtils.getMap();
        final TileLayer tmxLayer = (TileLayer) map.getLayers().get(0);
        for (int row = 0; row < tmxLayer.getHeight(); row++) {
            for (int col = 0; col < tmxLayer.getWidth(); col++) {
                final Tile tile = tmxLayer.getTile(row, col);
                if (tile == null) {
                    final QuestTile questTile = new QuestTile();
                    questTile.setRow(row);
                    questTile.setCol(col);
                    ROAD_TILES.add(questTile);
                }
            }
        }
    }

    public static synchronized void refreshUserDatas() {
        for (final QuestTreasureData questTreasureData : USER_QUEST_DATA.values()) {
            randomUserData(questTreasureData);
        }
    }

    public static QuestTreasureData getUserData(final int id) {
        QuestTreasureData questTreasureData = USER_QUEST_DATA.get(id);
        if (questTreasureData == null) {
            questTreasureData = new QuestTreasureData();
            final List<QuestTile> questTiles = Lists.newArrayList();
            questTreasureData.setQuestTiles(questTiles);
            randomUserData(questTreasureData);
            USER_QUEST_DATA.put(id, questTreasureData);
        }

        return questTreasureData;
    }

    private static synchronized void randomUserData(final QuestTreasureData questTreasureData) {
        final List<QuestTile> questTiles = questTreasureData.getQuestTiles();
        questTiles.clear();
        final Set<Integer> randomNumbers = Sets.newHashSet();
        while (randomNumbers.size() < 25) {
            final int randomNumber = RANDOM.nextInt(ROAD_TILES.size());
            randomNumbers.add(randomNumber);
        }
        for (final int randomNumber : randomNumbers) {
            final QuestTile roadTile = ROAD_TILES.get(randomNumber);
            final QuestTile treasureTile = new QuestTile();
            treasureTile.setCol(roadTile.getCol());
            treasureTile.setRow(roadTile.getRow());
            final TileItem[] tileItems = TileItem.values();
            final int randomItem = RANDOM.nextInt(tileItems.length);
            treasureTile.setItem(tileItems[randomItem]);
            questTiles.add(treasureTile);
        }
        questTreasureData.setVersion(System.currentTimeMillis());
    }

}
