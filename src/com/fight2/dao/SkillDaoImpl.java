package com.fight2.dao;

import org.springframework.stereotype.Repository;

import com.fight2.model.Skill;

@Repository
public class SkillDaoImpl extends BaseDaoImpl<Skill> implements SkillDao {

    public SkillDaoImpl() {
        super(Skill.class);
    }

}
