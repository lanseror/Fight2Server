package com.fight2.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fight2.dao.CardImageDao;
import com.fight2.dao.CardTemplateDao;
import com.fight2.model.CardImage;
import com.fight2.model.CardTemplate;

@Service
@Scope("singleton")
@Lazy(true)
public class SummonHelper {
    private final static int GRID_SIZE = 10000;
    private final static List<CardTemplate> GRIDS = new ArrayList<CardTemplate>(GRID_SIZE);
    @Autowired
    private CardTemplateDao cardTemplateDao;
    @Autowired
    private CardImageDao cardImageDao;
    private final Random random = new Random();

    @PostConstruct
    public void reLoadData() {
        GRIDS.clear();
        final List<CardTemplate> cardTemplates = cardTemplateDao.list();
        for (final CardTemplate cardTemplate : cardTemplates) {
            final List<CardImage> avatars = cardImageDao.listByTypeAndCardTemplate(CardImage.TYPE_AVATAR, cardTemplate);
            final List<CardImage> mainImages = cardImageDao.listByTypeAndCardTemplate(CardImage.TYPE_MAIN, cardTemplate);
            final List<CardImage> thumbs = cardImageDao.listByTypeAndCardTemplate(CardImage.TYPE_THUMB, cardTemplate);
            cardTemplate.setAvatars(avatars);
            cardTemplate.setMainImages(mainImages);
            cardTemplate.setThumbImages(thumbs);
            for (int i = 0; i < cardTemplate.getProbability(); i++) {
                GRIDS.add(cardTemplate);
            }
        }
        final int gridCount = GRIDS.size();
        if (gridCount < GRID_SIZE) {
            final int diffCount = GRID_SIZE - gridCount;

            int mostCardCount = 0;
            final List<CardTemplate> mostCards = cardTemplateDao.listMostProbabilityCard(2);
            for (final CardTemplate mostCard : mostCards) {
                mostCardCount += mostCard.getProbability();
            }
            if (mostCardCount != 0) {
                for (final CardTemplate mostCard : mostCards) {
                    final List<CardImage> avatars = cardImageDao.listByTypeAndCardTemplate(CardImage.TYPE_AVATAR, mostCard);
                    final List<CardImage> mainImages = cardImageDao.listByTypeAndCardTemplate(CardImage.TYPE_MAIN, mostCard);
                    final List<CardImage> thumbs = cardImageDao.listByTypeAndCardTemplate(CardImage.TYPE_THUMB, mostCard);
                    mostCard.setAvatars(avatars);
                    mostCard.setMainImages(mainImages);
                    mostCard.setThumbImages(thumbs);
                    final int shouldAddCount = diffCount * mostCard.getProbability() / mostCardCount;
                    for (int i = 0; i < shouldAddCount; i++) {
                        GRIDS.add(mostCard);
                    }
                }
            }
        }
    }

    public CardTemplate summon() {
        final int randomIndex = random.nextInt(GRIDS.size() - 1);
        return GRIDS.get(randomIndex);
    }

    public CardTemplateDao getCardTemplateDao() {
        return cardTemplateDao;
    }

    public void setCardTemplateDao(final CardTemplateDao cardTemplateDao) {
        this.cardTemplateDao = cardTemplateDao;
    }

    public CardImageDao getCardImageDao() {
        return cardImageDao;
    }

    public void setCardImageDao(final CardImageDao cardImageDao) {
        this.cardImageDao = cardImageDao;
    }

}
