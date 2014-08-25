package com.fight2.model.json;

public class ArenaJson {
    private int id;
    private String name;
    private String remainTime;
    private int onlineNumber;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(final String remainTime) {
        this.remainTime = remainTime;
    }

    public int getOnlineNumber() {
        return onlineNumber;
    }

    public void setOnlineNumber(final int onlineNumber) {
        this.onlineNumber = onlineNumber;
    }

}
