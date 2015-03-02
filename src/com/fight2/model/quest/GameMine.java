package com.fight2.model.quest;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fight2.model.BaseEntity;
import com.fight2.model.User;

@Entity
public class GameMine extends BaseEntity {
    public static final int MAX_AMOUNT = 50;
    private static final long serialVersionUID = -4613964966772845997L;
    private User owner;
    private int amount;
    private int row;
    private int col;
    private int heroRow;
    private int heroCol;
    private MineType type;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    public User getOwner() {
        return owner;
    }

    public void setOwner(final User owner) {
        this.owner = owner;
    }

    public static int getMaxAmount() {
        return MAX_AMOUNT;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
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

    public int getHeroRow() {
        return heroRow;
    }

    public void setHeroRow(final int heroRow) {
        this.heroRow = heroRow;
    }

    public int getHeroCol() {
        return heroCol;
    }

    public void setHeroCol(final int heroCol) {
        this.heroCol = heroCol;
    }

    public MineType getType() {
        return type;
    }

    public void setType(final MineType type) {
        this.type = type;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public enum MineType {
        Wood(2, 0),
        Mineral(1, 0),
        Crystal(1, 0),
        Diamon(1, 0);

        private final int xOffset;
        private final int yOffset;

        private MineType(final int xOffset, final int yOffset) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
        }

        public int getxOffset() {
            return xOffset;
        }

        public int getyOffset() {
            return yOffset;
        }
    }
}
