package com.fight2.model;

import javax.persistence.Entity;

@Entity
public class ChatMessage extends BaseEntity {
    private static final long serialVersionUID = 5519410863652000122L;

    private String date;

    private String sender;

    private String content;

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(final String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
