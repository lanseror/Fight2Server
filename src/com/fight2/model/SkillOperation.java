package com.fight2.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class SkillOperation extends BaseEntity {
    private static final long serialVersionUID = -8724159517984094999L;

    private int sign; // +1/-1

    private int point;// Percentage, 1/100.

    private SkillType skillType;

    private SkillPointAttribute skillPointAttribute;

    private SkillApplyParty skillApplyParty;

    private Skill skill;

    private ComboSkill comboSkill;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    public Skill getSkill() {
        return skill;
    }

    public void setSkill(final Skill skill) {
        this.skill = skill;
    }

    @ManyToOne
    @JoinColumn(name = "combo_skill_id")
    public ComboSkill getComboSkill() {
        return comboSkill;
    }

    public void setComboSkill(final ComboSkill comboSkill) {
        this.comboSkill = comboSkill;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(final int sign) {
        this.sign = sign;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(final int point) {
        this.point = point;
    }

    public SkillType getSkillType() {
        return skillType;
    }

    public void setSkillType(final SkillType skillType) {
        this.skillType = skillType;
    }

    public SkillPointAttribute getSkillPointAttribute() {
        return skillPointAttribute;
    }

    public void setSkillPointAttribute(final SkillPointAttribute skillPointAttribute) {
        this.skillPointAttribute = skillPointAttribute;
    }

    public SkillApplyParty getSkillApplyParty() {
        return skillApplyParty;
    }

    public void setSkillApplyParty(final SkillApplyParty skillApplyParty) {
        this.skillApplyParty = skillApplyParty;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
