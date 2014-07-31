package com.fight2.model;

import javax.persistence.Entity;

@Entity
public class SkillOperation extends BaseEntity {
    private static final long serialVersionUID = -8724159517984094999L;

    private int point;// Percentage, 1/10000.

    private SkillPointType skillPointType;

    private SkillApplyParty skillApplyParty;

    public int getPoint() {
        return point;
    }

    public void setPoint(final int point) {
        this.point = point;
    }

    public SkillPointType getSkillPointType() {
        return skillPointType;
    }

    public void setSkillPointType(final SkillPointType skillPointType) {
        this.skillPointType = skillPointType;
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
