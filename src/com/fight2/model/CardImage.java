package com.fight2.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class CardImage extends BaseEntity {
    private static final long serialVersionUID = 3275659831582982537L;
    public static final String TYPE_AVATAR = "AVATAR";
    public static final String TYPE_MAIN = "MAIN";
    public static final String TYPE_THUMB = "THUMB";

    private String type;
    private int tier;
    private String url;
    private int width;
    private int height;
    private CardTemplate cardTemplate;

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(final int tier) {
        this.tier = tier;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(final int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(final int height) {
        this.height = height;
    }

    @ManyToOne
    @JoinColumn(name = "card_template")
    public CardTemplate getCardTemplate() {
        return cardTemplate;
    }

    public void setCardTemplate(final CardTemplate cardTemplate) {
        this.cardTemplate = cardTemplate;
    }

}
