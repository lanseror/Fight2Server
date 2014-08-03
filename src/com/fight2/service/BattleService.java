package com.fight2.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fight2.model.Card;
import com.fight2.model.CardTemplate;
import com.fight2.model.Party;
import com.fight2.model.PartyGrid;
import com.fight2.model.PartyInfo;
import com.fight2.model.Skill;
import com.fight2.model.SkillApplyParty;
import com.fight2.model.SkillOperation;
import com.fight2.model.SkillPointAttribute;
import com.fight2.model.SkillType;
import com.fight2.model.User;
import com.google.common.collect.Lists;

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

            for (int partyIndex = 0; partyIndex < 3; partyIndex++) {
                // Attacker attack fist
                if (partyIndex < attackerParties.size()) {
                    final Party attackerParty = attackerParties.get(partyIndex);
                    if (attackerParty.getHp() > 0) {
                        for (final Party defenderParty : defenderParties) {
                            if (defenderParty.getHp() <= 0) {
                                continue;
                            } else {
                                attack(attackerParty, defenderParty, attackerParties, defenderParties, "你", defender.getName());
                                break;
                            }
                        }

                    }

                }

                // Defender attack
                if (partyIndex < defenderParties.size()) {
                    final Party defenderParty = defenderParties.get(partyIndex);
                    if (defenderParty.getHp() > 0) {
                        for (final Party attackerParty : attackerParties) {
                            if (attackerParty.getHp() <= 0) {
                                continue;
                            } else {
                                attack(defenderParty, attackerParty, defenderParties, attackerParties, defender.getName(), "你");
                                break;
                            }
                        }

                    }
                }
            }

        }
        System.out.println("----战斗结束！----");
        final String winner = getPartiesRemainHp(attackerParties) > 0 ? "你" : defender.getName();
        System.out.println("----" + winner + "赢了！----");
        return winner;
    }

    private void attack(final Party attackerParty, final Party defenderParty, final List<Party> attackerParties, final List<Party> defenderParties,
            final String attacker, final String defender) {
        // useSkill(attackerParty, defenderParty, attackerParties, defenderParties, attacker, defender);

        final int hp = defenderParty.getHp();
        final int atk = attackerParty.getAtk();
        final int reduceToHp = hp - atk;
        final int remainHp = reduceToHp < 0 ? 0 : reduceToHp;

        System.out.println(attacker + "的队伍" + attackerParty.getPartyNumber() + "对" + defender + "的队伍" + defenderParty.getPartyNumber() + "发起进攻，造成"
                + atk + "点伤害.");
        System.out.println(defender + "的队伍" + defenderParty.getPartyNumber() + "剩下" + remainHp + "点血.");

        defenderParty.setHp(remainHp);
    }

    private void useSkill(final Party attackerParty, final Party defenderParty, final List<Party> attackerParties, final List<Party> defenderParties,
            final String attacker, final String defender) {
        final List<PartyGrid> atPartyGrids = Lists.newArrayList();
        for (final PartyGrid partyGrid : attackerParty.getPartyGrids()) {
            final Card card = partyGrid.getCard();
            final CardTemplate cardTemplate = card.getCardTemplate();
            final Skill skill = cardTemplate.getSkill();
            if (skill != null) {
                atPartyGrids.add(partyGrid);
                // skill.getProbability()
                // final List<SkillOperation> operations = skill.getOperations();
                // for (final SkillOperation operation : operations) {
                // final SkillApplyParty skillApplyParty = operation.getSkillApplyParty();
                //
                // }
            }
        }

        Collections.sort(atPartyGrids, new PartyGridComparator());
        final PartyGrid partyGrid = atPartyGrids.size() > 0 ? atPartyGrids.get(0) : null;

        if (partyGrid != null) {
            final Card card = partyGrid.getCard();
            final CardTemplate cardTemplate = card.getCardTemplate();
            final Skill skill = cardTemplate.getSkill();
            final List<SkillOperation> operations = skill.getOperations();
            System.out.println(cardTemplate.getName() + "发动机能：" + skill.getName());
            System.out.println("效果：" + skill.getName());
            for (final SkillOperation operation : operations) {
                final List<Party> applyParties = getApplyParties(attackerParty, defenderParty, operation.getSkillApplyParty());
                final SkillType skillType = operation.getSkillType();
                final int sign = operation.getSign();
                final int point = operation.getPoint();
                final SkillPointAttribute pointAttribute = operation.getSkillPointAttribute();
                final int changePoint = sign * point * getAttribute(pointAttribute, card) / 100;

                for (final Party applyParty : applyParties) {
                    switch (skillType) {
                    case HP:
                        applyParty.setHp(applyParty.getHp() + changePoint);
                    case ATK:
                        applyParty.setAtk(applyParty.getAtk() + changePoint);
                    case Defence:
                        applyParty.setDefence(applyParty.getDefence() + changePoint);
                    case Skip:
                        // TODO
                    }
                }

            }
        }

    }

    private int getAttribute(final SkillPointAttribute pointAttribute, final Card card) {
        switch (pointAttribute) {
        case HP:
            return card.getHp();
        case ATK:
            return card.getAtk();
        default:
            return card.getHp();
        }

    }

    private List<Party> getApplyParties(final Party attackerParty, final Party defenderParty, final SkillApplyParty skillApplyParty) {

        switch (skillApplyParty) {
        case Self:
            return Lists.newArrayList(attackerParty);
        case Opponent:
            return Lists.newArrayList(defenderParty);
        case Leader:
            return Lists.newArrayList(attackerParties.get(0));
        case OpponentLeader:
            return Lists.newArrayList(defenderParties.get(0));
        case SelfAll:
            return attackerParties;
        case OpponentAll:
            return defenderParties;
        default:
            return Lists.newArrayList(attackerParty);
        }
    }

    private class PartyGridComparator implements Comparator<PartyGrid> {
        @Override
        public int compare(final PartyGrid partyGrid1, final PartyGrid partyGrid2) {
            final Card card1 = partyGrid1.getCard();
            final CardTemplate cardTemplate1 = card1.getCardTemplate();
            final Skill skill1 = cardTemplate1.getSkill();
            final Card card2 = partyGrid2.getCard();
            final CardTemplate cardTemplate2 = card2.getCardTemplate();
            final Skill skill2 = cardTemplate2.getSkill();

            return skill1.getProbability() - skill2.getProbability();
        }
    }

    private int getPartiesRemainHp(final List<Party> parties) {
        int hp = 0;
        for (final Party party : parties) {
            hp += party.getHp();
        }
        return hp;
    }

}
