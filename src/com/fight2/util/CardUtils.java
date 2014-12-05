package com.fight2.util;

import com.fight2.model.Card;

public class CardUtils {

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
}
