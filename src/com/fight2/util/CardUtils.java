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

    public static void getUpgradeLevel(final Card card) {
        final int exp = card.getExp();
        final int currentLevel = card.getLevel();
        final int maxLevel = card.getStar() * 10 + (card.getTier() - 1) * 10;

        final int maxLevelExp = (int) (100 * Math.pow(1.065, maxLevel - 1));
        if (exp >= maxLevelExp) {
            card.setLevel(maxLevel);
            card.setExp(maxLevelExp);
        } else {
            int level = 1;
            for (int i = currentLevel; i < maxLevel; i++) {
                final int levelExp = (int) (100 * Math.pow(1.065, i));
                if (exp >= levelExp) {
                    level = currentLevel + 1;
                } else {
                    break;
                }
            }
            card.setLevel(level);
        }
    }

}
