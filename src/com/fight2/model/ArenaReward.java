package com.fight2.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ArenaReward extends BaseEntity {
    private static final long serialVersionUID = -6584879750459892301L;
    private Arena arena;
    private ArenaRewardType type;
    private int min;
    private int max;

    @ManyToOne
    @JoinColumn(name = "arena_id")
    public Arena getArena() {
        return arena;
    }

    public void setArena(final Arena arena) {
        this.arena = arena;
    }

    public ArenaRewardType getType() {
        return type;
    }

    public void setType(final ArenaRewardType type) {
        this.type = type;
    }

    public int getMin() {
        return min;
    }

    public void setMin(final int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(final int max) {
        this.max = max;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public enum ArenaRewardType {
        Might,
        Ranking
    }
}
