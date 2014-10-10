package com.fight2.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Bid extends BaseEntity {
    private static final long serialVersionUID = -290539244199511620L;
    private int price;
    private int amount;
    private Guild guild;
    private BidItemType type;
    private GuildCard guildCard;
    private BidStatus status;

    public int getPrice() {
        return price;
    }

    public void setPrice(final int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

    @ManyToOne
    @JoinColumn(name = "guild_id")
    public Guild getGuild() {
        return guild;
    }

    public void setGuild(final Guild guild) {
        this.guild = guild;
    }

    public BidItemType getType() {
        return type;
    }

    public void setType(final BidItemType type) {
        this.type = type;
    }

    @OneToOne
    @JoinColumn(name = "guild_card_id", unique = true)
    public GuildCard getGuildCard() {
        return guildCard;
    }

    public void setGuildCard(final GuildCard guildCard) {
        this.guildCard = guildCard;
    }

    public BidStatus getStatus() {
        return status;
    }

    public void setStatus(final BidStatus status) {
        this.status = status;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public enum BidItemType {
        ArenaTicket,
        Stamina,
        Card
    }

    public enum BidStatus {
        Open,
        closed
    }
}
