package com.fight2.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class UserProperties extends BaseEntity {
    private static final long serialVersionUID = 1335023426567017048L;
    private int stamina;
    private int ticket;
    private int coin;
    private User user;

    public int getStamina() {
        return stamina;
    }

    public void setStamina(final int stamina) {
        this.stamina = stamina;
    }

    public int getTicket() {
        return ticket;
    }

    public void setTicket(final int ticket) {
        this.ticket = ticket;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(final int coin) {
        this.coin = coin;
    }

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}