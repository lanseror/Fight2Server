package com.fight2.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class GuildCard extends BaseEntity {
    private static final long serialVersionUID = -7461239785078576809L;
    private GuildStoreroom guildStoreroom;
    private Card card;

    @ManyToOne
    @JoinColumn(name = "guild_storeroom_id")
    public GuildStoreroom getGuildStoreroom() {
        return guildStoreroom;
    }

    public void setGuildStoreroom(final GuildStoreroom guildStoreroom) {
        this.guildStoreroom = guildStoreroom;
    }

    @OneToOne(optional = false)
    @JoinColumn(name = "card_id", unique = true, nullable = false)
    public Card getCard() {
        return card;
    }

    public void setCard(final Card card) {
        this.card = card;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
