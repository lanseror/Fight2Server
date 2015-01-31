package com.fight2.model;

import java.util.List;

import com.google.common.collect.Lists;

public class ComboSkillRecord {
    private int comboId;
    private final List<SkillOperation> operations = Lists.newArrayList();

    public int getComboId() {
        return comboId;
    }

    public void setComboId(final int comboId) {
        this.comboId = comboId;
    }

    public List<SkillOperation> getOperations() {
        return operations;
    }

    public enum ComboSkillType {
        BeforeSkill,
        AfterSkill,
        AfterAttack;
    }
}
