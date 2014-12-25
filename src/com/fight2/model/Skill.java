package com.fight2.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Skill extends BaseEntity {
    private static final long serialVersionUID = 3643769365102824893L;

    private String name;

    private int probability;

    private List<SkillOperation> operations;

    private CardTemplate cardTemplate;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getProbability() {
        return probability;
    }

    public void setProbability(final int probability) {
        this.probability = probability;
    }

    @OneToMany(mappedBy = "skill", fetch = FetchType.EAGER)
    public List<SkillOperation> getOperations() {
        return operations;
    }

    public void setOperations(final List<SkillOperation> operations) {
        this.operations = operations;
    }

    @OneToOne
    @JoinColumn(name = "card_template_id", unique = true, nullable = true)
    public CardTemplate getCardTemplate() {
        return cardTemplate;
    }

    public void setCardTemplate(final CardTemplate cardTemplate) {
        this.cardTemplate = cardTemplate;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
