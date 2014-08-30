package com.fight2.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ArenaContinuousWin extends BaseEntity {
    private static final long serialVersionUID = -7298925985689022657L;
    public static final int DEFAULT_RATE = 10;
    public static final int MAX_RATE = 200;
    public static final int MAX_BACKGROUD_RATE = 400;
    private Date startDate;
    private Date endDate;
    private int rate = DEFAULT_RATE; // Continuous win reward rate.
    private int backgroudRate = DEFAULT_RATE;; // To calculate reduce rate when rate exceeds MAX_RATE.
    private boolean checkWin;// To check if the previous battle result is win.
    private boolean enable;
    private User user;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(final int rate) {
        this.rate = rate;
    }

    public int getBackgroudRate() {
        return backgroudRate;
    }

    public void setBackgroudRate(final int backgroudRate) {
        this.backgroudRate = backgroudRate;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public boolean isCheckWin() {
        return checkWin;
    }

    public void setCheckWin(final boolean checkWin) {
        this.checkWin = checkWin;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(final boolean enable) {
        this.enable = enable;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }
}
