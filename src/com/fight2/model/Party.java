package com.fight2.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

@Entity
public class Party extends BaseEntity {
    private static final long serialVersionUID = -949260842967108311L;

    private int partyNumber;

    private List<PartyGrid> partyGrids;

    private List<Card> cards;

    private User user;

    public int getPartyNumber() {
        return partyNumber;
    }

    public void setPartyNumber(final int partyNumber) {
        this.partyNumber = partyNumber;
    }

    @OneToMany(mappedBy = "party")
    @OrderBy("gridNumber ASC")
    public List<PartyGrid> getPartyGrids() {
        return partyGrids;
    }

    public void setPartyGrids(final List<PartyGrid> partyGrids) {
        this.partyGrids = partyGrids;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Transient
    public List<Card> getCards() {
        return cards;
    }

    public void setCards(final List<Card> cards) {
        this.cards = cards;
    }

}
