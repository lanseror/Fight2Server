package com.fight2.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class Guild extends BaseEntity {
    private static final long serialVersionUID = -6974306596128495874L;
    private String name;
    private String qq;
    private User president;
    private String notice;
    private Date createDate;
    private boolean pollEnabled;
    private List<GuildArenaUser> arenaUsers;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(final String qq) {
        this.qq = qq;
    }

    @ManyToOne
    @JoinColumn(name = "president_id", unique = true)
    public User getPresident() {
        return president;
    }

    public void setPresident(final User president) {
        this.president = president;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(final String notice) {
        this.notice = notice;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(final Date createDate) {
        this.createDate = createDate;
    }

    public boolean isPollEnabled() {
        return pollEnabled;
    }

    public void setPollEnabled(final boolean pollEnabled) {
        this.pollEnabled = pollEnabled;
    }

    @Transient
    public List<GuildArenaUser> getArenaUsers() {
        return arenaUsers;
    }

    public void setArenaUsers(final List<GuildArenaUser> arenaUsers) {
        this.arenaUsers = arenaUsers;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
