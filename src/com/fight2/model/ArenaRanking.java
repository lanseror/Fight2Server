package com.fight2.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ArenaRanking extends BaseEntity {
    private static final long serialVersionUID = -7148408013776654638L;
    private User user;
    private Arena arena;
    private int rankNumber;
    private int might;
    private int win;
    private int lose;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "arena_id")
    public Arena getArena() {
        return arena;
    }

    public void setArena(final Arena arena) {
        this.arena = arena;
    }

    public int getRankNumber() {
        return rankNumber;
    }

    public void setRankNumber(final int rankNumber) {
        this.rankNumber = rankNumber;
    }

    public int getMight() {
        return might;
    }

    public void setMight(final int might) {
        this.might = might;
    }

    public int getWin() {
        return win;
    }

    public void setWin(final int win) {
        this.win = win;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(final int lose) {
        this.lose = lose;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
