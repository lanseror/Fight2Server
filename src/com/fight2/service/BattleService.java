package com.fight2.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.fight2.model.BattleRecord;
import com.fight2.model.Card;
import com.fight2.model.CardTemplate;
import com.fight2.model.Party;
import com.fight2.model.PartyGrid;
import com.fight2.model.PartyInfo;
import com.fight2.model.Skill;
import com.fight2.model.SkillApplyParty;
import com.fight2.model.SkillOperation;
import com.fight2.model.SkillPointAttribute;
import com.fight2.model.SkillRecord;
import com.fight2.model.SkillType;
import com.fight2.model.User;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class BattleService {
    private final User attacker;
    private final User defender;
    private final PartyInfo attackerPartyInfo;
    private final PartyInfo defenderPartyInfo;
    private final List<Party> attackerParties;
    private final List<Party> defenderParties;

    private final Map<String, String> effectMap = Maps.newHashMap();
    private final Map<Card, Random> randomMap = Maps.newHashMap();
    private final Map<Card, int[]> randomGridMap = Maps.newHashMap();

    public BattleService(final User attacker, final User defender, final PartyInfo attackerPartyInfo, final PartyInfo defenderPartyInfo) {
        super();
        this.attacker = attacker;
        this.defender = defender;
        this.attackerPartyInfo = attackerPartyInfo;
        this.defenderPartyInfo = defenderPartyInfo;
        this.attackerParties = attackerPartyInfo.getParties();
        this.defenderParties = defenderPartyInfo.getParties();

        effectMap.put("-1" + SkillType.HP, "对%s造成伤害");
        effectMap.put("1" + SkillType.HP, "为%s恢复生命值");
        effectMap.put("-1" + SkillType.ATK, "降低%s的攻击力");
        effectMap.put("1" + SkillType.ATK, "增加%s的攻击力");
        effectMap.put("-1" + SkillType.Defence, "为%s制造一个护盾");
        effectMap.put("1" + SkillType.Defence, "为%s制造一个护盾");
        effectMap.put("-1" + SkillType.Skip, "对%s造成眩晕");
        effectMap.put("1" + SkillType.Skip, "对%s造成眩晕");

        for (final Party party : attackerParties) {
            for (final PartyGrid partyGrid : party.getPartyGrids()) {
                final Card card = partyGrid.getCard();
                if (card == null) {
                    continue;
                }
                final Skill skill = card.getCardTemplate().getSkill();
                if (skill != null) {
                    randomMap.put(card, new Random());
                    final int[] randomGrid = new int[100];
                    for (int i = 0; i < skill.getProbability(); i++) {
                        randomGrid[i] = 1;
                    }
                    randomGridMap.put(card, randomGrid);
                }
            }

        }

        for (final Party party : defenderParties) {
            for (final PartyGrid partyGrid : party.getPartyGrids()) {
                final Card card = partyGrid.getCard();
                if (card == null) {
                    continue;
                }
                final Skill skill = card.getCardTemplate().getSkill();
                if (skill != null) {
                    randomMap.put(card, new Random());
                    final int[] randomGrid = new int[100];
                    for (int i = 0; i < skill.getProbability(); i++) {
                        randomGrid[i] = 1;
                    }
                    randomGridMap.put(card, randomGrid);
                }
            }
        }
    }

    public List<BattleRecord> fight() {

        System.out.println("----初始数值----");
        for (int i = 0; i < attackerParties.size(); i++) {
            final Party party = attackerParties.get(i);
            party.setFullHp(party.getHp());
            System.out.println(String.format("%s的team%s - HP:%s  ATK:%s, 护盾: %s", "Player1", party.getPartyNumber(), party.getHp(), party.getAtk(),
                    party.getDefence()));

        }
        System.out.println();
        for (int i = 0; i < defenderParties.size(); i++) {
            final Party party = defenderParties.get(i);
            party.setFullHp(party.getHp());
            System.out.println(String.format("%s的team%s - HP:%s  ATK:%s, 护盾: %s", "Player2", party.getPartyNumber(), party.getHp(), party.getAtk(),
                    party.getDefence()));

        }
        System.out.println();
        System.out.println("----战斗开始！----");

        final List<BattleRecord> battleRecords = Lists.newArrayList();
        while (getPartiesRemainHp(attackerParties) > 0 && getPartiesRemainHp(defenderParties) > 0) {

            for (int partyIndex = 0; partyIndex < 3; partyIndex++) {
                // Self attack fist
                if (partyIndex < attackerParties.size()) {
                    final Party attackerParty = attackerParties.get(partyIndex);
                    if (attackerParty.getHp() > 0) {
                        final BattleRecord atkBattleRecord = new BattleRecord();
                        atkBattleRecord.setActionPlayer("Player1");
                        atkBattleRecord.setAtkParty(partyIndex);
                        final SkillRecord skillRecord = useSkill(attackerParty, attackerParties, defenderParties, "Player1", "Player2");
                        atkBattleRecord.setSkill(skillRecord);
                        battleRecords.add(atkBattleRecord);
                        for (int defenderIndex = 0; defenderIndex < defenderParties.size(); defenderIndex++) {
                            final Party defenderParty = defenderParties.get(defenderIndex);
                            if (defenderParty.getHp() <= 0) {
                                continue;
                            } else {
                                atkBattleRecord.setDefenceParty(defenderIndex);
                                final int atk = attack(attackerParty, defenderParty, attackerParties, defenderParties, "Player1", "Player2");
                                atkBattleRecord.setAtk(atk);
                                break;
                            }
                        }
                        if (getPartiesRemainHp(defenderParties) <= 0) {
                            break;
                        }
                    }
                }

                // Opponent attack
                if (partyIndex < defenderParties.size()) {
                    final Party defenderParty = defenderParties.get(partyIndex);
                    if (defenderParty.getHp() > 0) {
                        final BattleRecord dfcBattleRecord = new BattleRecord();
                        dfcBattleRecord.setActionPlayer("Player2");
                        dfcBattleRecord.setAtkParty(partyIndex);
                        final SkillRecord skillRecord = useSkill(defenderParty, defenderParties, attackerParties, "Player2", "Player1");
                        dfcBattleRecord.setSkill(skillRecord);
                        battleRecords.add(dfcBattleRecord);
                        for (int attackeIndex = 0; attackeIndex < attackerParties.size(); attackeIndex++) {
                            final Party attackerParty = attackerParties.get(attackeIndex);
                            if (attackerParty.getHp() <= 0) {
                                continue;
                            } else {
                                dfcBattleRecord.setDefenceParty(attackeIndex);
                                final int atk = attack(defenderParty, attackerParty, defenderParties, attackerParties, "Player2", "Player1");
                                dfcBattleRecord.setAtk(atk);
                                break;
                            }
                        }
                        if (getPartiesRemainHp(attackerParties) <= 0) {
                            break;
                        }
                    }
                }
            }

        }
        System.out.println("----战斗结束！----");
        final String winner = getPartiesRemainHp(attackerParties) > 0 ? "Player1" : "Player2";
        System.out.println("----" + winner + "赢了！----");
        return battleRecords;
    }

    private int attack(final Party attackerParty, final Party defenderParty, final List<Party> attackerParties, final List<Party> defenderParties,
            final String attacker, final String defender) {
        // useSkill(attackerParty, defenderParty, attackerParties, defenderParties, attacker, defender);

        for (int i = 0; i < attackerParties.size(); i++) {
            final Party party = attackerParties.get(i);
            System.out.println(String.format("%s的team%s - HP:%s  ATK:%s, 护盾: %s", attacker, party.getPartyNumber(), party.getHp(), party.getAtk(),
                    party.getDefence()));

        }

        final int hp = defenderParty.getHp();
        final int atk = attackerParty.getAtk();
        final int defence = defenderParty.getDefence();
        final int changeDefence = defence - atk;
        if (changeDefence > 0) {
            defenderParty.setDefence(changeDefence);
        } else {
            defenderParty.setDefence(0);
            final int changeHp = hp + changeDefence;
            defenderParty.setHp(changeHp < 0 ? 0 : changeHp);
        }
        System.out.println(String.format("%s的team%s攻击%s的team%s造成伤害: %s", attacker, attackerParty.getPartyNumber(), defender,
                defenderParty.getPartyNumber(), atk));
        System.out.println();
        System.out.println();
        return atk;
    }

    private SkillRecord useSkill(final Party selfParty, final List<Party> selfParties, final List<Party> opponentParties, final String attacker,
            final String defender) {
        final List<PartyGrid> atPartyGrids = Lists.newArrayList();
        for (final PartyGrid partyGrid : selfParty.getPartyGrids()) {
            final Card card = partyGrid.getCard();
            if (card == null) {
                continue;
            }
            final CardTemplate cardTemplate = card.getCardTemplate();
            final Skill skill = cardTemplate.getSkill();
            if (skill != null) {
                atPartyGrids.add(partyGrid);
            }
        }

        Collections.sort(atPartyGrids, new PartyGridComparator());

        final PartyGrid skillPartyGrid = getSkillCard(atPartyGrids);
        if (skillPartyGrid != null && skillPartyGrid.getCard() != null) {
            final Card card = skillPartyGrid.getCard();
            final SkillRecord skillRecord = new SkillRecord();
            final CardTemplate cardTemplate = card.getCardTemplate();
            final Skill skill = cardTemplate.getSkill();
            final List<SkillOperation> operations = skill.getOperations();
            skillRecord.setCardIndex(skillPartyGrid.getGridNumber());
            skillRecord.setName(skill.getName());
            final List<SkillOperation> operationRecords = skillRecord.getOperations();
            final StringBuilder effectStrs = new StringBuilder();
            for (final SkillOperation operation : operations) {
                final SkillApplyParty skillApplyParty = operation.getSkillApplyParty();
                final List<Party> applyParties = getApplyParties(selfParty, skillApplyParty, selfParties, opponentParties);
                final SkillType skillType = operation.getSkillType();
                final int sign = operation.getSign();
                final int point = operation.getPoint();
                final SkillPointAttribute pointAttribute = operation.getSkillPointAttribute();
                final int changePoint = sign * point * getAttribute(pointAttribute, card) / 100;

                final String effectStr = effectMap.get(String.valueOf(sign) + skillType);
                if (effectStrs.length() > 0) {
                    effectStrs.append("，");
                }
                effectStrs.append(String.format(effectStr, skillApplyParty.getDescription()));

                final SkillOperation operationRecord = new SkillOperation();
                operationRecord.setSign(sign);
                operationRecord.setPoint(changePoint * sign);
                operationRecord.setSkillApplyParty(operation.getSkillApplyParty());
                operationRecord.setSkillType(skillType);
                operationRecords.add(operationRecord);

                for (final Party applyParty : applyParties) {
                    switch (skillType) {
                    case HP:
                        if (changePoint < 0) {
                            final int changeDefence = applyParty.getDefence() + changePoint;
                            if (changeDefence > 0) {
                                applyParty.setDefence(changeDefence);
                            } else {
                                applyParty.setDefence(0);
                                final int changeHp = applyParty.getHp() + changeDefence;
                                applyParty.setHp(changeHp < 0 ? 0 : changeHp);
                            }

                        } else {
                            final int currentHp = applyParty.getHp();
                            final int fullHp = applyParty.getFullHp();
                            final int toSetHp = currentHp + changePoint > fullHp ? fullHp : currentHp + changePoint;
                            applyParty.setHp(toSetHp);
                        }
                        break;
                    case ATK:
                        applyParty.setAtk(applyParty.getAtk() + changePoint);
                        break;
                    case Defence:
                        applyParty.setDefence(applyParty.getDefence() + Math.abs(changePoint));
                        break;
                    case Skip:
                        // TODO
                        break;
                    }
                }

            }
            effectStrs.append("。");
            skillRecord.setEffect(effectStrs.toString());
            System.out.println(String.format("%s的team%s - %s发动技能：%s. 效果：%s", attacker, selfParty.getPartyNumber(), cardTemplate.getName(),
                    skill.getName(), effectStrs.toString()));
            return skillRecord;
        }

        return null;

    }

    private PartyGrid getSkillCard(final List<PartyGrid> atPartyGrids) {
        PartyGrid skillPartyGrid = null;
        for (final PartyGrid partyGrid : atPartyGrids) {
            final Card card = partyGrid.getCard();
            final Random random = randomMap.get(card);
            final int[] randomGrids = randomGridMap.get(card);
            if (randomGrids[random.nextInt(randomGrids.length)] == 1) {
                skillPartyGrid = partyGrid;
                break;
            }
        }

        return skillPartyGrid;
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

    private List<Party> getApplyParties(final Party selfParty, final SkillApplyParty skillApplyParty, final List<Party> selfParties,
            final List<Party> opponentParties) {
        switch (skillApplyParty) {
        case Self:
            return Lists.newArrayList(selfParty);
        case Opponent:
            return getFirstAliveParty(opponentParties);
        case Leader:
            return getLeaderParty(selfParties);
        case OpponentLeader:
            return getLeaderParty(opponentParties);
        case SelfAll:
            return getAliveParties(selfParties);
        case OpponentAll:
            return getAliveParties(opponentParties);
        default:
            return Lists.newArrayList(selfParty);
        }
    }

    private List<Party> getLeaderParty(final List<Party> parties) {
        final List<Party> leaderParties = Lists.newArrayList();
        final Party leaderParty = parties.get(0);
        if (leaderParty.getHp() > 0) {
            leaderParties.add(leaderParty);
        }
        return leaderParties;
    }

    private List<Party> getFirstAliveParty(final List<Party> parties) {
        final List<Party> firstAliveParty = Lists.newArrayList();
        for (final Party party : parties) {
            if (party.getHp() > 0) {
                firstAliveParty.add(party);
                break;
            }
        }
        return firstAliveParty;
    }

    private List<Party> getAliveParties(final List<Party> parties) {
        final List<Party> aliveParties = Lists.newArrayList();
        for (final Party party : parties) {
            if (party.getHp() > 0) {
                aliveParties.add(party);
            }
        }
        return aliveParties;
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
