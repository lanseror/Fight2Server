package com.fight2.dao;

import java.util.List;

import com.fight2.model.ComboSkill;

public interface ComboSkillDao extends BaseDao<ComboSkill> {
    
    public List<ComboSkill> getComboSkills(int cardTemplateId);

}
