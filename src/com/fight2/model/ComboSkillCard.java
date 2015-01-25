package com.fight2.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class ComboSkillCard extends BaseEntity {
    private static final long serialVersionUID = -6844578894577268998L;

    private ComboSkill comboSkill;

    private CardTemplate cardTemplate;

    @ManyToOne
    @JoinColumn(name = "combo_skill_id")
    public ComboSkill getComboSkill() {
        return comboSkill;
    }

    public void setComboSkill(final ComboSkill comboSkill) {
        this.comboSkill = comboSkill;
    }

    @OneToOne
    @JoinColumn(name = "card_template_id", nullable = false)
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
