package com.fight2.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class GuildVoter extends BaseEntity {
    private static final long serialVersionUID = -3126675224775633848L;
    private Guild guild;
    private User voter;

    @ManyToOne
    @JoinColumn(name = "guild_id")
    public Guild getGuild() {
        return guild;
    }

    public void setGuild(final Guild guild) {
        this.guild = guild;
    }

    @ManyToOne
    @JoinColumn(name = "voter_id")
    public User getVoter() {
        return voter;
    }

    public void setVoter(final User voter) {
        this.voter = voter;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
