package com.fight2.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
public class Bid extends BaseEntity {
    private static final long serialVersionUID = -290539244199511620L;
    private int price;
    private int amount;
    private Guild guild;
    private User user;
    private BidItemType type;
    private GuildCard guildCard;
    private Card card;
    private BidStatus status;
    private int version = 1;
    private boolean isMyBid = false;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public BidItemType getType() {
        return type;
    }

    public void setType(final BidItemType type) {
        this.type = type;
    }

    @Transient
    public Card getCard() {
        return card;
    }

    public void setCard(final Card card) {
        this.card = card;
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

    public int getVersion() {
        return version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    @Transient
    public boolean isMyBid() {
        return isMyBid;
    }

    public void setMyBid(final boolean isMyBid) {
        this.isMyBid = isMyBid;
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
