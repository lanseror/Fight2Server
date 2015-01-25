package com.fight2.dao;

import org.springframework.stereotype.Repository;

import com.fight2.model.ComboSkill;

@Repository
public class ComboSkillDaoImpl extends BaseDaoImpl<ComboSkill> implements ComboSkillDao {

    public ComboSkillDaoImpl() {
        super(ComboSkill.class);
    }

}
