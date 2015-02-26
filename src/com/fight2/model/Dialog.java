package com.fight2.model;

import javax.persistence.Entity;

@Entity
public class Dialog extends BaseEntity {
    private static final long serialVersionUID = 1413796321742490957L;
    private String content;
    private Speaker speaker;
    private OrderType orderType;

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public void setSpeaker(final Speaker speaker) {
        this.speaker = speaker;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(final OrderType orderType) {
        this.orderType = orderType;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public enum OrderType {
        Random,
        Sequence
    }

    public enum Speaker {
        Self,
        NPC,
        OtherPlayer
    }

}
