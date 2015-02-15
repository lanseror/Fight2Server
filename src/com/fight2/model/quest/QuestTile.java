package com.fight2.model.quest;

import com.fight2.model.BaseEntity;

//@Entity
public class QuestTile extends BaseEntity {
    private static final long serialVersionUID = -4613964966772845997L;
    private int row;
    private int col;
    private TileItem item;

    public QuestTile(final int row, final int col) {
        super();
        this.row = row;
        this.col = col;
    }

    public QuestTile() {

    }

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

    @Override
    public String toString() {
        return row + "," + col;
    }

    @Override
    public int hashCode() {
        return (row + "," + col).hashCode();
    }

    @Override
    public boolean equals(final Object tile) {
        if (this == tile) {
            return true;
        } else if (tile != null && tile instanceof QuestTile) {
            final QuestTile questTile = (QuestTile) tile;
            return this.col == questTile.getCol() && this.row == questTile.getRow();
        } else {
            return false;
        }
    }

    public enum TileItem {
        Ticket,
        Stamina,
        CoinBag,
        Card,
        SummonCharm,
        Diamon
    }
}
