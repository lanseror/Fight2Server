package com.fight2.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
public class GuildStoreroom extends BaseEntity {
    private static final long serialVersionUID = 5035605098835378664L;
    private int stamina;
    private int ticket;
    private int coin;
    private Guild guild;
    private List<GuildCard> guildCards;
    private List<Card> cards;

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

    @Transient
    public List<Card> getCards() {
        return cards;
    }

    public void setCards(final List<Card> cards) {
        this.cards = cards;
    }

    @OneToOne(optional = false)
    @JoinColumn(name = "guild_id", unique = true, nullable = false)
    public Guild getGuild() {
        return guild;
    }

    public void setGuild(final Guild guild) {
        this.guild = guild;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @OneToMany(mappedBy = "guildStoreroom")
    public List<GuildCard> getGuildCards() {
        return guildCards;
    }

    public void setGuildCards(final List<GuildCard> guildCards) {
        this.guildCards = guildCards;
    }

}
