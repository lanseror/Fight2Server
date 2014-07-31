package com.fight2.service;

import java.util.List;

import com.fight2.model.Party;
import com.fight2.model.PartyInfo;
import com.fight2.model.User;

public class BattleService {
    private final User attacker;
    private final User defender;
    private final PartyInfo attackerPartyInfo;
    private final PartyInfo defenderPartyInfo;
    private final List<Party> attackerParties;
    private final List<Party> defenderParties;

    public BattleService(final User attacker, final User defender, final PartyInfo attackerPartyInfo, final PartyInfo defenderPartyInfo) {
        super();
        this.attacker = attacker;
        this.defender = defender;
        this.attackerPartyInfo = attackerPartyInfo;
        this.defenderPartyInfo = defenderPartyInfo;
        this.attackerParties = attackerPartyInfo.getParties();
        this.defenderParties = defenderPartyInfo.getParties();
    }

    public String fight() {
        System.out.println("----战斗开始！----");
        while (getPartiesRemainHp(attackerParties) > 0 && getPartiesRemainHp(defenderParties) > 0) {
            // Attacker attack fist
            for (final Party attackerParty : attackerParties) {
                if (attackerParty.getHp() <= 0) {
                    continue;
                }

                for (final Party defenderParty : defenderParties) {
                    if (defenderParty.getHp() <= 0) {
                        continue;
                    } else {
                        attack(attackerParty, defenderParty, "A", "B");
                        break;
                    }

                }
            }

            // Defender attack
            for (final Party defenderParty : defenderParties) {
                if (defenderParty.getHp() <= 0) {
                    continue;
                }

                for (final Party attackerParty : attackerParties) {
                    if (attackerParty.getHp() <= 0) {
                        continue;
                    } else {
                        attack(defenderParty, attackerParty, "B", "A");
                        break;
                    }
                }
            }

        }
        System.out.println("----战斗结束！----");
        final String winner = getPartiesRemainHp(attackerParties) > 0 ? "A" : "B";
        System.out.println("----" + winner + "赢了！----");
        return winner;
    }

    private void attack(final Party attackerParty, final Party defenderParty, final String attacker, final String defender) {
        final int hp = defenderParty.getHp();
        final int atk = attackerParty.getAtk();
        final int reduceToHp = hp - atk;
        final int remainHp = reduceToHp < 0 ? 0 : reduceToHp;

        System.out.println(attacker + "的队伍" + attackerParty.getPartyNumber() + "对" + defender + "的队伍" + defenderParty.getPartyNumber() + "发起进攻，造成"
                + atk + "点伤害.");
        System.out.println(defender + "的队伍" + defenderParty.getPartyNumber() + "剩下" + remainHp + "点血.");

        defenderParty.setHp(remainHp);
    }

    private int getPartiesRemainHp(final List<Party> parties) {
        int hp = 0;
        for (final Party party : parties) {
            hp += party.getHp();
        }
        return hp;
    }

}
