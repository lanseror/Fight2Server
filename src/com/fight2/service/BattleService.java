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

        effectMap.put("-1" + SkillType.HP, "对%造成伤害");
        effectMap.put("1" + SkillType.HP, "为%恢复生命值");
        effectMap.put("-1" + SkillType.ATK, "降低%的攻击力");
        effectMap.put("1" + SkillType.HP, "增加%的攻击力");
        effectMap.put("1" + SkillType.Defence, "为%制造一个护盾");
        effectMap.put("-1" + SkillType.Skip, "对%造成眩晕");
        effectMap.put("1" + SkillType.Skip, "对%造成眩晕");

        for (final Party party : attackerParties) {
            for (final PartyGrid partyGrid : party.getPartyGrids()) {
                final Card card = partyGrid.getCard();
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
        return null;
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

    private SkillRecord useSkill(final Party attackerParty, final Party defenderParty, final List<Party> attackerParties,
            final List<Party> defenderParties, final String attacker, final String defender) {
        final List<PartyGrid> atPartyGrids = Lists.newArrayList();
        for (final PartyGrid partyGrid : attackerParty.getPartyGrids()) {
            final Card card = partyGrid.getCard();
            final CardTemplate cardTemplate = card.getCardTemplate();
            final Skill skill = cardTemplate.getSkill();
            if (skill != null) {
                atPartyGrids.add(partyGrid);
            }
        }

        Collections.sort(atPartyGrids, new PartyGridComparator());

        final Card card = getSkillCard(atPartyGrids);
        if (card != null) {
            final SkillRecord skillRecord = new SkillRecord();
            final CardTemplate cardTemplate = card.getCardTemplate();
            final Skill skill = cardTemplate.getSkill();
            final List<SkillOperation> operations = skill.getOperations();
            skillRecord.setCardId(card.getId());
            skillRecord.setName(skill.getName());
            final List<SkillOperation> operationRecords = skillRecord.getOperations();
            System.out.println(cardTemplate.getName() + "发动机能：" + skill.getName());
            System.out.println("效果：" + skill.getName());
            final StringBuilder effectStrs = new StringBuilder();
            for (final SkillOperation operation : operations) {
                final SkillApplyParty skillApplyParty = operation.getSkillApplyParty();
                final List<Party> applyParties = getApplyParties(attackerParty, defenderParty, skillApplyParty);
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
                            applyParty.setHp(applyParty.getHp() + changePoint);
                        }
                    case ATK:
                        applyParty.setAtk(applyParty.getAtk() + changePoint);
                    case Defence:
                        applyParty.setDefence(applyParty.getDefence() + changePoint);
                    case Skip:
                        // TODO
                    }
                }

            }
            effectStrs.append("。");
            skillRecord.setEffect(effectStrs.toString());
        }

        return null;

    }

    private Card getSkillCard(final List<PartyGrid> atPartyGrids) {
        Card skillCard = null;
        for (final PartyGrid partyGrid : atPartyGrids) {
            final Card card = partyGrid.getCard();
            final Random random = randomMap.get(card);
            final int[] randomGrids = randomGridMap.get(card);
            if (randomGrids[random.nextInt(randomGrids.length)] == 1) {
                skillCard = card;
                break;
            }
        }

        return skillCard;
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
