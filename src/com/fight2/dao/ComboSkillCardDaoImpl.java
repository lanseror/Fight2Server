package com.fight2.dao;

import org.springframework.stereotype.Repository;

import com.fight2.model.ComboSkillCard;

@Repository
public class ComboSkillCardDaoImpl extends BaseDaoImpl<ComboSkillCard> implements ComboSkillCardDao {

    public ComboSkillCardDaoImpl() {
        super(ComboSkillCard.class);
    }

}
