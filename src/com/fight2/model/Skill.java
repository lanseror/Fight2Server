package com.fight2.model;

import java.util.List;

import javax.persistence.Entity;

@Entity
public class Skill extends BaseEntity {
    private static final long serialVersionUID = 3643769365102824893L;

    private int probability;

    private List<SkillOperation> operations;

    public int getProbability() {
        return probability;
    }

    public void setProbability(final int probability) {
        this.probability = probability;
    }

    public List<SkillOperation> getOperations() {
        return operations;
    }

    public void setOperations(final List<SkillOperation> operations) {
        this.operations = operations;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
