package com.fight2.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.fight2.dao.CardImageDao;
import com.fight2.dao.CardTemplateDao;
import com.fight2.dao.ComboSkillCardDao;
import com.fight2.dao.ComboSkillDao;
import com.fight2.model.Card;
import com.fight2.model.CardImage;
import com.fight2.model.CardTemplate;
import com.fight2.model.ComboSkill;
import com.fight2.model.ComboSkillCard;
import com.fight2.model.Party;
import com.fight2.model.PartyGrid;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service
@Scope("singleton")
@Lazy(true)
public class ComboSkillService {
    private static Map<String, Integer> COMBO_MAP = Maps.newHashMap();

    @Autowired
    private ComboSkillDao comboSkillDao;
    @Autowired
    private ComboSkillCardDao comboSkillCardDao;
    @Autowired
    private CardTemplateDao cardTemplateDao;
    @Autowired
    private CardImageDao cardImageDao;

    @PostConstruct
    public void reLoadData() {
        COMBO_MAP.clear();
        final List<ComboSkill> skills = comboSkillDao.list();
        for (final ComboSkill skill : skills) {
            final List<ComboSkillCard> skillCards = skill.getComboSkillCards();
            final StringBuilder keyBuilder = new StringBuilder(25);
            for (final ComboSkillCard skillCard : skillCards) {
                keyBuilder.append(skillCard.getCardTemplate().getId());
                keyBuilder.append("_");
            }
            COMBO_MAP.put(keyBuilder.toString(), skill.getId());
        }
    }

    public List<ComboSkill> getComboSkills(final List<Integer> templateIds, final boolean persistent) {
        final List<ComboSkill> skills = Lists.newArrayList();
        if (templateIds.size() == 0) {
            return skills;
        }
        Collections.sort(templateIds);
        final List<String> keys = Lists.newArrayList();
        calculateKey(keys, templateIds);
        for (final String key : keys) {
            if (COMBO_MAP.containsKey(key)) {
                final int skillId = COMBO_MAP.get(key);
                final ComboSkill comboSkill = comboSkillDao.get(skillId);
                if (persistent) {
                    skills.add(comboSkill);
                } else {
                    skills.add(new ComboSkill(comboSkill));
                }

            }
        }
        return skills;
    }

    public List<ComboSkill> getComboSkills(final Party party, final boolean persistent) {
        final List<Integer> partyTemplateIds = Lists.newArrayList();
        for (final PartyGrid partyGrid : party.getPartyGrids()) {
            final Card card = partyGrid.getCard();
            if (card != null) {
                partyTemplateIds.add(card.getCardTemplate().getId());
            }
        }

        return getComboSkills(partyTemplateIds, persistent);
    }

    private static void calculateKey(final List<String> keys, final List<Integer> idList) {
        if (idList.size() == 1) {
            return;
        }
        final int firstKey = idList.get(0);
        final List<Integer> subIdList = idList.subList(1, idList.size());
        calculateKey(keys, subIdList);
        final List<String> addKeys = Lists.newArrayList();
        for (final String key : keys) {
            addKeys.add(firstKey + "_" + key);
        }
        for (final int id : subIdList) {
            addKeys.add(firstKey + "_" + id + "_");
        }
        keys.addAll(addKeys);
    }

    public List<ComboSkill> getComboSkills(final int templateId, final boolean persistent) {
        final List<ComboSkill> comboSkills = comboSkillDao.getComboSkills(templateId);
        if (persistent) {
            return comboSkills;
        } else {
            final List<ComboSkill> skills = Lists.newArrayList();
            for (final ComboSkill comboSkill : comboSkills) {
                final ComboSkill comboSkillVo = new ComboSkill(comboSkill);
                final List<ComboSkillCard> comboSkillCards = comboSkill.getComboSkillCards();
                final List<Card> cards = Lists.newArrayList();
                for (final ComboSkillCard comboSkillCard : comboSkillCards) {
                    final CardTemplate cardTemplate = comboSkillCard.getCardTemplate();
                    final CardImage avatarObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_AVATAR, 1, cardTemplate);
                    final String avatar = avatarObj.getUrl();
                    final Card card = new Card();
                    card.setId(cardTemplate.getId());
                    card.setAvatar(avatar);
                    cards.add(card);
                }
                comboSkillVo.setCards(cards);
                skills.add(comboSkillVo);
            }
            return skills;
        }
    }

    public ComboSkillDao getComboSkillDao() {
        return comboSkillDao;
    }

    public void setComboSkillDao(final ComboSkillDao comboSkillDao) {
        this.comboSkillDao = comboSkillDao;
    }

    public ComboSkillCardDao getComboSkillCardDao() {
        return comboSkillCardDao;
    }

    public void setComboSkillCardDao(final ComboSkillCardDao comboSkillCardDao) {
        this.comboSkillCardDao = comboSkillCardDao;
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
