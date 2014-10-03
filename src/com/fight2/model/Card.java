package com.fight2.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class Card extends BaseEntity {
    private static final long serialVersionUID = 4701021880325329063L;
    private String name;
    private String avatar;
    private String image;
    private int star = 1;
    private int level = 1;
    private int tier = 1;// Evolution tier
    private int hp;
    private int atk;// Attack value;
    private String skill;
    private int cardVersion;
    private CardStatus status = CardStatus.InCardPack;
    private User user;
    private CardTemplate cardTemplate;
    private int amount; // Use to count cards in same card template.

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Transient
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(final String avatar) {
        this.avatar = avatar;
    }

    @Transient
    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public int getStar() {
        return star;
    }

    public void setStar(final int star) {
        this.star = star;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(final int level) {
        this.level = level;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(final int tier) {
        this.tier = tier;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(final int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(final int atk) {
        this.atk = atk;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(final String skill) {
        this.skill = skill;
    }

    public int getCardVersion() {
        return cardVersion;
    }

    public void setCardVersion(final int cardVersion) {
        this.cardVersion = cardVersion;
    }

    public CardStatus getStatus() {
        return status;
    }

    public void setStatus(final CardStatus status) {
        this.status = status;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "card_template_id")
    public CardTemplate getCardTemplate() {
        return cardTemplate;
    }

    public void setCardTemplate(final CardTemplate cardTemplate) {
        this.cardTemplate = cardTemplate;
    }

    @Transient
    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public static enum CardStatus {
        InCardPack,
        InStoreroom;
    }
}
