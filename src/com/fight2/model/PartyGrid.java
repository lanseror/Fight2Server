package com.fight2.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class PartyGrid extends BaseEntity {
    private static final long serialVersionUID = -3340617388225596602L;

    private int gridNumber;

    private Party party;

    private Card card;

    public int getGridNumber() {
        return gridNumber;
    }

    public void setGridNumber(final int gridNumber) {
        this.gridNumber = gridNumber;
    }

    @ManyToOne
    @JoinColumn(name = "party_id")
    public Party getParty() {
        return party;
    }

    public void setParty(final Party party) {
        this.party = party;
    }

    @OneToOne
    @JoinColumn(name = "card_id", unique = true, nullable = true, updatable = false)
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
