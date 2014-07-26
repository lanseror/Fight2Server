package com.fight2.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class User extends BaseEntity {
    private static final long serialVersionUID = -4914598284011248917L;
    private String installUUID;
    private String name;
    private String username;
    private String password;
    private String avatar;
    private int cardCount;
    private int level;
    private List<Card> cards;
    private List<Party> parties;

    public String getInstallUUID() {
        return installUUID;
    }

    public void setInstallUUID(final String installUUID) {
        this.installUUID = installUUID;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(final String avatar) {
        this.avatar = avatar;
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(final int cardCount) {
        this.cardCount = cardCount;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(final int level) {
        this.level = level;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @OneToMany(mappedBy = "user")
    public List<Card> getCards() {
        return cards;
    }

    public void setCards(final List<Card> cards) {
        this.cards = cards;
    }

    @OneToMany(mappedBy = "user")
    @OrderBy("partyNumber ASC")
    public List<Party> getParties() {
        return parties;
    }

    public void setParties(final List<Party> parties) {
        this.parties = parties;
    }

}
