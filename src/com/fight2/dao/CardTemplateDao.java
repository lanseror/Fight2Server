package com.fight2.dao;

import java.util.List;

import com.fight2.model.CardTemplate;

public interface CardTemplateDao extends BaseDao<CardTemplate> {

    public List<CardTemplate> listMostProbabilityCardByStar(int i, int star);

}
