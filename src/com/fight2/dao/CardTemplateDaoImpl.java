package com.fight2.dao;

import org.springframework.stereotype.Repository;

import com.fight2.model.CardTemplate;

@Repository
public class CardTemplateDaoImpl extends BaseDaoImpl<CardTemplate> implements CardTemplateDao {

    public CardTemplateDaoImpl() {
        super(CardTemplate.class);
    }

}
