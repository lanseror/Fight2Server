package com.fight2.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

@Entity
public class ComboSkill extends BaseEntity {
    private static final long serialVersionUID = 3643769365102824893L;

    private String name;

    private int probability;

    private String icon;

    private List<SkillOperation> operations;

    private List<ComboSkillCard> comboSkillCards;

    private List<Card> cards;

    public ComboSkill() {
        super();
    }

    public ComboSkill(final ComboSkill comboSkill) {
        super(comboSkill);
        this.name = comboSkill.getName();
        this.probability = comboSkill.getProbability();
        this.icon = comboSkill.getIcon();
        this.cards = comboSkill.getCards();
    }

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(final String icon) {
        this.icon = icon;
    }

    @OneToMany(mappedBy = "comboSkill", fetch = FetchType.EAGER)
    public List<SkillOperation> getOperations() {
        return operations;
    }

    public void setOperations(final List<SkillOperation> operations) {
        this.operations = operations;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @OneToMany(mappedBy = "comboSkill")
    @OrderBy("cardTemplate.id ASC")
    public List<ComboSkillCard> getComboSkillCards() {
        return comboSkillCards;
    }

    public void setComboSkillCards(final List<ComboSkillCard> comboSkillCards) {
        this.comboSkillCards = comboSkillCards;
    }

    @Transient
    public List<Card> getCards() {
        return cards;
    }

    public void setCards(final List<Card> cards) {
        this.cards = cards;
    }

}
