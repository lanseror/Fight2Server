package com.fight2.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.fight2.model.ComboSkill;

@Repository
public class ComboSkillDaoImpl extends BaseDaoImpl<ComboSkill> implements ComboSkillDao {

    public ComboSkillDaoImpl() {
        super(ComboSkill.class);
    }

    @Override
    public List<ComboSkill> getComboSkills(final int cardTemplateId) {
        final Query query = getSession().createQuery("select distinct s from ComboSkillCard sc inner join sc.comboSkill s where sc.cardTemplate.id=?")
                .setParameter(0, cardTemplateId);
        @SuppressWarnings("unchecked")
        final List<ComboSkill> list = query.list();
        return list;
    }

}
