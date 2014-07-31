package com.fight2.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

@Entity
public class PartyInfo extends BaseEntity {
    private static final long serialVersionUID = -949260842967108311L;

    private int hp;
    private int atk;
    private List<Party> parties;

    private User user;

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

    @OneToMany(mappedBy = "partyInfo")
    @OrderBy("partyNumber ASC")
    public List<Party> getParties() {
        return parties;
    }

    public void setParties(final List<Party> parties) {
        this.parties = parties;
    }

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
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
