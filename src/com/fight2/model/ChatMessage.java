package com.fight2.model;

import java.io.Serializable;
import java.util.Date;

public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 5519410863652000122L;

    private Date date;

    private String sender;

    private String content;

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
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
