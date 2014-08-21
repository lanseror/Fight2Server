package com.fight2.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class Arena extends BaseEntity {
    private static final long serialVersionUID = -3103546052796375038L;
    private String name;
    private Date startDate;
    private Date endDate;
    private int onlineNumber;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

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

    @Transient
    public int getOnlineNumber() {
        return onlineNumber;
    }

    public void setOnlineNumber(final int onlineNumber) {
        this.onlineNumber = onlineNumber;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
