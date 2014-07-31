package com.fight2.model;

import java.util.List;

import javax.persistence.Entity;

@Entity
public class Skill extends BaseEntity {
    private static final long serialVersionUID = 3643769365102824893L;

    private int probability;

    private List<SkillOperation> hpOperation;

    private int atk;

    private int defence;

    public int getProbability() {
        return probability;
    }

    public void setProbability(final int probability) {
        this.probability = probability;
    }

    public List<SkillOperation> getHpOperation() {
        return hpOperation;
    }

    public void setHpOperation(final List<SkillOperation> hpOperation) {
        this.hpOperation = hpOperation;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(final int atk) {
        this.atk = atk;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(final int defence) {
        this.defence = defence;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
