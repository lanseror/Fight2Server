package com.fight2.dao;

import java.util.List;

import com.fight2.model.CardImage;
import com.fight2.model.CardTemplate;

public interface CardImageDao extends BaseDao<CardImage> {
    public List<CardImage> listByTypeAndCardTemplate(String type, CardTemplate cardTemplate);

    public List<CardImage> listByCardTemplate(CardTemplate cardTemplate);

    public CardImage getByTypeTierAndCardTemplate(String type, int tier, CardTemplate cardTemplate);
}
