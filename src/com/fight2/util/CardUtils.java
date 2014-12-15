package com.fight2.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import com.fight2.model.Card;
import com.fight2.model.CardTemplate;
import com.fight2.model.Skill;
import com.fight2.model.SkillApplyParty;
import com.fight2.model.SkillOperation;
import com.fight2.model.SkillType;
import com.google.common.collect.Maps;

public class CardUtils {
    private final static Map<String, String> EFFECT_MAP = Maps.newHashMap();
    static {
        EFFECT_MAP.put("-1" + SkillType.HP, "对%s造成伤害");
        EFFECT_MAP.put("1" + SkillType.HP, "为%s恢复生命值");
        EFFECT_MAP.put("-1" + SkillType.ATK, "降低%s的攻击力");
        EFFECT_MAP.put("1" + SkillType.ATK, "增加%s的攻击力");
        EFFECT_MAP.put("-1" + SkillType.Defence, "为%s制造一个护盾");
        EFFECT_MAP.put("1" + SkillType.Defence, "为%s制造一个护盾");
        EFFECT_MAP.put("-1" + SkillType.Skip, "对%s造成眩晕");
        EFFECT_MAP.put("1" + SkillType.Skip, "对%s造成眩晕");
    }

    public static int getBaseExp(final Card card) {
        if (card.getBaseExp() == 0) {
            return card.getStar() * 100;
        } else {
            return card.getBaseExp();
        }
    }

    public static void upgrade(final Card card) {
        final int exp = card.getExp();
        final int currentLevel = card.getLevel();
        final int maxLevel = card.getStar() * 10 + (card.getTier() - 1) * 10;

        int level = 1;
        int levelExp = 0;
        for (int i = 1; i < maxLevel; i++) {
            levelExp += (int) (100 * Math.pow(1.065, i));
            if (exp >= levelExp) {
                level = i + 1;
            } else {
                break;
            }
        }
        card.setLevel(level);
        if (level == maxLevel) {
            card.setExp(levelExp);
        }

        if (currentLevel != card.getLevel()) {
            final int addedLevel = card.getLevel() - currentLevel;
            final int currentHp = card.getHp();
            final int currentAtk = card.getAtk();
            final int upgradeHp = (int) (card.getHp() * Math.pow(1.015, addedLevel));
            final int upgradeAtk = upgradeHp * currentAtk / currentHp;
            card.setHp(upgradeHp);
            card.setAtk(upgradeAtk);
        }

    }

    public static int getMaxLevel(final Card card) {
        return card.getStar() * 10 + (card.getTier() - 1) * 10;
    }

    public static int getMaxEvoTier(final Card card) {
        return (card.getStar() + 1) / 2 + 1;
    }

    public static void evolution(final Card mainCard, final Card supportCard) {
        final CardTemplate cardTemplate = mainCard.getCardTemplate();
        final Card higherTierCard = mainCard.getTier() > supportCard.getTier() ? mainCard : supportCard;
        final int baseHp = (int) (cardTemplate.getHp() * Math.pow(1.2, higherTierCard.getTier()));
        final int card1MaxLevel = getMaxLevel(mainCard);
        final double card1Rate = mainCard.getLevel() == card1MaxLevel ? 0.1 : 0.05;
        final int card1AddHp = (int) (mainCard.getHp() * card1Rate);
        final int card2MaxLevel = getMaxLevel(supportCard);
        final double card2Rate = supportCard.getLevel() == card2MaxLevel ? 0.1 : 0.05;
        final int card2AddHp = (int) (supportCard.getHp() * card2Rate);

        final int evoHp = baseHp + card1AddHp + card2AddHp;

        final BigDecimal templateAtk = BigDecimal.valueOf(cardTemplate.getAtk());
        final BigDecimal templateHp = BigDecimal.valueOf(cardTemplate.getHp());
        final BigDecimal evoHpDecimal = BigDecimal.valueOf(evoHp);
        final int evoAtk = templateAtk.divide(templateHp, 6, RoundingMode.HALF_UP).multiply(evoHpDecimal).intValue();

        final int evoBaseExp = (int) (getBaseExp(mainCard) * 0.75 + getBaseExp(supportCard) * 0.75 + mainCard.getExp() * 0.3 + supportCard.getExp() * 0.3);

        mainCard.setHp(evoHp);
        mainCard.setAtk(evoAtk);
        mainCard.setTier(mainCard.getTier() + 1);
        mainCard.setBaseExp(evoBaseExp);
        mainCard.setExp(0);

        supportCard.setHp(evoHp);
        supportCard.setAtk(evoAtk);
        supportCard.setTier(mainCard.getTier());
        supportCard.setBaseExp(evoBaseExp);
        supportCard.setExp(0);
    }

    public static String getSkillEffectString(final Card card) {
        return getSkillEffectString(card.getCardTemplate());
    }

    public static String getSkillEffectString(final CardTemplate cardTemplate) {
        final StringBuilder effectStrs = new StringBuilder();
        final Skill skill = cardTemplate.getSkill();
        final List<SkillOperation> operations = skill.getOperations();
        for (final SkillOperation operation : operations) {
            final SkillApplyParty skillApplyParty = operation.getSkillApplyParty();
            final SkillType skillType = operation.getSkillType();
            final int sign = operation.getSign();
            final String effectStr = EFFECT_MAP.get(String.valueOf(sign) + skillType);
            if (effectStrs.length() > 0) {
                effectStrs.append("，");
            }
            effectStrs.append(String.format(effectStr, skillApplyParty.getDescription()));
        }
        return effectStrs.toString();
    }
}
