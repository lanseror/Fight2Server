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

    private int hp;

    private int atk;

    private List<PartyGrid> partyGrids;

    private List<Integer> cards;

    private PartyInfo partyInfo;

    public int getPartyNumber() {
        return partyNumber;
    }

    public void setPartyNumber(final int partyNumber) {
        this.partyNumber = partyNumber;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(final int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(final int atk) {
        this.atk = atk;
    }

    @OneToMany(mappedBy = "party")
    @OrderBy("gridNumber ASC")
    public List<PartyGrid> getPartyGrids() {
        return partyGrids;
    }

    public void setPartyGrids(final List<PartyGrid> partyGrids) {
        this.partyGrids = partyGrids;
    }

    @Transient
    public List<Integer> getCards() {
        return cards;
    }

    public void setCards(final List<Integer> cards) {
        this.cards = cards;
    }

    @ManyToOne
    @JoinColumn(name = "party_info_id")
    public PartyInfo getPartyInfo() {
        return partyInfo;
    }

    public void setPartyInfo(final PartyInfo partyInfo) {
        this.partyInfo = partyInfo;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
