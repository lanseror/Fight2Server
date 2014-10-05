package com.fight2.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class GuildPoll extends BaseEntity {
    private static final long serialVersionUID = -7156598538485536902L;
    private Guild guild;
    private User candidate;
    private int votes;// Number of votes

    @ManyToOne
    @JoinColumn(name = "guild_id")
    public Guild getGuild() {
        return guild;
    }

    public void setGuild(final Guild guild) {
        this.guild = guild;
    }

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    public User getCandidate() {
        return candidate;
    }

    public void setCandidate(final User candidate) {
        this.candidate = candidate;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(final int votes) {
        this.votes = votes;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
