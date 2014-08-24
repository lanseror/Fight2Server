package com.fight2.model;

public enum ArenaStatus {
    Scheduled("已配置"),
    Started("进行中"),
    Stopped("已结束"),
    Cancelled("已取消");

    private final String text;

    private ArenaStatus(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
