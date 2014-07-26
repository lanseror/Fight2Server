package com.fight2.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class Party extends BaseEntity {
    private static final long serialVersionUID = -949260842967108311L;

    private int partyNumber;

    private List<Card> partyCards;

    private User user;

    public int getPartyNumber() {
        return partyNumber;
    }

    public void setPartyNumber(final int partyNumber) {
        this.partyNumber = partyNumber;
    }

    @Transient
    public List<Card> getPartyCards() {
        return partyCards;
    }

    public void setPartyCards(final List<Card> partyCards) {
        this.partyCards = partyCards;
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

}
