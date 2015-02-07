package com.fight2.model.quest;

import com.fight2.model.BaseEntity;

//@Entity
public class QuestTile extends BaseEntity {
    private static final long serialVersionUID = -4613964966772845997L;
    private int row;
    private int col;
    private TileItem item;

    public int getRow() {
        return row;
    }

    public void setRow(final int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(final int col) {
        this.col = col;
    }

    public TileItem getItem() {
        return item;
    }

    public void setItem(final TileItem item) {
        this.item = item;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public enum TileItem {
        Ticket,
        Stamina,
        CoinBag,
        Card
    }
}
