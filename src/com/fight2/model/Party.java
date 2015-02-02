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

    private int fullHp;

    private int atk;

    private int defence;

    private int protection;

    private List<PartyGrid> partyGrids;

    private List<Integer> cards;

    private List<ComboSkill> comboSkills;

    private PartyInfo partyInfo;

    public Party() {

    }

    public Party(final Party party) {
        super(party);
        this.partyNumber = party.getPartyNumber();
        this.hp = party.getHp();
        this.fullHp = party.getFullHp();
        this.atk = party.getAtk();
        this.defence = party.getDefence();
        this.protection = party.getProtection();
        this.cards = party.getCards();
        this.comboSkills = party.getComboSkills();
    }

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

    @Transient
    public int getFullHp() {
        return fullHp;
    }

    public void setFullHp(final int fullHp) {
        this.fullHp = fullHp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(final int atk) {
        this.atk = atk;
    }

    @Transient
    public int getDefence() {
        return defence;
    }

    public void setDefence(final int defence) {
        this.defence = defence;
    }

    @Transient
    public int getProtection() {
        return protection;
    }

    public void setProtection(final int protection) {
        this.protection = protection;
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

    @Transient
    public List<ComboSkill> getComboSkills() {
        return comboSkills;
    }

    public void setComboSkills(final List<ComboSkill> comboSkills) {
        this.comboSkills = comboSkills;
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
