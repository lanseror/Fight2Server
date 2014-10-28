package com.fight2.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class UserQuestInfo extends BaseEntity {
    private static final long serialVersionUID = -4613964966772845997L;
    private User user;
    private int row;
    private int col;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", unique = true)
    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public int getRow() {
        return row;
    }

    public void setRow(final int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(final int col) {
        this.col = col;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
