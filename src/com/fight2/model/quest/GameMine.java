package com.fight2.model.quest;

import javax.persistence.Entity;

import com.fight2.model.BaseEntity;

@Entity
public class GameMine extends BaseEntity {
    private static final long serialVersionUID = -4613964966772845997L;
    private int ownerId;
    private int amount;
    private int row;
    private int col;
    private MineType type;

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(final int ownerId) {
        this.ownerId = ownerId;
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
        Mineral,
        Wood,
        Crystal
    }
}