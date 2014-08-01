package com.fight2.dao;

import org.springframework.stereotype.Repository;

import com.fight2.model.SkillOperation;

@Repository
public class SkillOperationDaoImpl extends BaseDaoImpl<SkillOperation> implements SkillOperationDao {

    public SkillOperationDaoImpl() {
        super(SkillOperation.class);
    }

}
