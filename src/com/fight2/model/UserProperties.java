package com.fight2.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class UserProperties extends BaseEntity {
    public static final int MAX_STAMINA = 100;
    private static final long serialVersionUID = 1335023426567017048L;
    private int stamina;
    private long staminaTime = 0;
    private int ticket;
    private int coin;
    private int guildContrib;
    private User user;

    public int getStamina() {
        return stamina;
    }

    public void setStamina(final int stamina) {
        this.stamina = stamina;
    }

    public long getStaminaTime() {
        return staminaTime;
    }

    public void setStaminaTime(final long staminaTime) {
        this.staminaTime = staminaTime;
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

    public int getGuildContrib() {
        return guildContrib;
    }

    public void setGuildContrib(final int guildContrib) {
        this.guildContrib = guildContrib;
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
