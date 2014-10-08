package com.fight2.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class GuildArenaUser extends BaseEntity {
    private static final long serialVersionUID = -3126675224775633848L;
    private Guild guild;
    private User user;
    private boolean locked;

    @ManyToOne
    @JoinColumn(name = "guild_id")
    public Guild getGuild() {
        return guild;
    }

    public void setGuild(final Guild guild) {
        this.guild = guild;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", unique = true)
    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(final boolean locked) {
        this.locked = locked;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
