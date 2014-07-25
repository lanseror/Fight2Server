package com.fight2.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class CardTemplate extends BaseEntity {
    private static final long serialVersionUID = 4701021880325329063L;
    private String name;
    private List<CardImage> avatars;
    private List<CardImage> mainImages;
    private List<CardImage> thumbImages;
    private int star;
    private int level;
    private int tier;// Evolution tier
    private int hp;
    private int atk;// Attack value;
    private int probability; // Summon probability, unit is 1/10000.
    private String skill;
    private int skillLevel;
    private int cardVersion;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cardTemplate")
    public List<CardImage> getAvatars() {
        return avatars;
    }

    public void setAvatars(final List<CardImage> avatars) {
        this.avatars = avatars;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cardTemplate")
    public List<CardImage> getMainImages() {
        return mainImages;
    }

    public void setMainImages(final List<CardImage> mainImages) {
        this.mainImages = mainImages;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cardTemplate")
    public List<CardImage> getThumbImages() {
        return thumbImages;
    }

    public void setThumbImages(final List<CardImage> thumbImages) {
        this.thumbImages = thumbImages;
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

    public int getProbability() {
        return probability;
    }

    public void setProbability(final int probability) {
        this.probability = probability;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(final String skill) {
        this.skill = skill;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(final int skillLevel) {
        this.skillLevel = skillLevel;
    }

    public int getCardVersion() {
        return cardVersion;
    }

    public void setCardVersion(final int cardVersion) {
        this.cardVersion = cardVersion;
    }

}
