package com.fight2.model.quest;

import java.util.List;

public class QuestTreasureData {
    private List<QuestTile> questTiles;
    private long version;

    public List<QuestTile> getQuestTiles() {
        return questTiles;
    }

    public void setQuestTiles(final List<QuestTile> questTiles) {
        this.questTiles = questTiles;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(final long version) {
        this.version = version;
    }

}
