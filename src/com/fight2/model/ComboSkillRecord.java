package com.fight2.model;

import java.util.List;

import com.google.common.collect.Lists;

public class ComboSkillRecord {
    private int cardIndex;
    private String name;
    private String effect;
    private final List<SkillOperation> operations = Lists.newArrayList();

    public int getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(final int cardIndex) {
        this.cardIndex = cardIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(final String effect) {
        this.effect = effect;
    }

    public List<SkillOperation> getOperations() {
        return operations;
    }

}
