package com.fight2.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.fight2.model.ArenaContinuousWin;
import com.fight2.model.BattleRecord;
import com.fight2.model.BattleResult;
import com.fight2.model.Card;
import com.fight2.model.CardTemplate;
import com.fight2.model.ComboSkill;
import com.fight2.model.Party;
import com.fight2.model.PartyGrid;
import com.fight2.model.PartyInfo;
import com.fight2.model.RevivalRecord;
import com.fight2.model.RevivalRecord.RevivalType;
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
    private static final int[] MIGHTS = { 10, 8, 5, 2 };
    private final PartyInfo attackerPartyInfo;
    private final PartyInfo defenderPartyInfo;
    private final List<Party> attackerParties = Lists.newArrayList();
    private final List<Party> defenderParties = Lists.newArrayList();
    private final List<Party> attackerPartiesVo = Lists.newArrayList();
    private final List<Party> defenderPartiesVo = Lists.newArrayList();
    private final ArenaContinuousWin continuousWin;
    private ComboSkillService comboSkillService;
    private final Map<Integer, List<ComboSkill>> comboSkillMap = Maps.newHashMap();

    private final static Map<String, String> EFFECT_MAP = Maps.newHashMap();
    private final Map<Card, Random> randomMap = Maps.newHashMap();
    private final Map<Card, int[]> randomGridMap = Maps.newHashMap();
    private final Random random = new Random();
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

    public BattleService(final User attacker, final User defender, final PartyInfo attackerPartyInfo, final PartyInfo defenderPartyInfo,
            final ArenaContinuousWin continuousWin) {
        super();
        this.continuousWin = continuousWin;
        this.attackerPartyInfo = attackerPartyInfo;
        this.defenderPartyInfo = defenderPartyInfo;
    }

    private void handlePreCombo(final Party party, final Party origParty) {
        final List<ComboSkill> comboSkills = comboSkillMap.get(party.getId());
        final Iterator<ComboSkill> it = comboSkills.iterator();
        while (it.hasNext()) {
            final ComboSkill skill = it.next();
            for (final SkillOperation operation : skill.getOperations()) {
                final SkillType skillType = operation.getSkillType();
                final int point = operation.getPoint();
                if (skillType == SkillType.HP) {
                    final int changePoint = point * origParty.getHp() / 100;
                    party.setHp(party.getHp() + changePoint);
                } else if (skillType == SkillType.ATK) {
                    final int changePoint = point * origParty.getAtk() / 100;
                    party.setAtk(party.getAtk() + changePoint);
                } else if (skillType == SkillType.Defence) {
                    party.setProtection(point);
                }
                if (skillType != SkillType.Revival) {
                    it.remove();
                }
            }
        }
    }

    public BattleResult fight(final int index) {
        for (final Party partyPo : attackerPartyInfo.getParties()) {
            final Party copyParty = new Party(partyPo);
            copyParty.setPartyGrids(partyPo.getPartyGrids());
            final List<ComboSkill> comboSkills = comboSkillService.getComboSkills(partyPo, true);
            comboSkillMap.put(copyParty.getId(), comboSkills);
            handlePreCombo(copyParty, partyPo);
            attackerParties.add(copyParty);
            attackerPartiesVo.add(new Party(copyParty));
        }
        for (final Party partyPo : defenderPartyInfo.getParties()) {
            final Party copyParty = new Party(partyPo);
            copyParty.setPartyGrids(partyPo.getPartyGrids());
            final List<ComboSkill> comboSkills = comboSkillService.getComboSkills(partyPo, true);
            comboSkillMap.put(copyParty.getId(), comboSkills);
            handlePreCombo(copyParty, partyPo);
            defenderParties.add(copyParty);
            defenderPartiesVo.add(new Party(copyParty));
        }

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

        final BattleResult battleResult = new BattleResult();
        battleResult.setAttackerParties(attackerPartiesVo);
        battleResult.setDefenderParties(defenderPartiesVo);
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
                        final SkillRecord skillRecord = useSkill(attackerParty, attackerParties, defenderParties, "Player1", "Player2",
                                atkBattleRecord);
                        atkBattleRecord.setSkill(skillRecord);
                        battleRecords.add(atkBattleRecord);
                        for (int defenderIndex = 0; defenderIndex < defenderParties.size(); defenderIndex++) {
                            final Party defenderParty = defenderParties.get(defenderIndex);
                            if (defenderParty.getHp() <= 0) {
                                continue;
                            } else {
                                atkBattleRecord.setDefenceParty(defenderIndex);
                                final int atk = attack(attackerParty, defenderParty, attackerParties, defenderParties, "Player1", "Player2",
                                        atkBattleRecord);
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
                        final SkillRecord skillRecord = useSkill(defenderParty, defenderParties, attackerParties, "Player2", "Player1",
                                dfcBattleRecord);
                        dfcBattleRecord.setSkill(skillRecord);
                        battleRecords.add(dfcBattleRecord);
                        for (int attackeIndex = 0; attackeIndex < attackerParties.size(); attackeIndex++) {
                            final Party attackerParty = attackerParties.get(attackeIndex);
                            if (attackerParty.getHp() <= 0) {
                                continue;
                            } else {
                                dfcBattleRecord.setDefenceParty(attackeIndex);
                                final int atk = attack(defenderParty, attackerParty, defenderParties, attackerParties, "Player2", "Player1",
                                        dfcBattleRecord);
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

        final boolean isWinner = getPartiesRemainHp(attackerParties) > 0;
        final boolean isAllAlive = isPartiesAllAlive(attackerParties);
        final int baseMight = isWinner ? MIGHTS[index] : MIGHTS[3];
        final int aliveMight = isAllAlive ? 3 : 0;

        battleResult.setBattleRecord(battleRecords);
        battleResult.setWinner(isWinner);
        battleResult.setBaseMight(baseMight);
        battleResult.setAliveMight(aliveMight);
        if (continuousWin != null && continuousWin.isEnable()) {
            final int rate = continuousWin.getRate();
            final double calculateRate = continuousWin.getRate() / 100.0;
            final int cwMight = (int) Math.ceil((baseMight + aliveMight) * calculateRate);
            battleResult.setCwMight(cwMight);
            battleResult.setCwRate(rate);
        }
        battleResult.setTotalMight(baseMight + aliveMight + battleResult.getCwMight());
        return battleResult;
    }

    private int attack(final Party attackerParty, final Party defenderParty, final List<Party> attackerParties, final List<Party> defenderParties,
            final String attacker, final String defender, final BattleRecord battleRecord) {
        // useSkill(attackerParty, defenderParty, attackerParties, defenderParties, attacker, defender);

        for (int i = 0; i < attackerParties.size(); i++) {
            final Party party = attackerParties.get(i);
            System.out.println(String.format("%s的team%s - HP:%s  ATK:%s, 护盾: %s", attacker, party.getPartyNumber(), party.getHp(), party.getAtk(),
                    party.getDefence()));

        }

        final int hp = defenderParty.getHp();
        final int atk = attackerParty.getAtk() * (100 - defenderParty.getProtection()) / 100;
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
        if (defenderParty.getHp() <= 0) {
            reviveIfApplicable(defenderParty, RevivalType.AfterAttack, battleRecord);
        }
        return atk;
    }

    private void reviveIfApplicable(final Party party, final RevivalType type, final BattleRecord battleRecord) {
        final List<ComboSkill> comboSkills = comboSkillMap.get(party.getId());
        final Iterator<ComboSkill> it = comboSkills.iterator();
        ComboLoop: while (it.hasNext()) {
            final ComboSkill comboSkill = it.next();

            for (final SkillOperation operation : comboSkill.getOperations()) {
                if (operation.getSkillType() == SkillType.Revival) {
                    if (random.nextInt(100) < comboSkill.getProbability()) {
                        final int point = operation.getPoint();
                        final int changePoint = point * party.getFullHp() / 100;
                        party.setHp(changePoint);
                        final RevivalRecord revivalRecord = new RevivalRecord();
                        revivalRecord.setComboId(comboSkill.getId());
                        revivalRecord.setType(type);
                        revivalRecord.setPoint(point);
                        revivalRecord.setPartyNumber(party.getPartyNumber());
                        battleRecord.getRevivalRecords().add(revivalRecord);
                        it.remove();
                        System.out.println(String.format("触发复活技能，复活了%s%%", changePoint));
                        break ComboLoop;
                    }
                }
            }
        }
    }

    private SkillRecord useSkill(final Party selfParty, final List<Party> selfParties, final List<Party> opponentParties, final String attacker,
            final String defender, final BattleRecord battleRecord) {
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
            final List<Boolean> opponentPartiesAliveInfo = Lists.newArrayList();
            for (final Party party : opponentParties) {
                opponentPartiesAliveInfo.add(isPartyAlive(party));
            }
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

                final String effectStr = EFFECT_MAP.get(String.valueOf(sign) + skillType);
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
                            final int changeDefence = applyParty.getDefence() + changePoint * (100 - applyParty.getProtection()) / 100;
                            ;
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
                    case Revival:
                        // TODO
                        break;
                    }
                }

            }
            effectStrs.append("。");
            skillRecord.setEffect(effectStrs.toString());
            System.out.println(String.format("%s的team%s - %s发动技能：%s. 效果：%s", attacker, selfParty.getPartyNumber(), cardTemplate.getName(),
                    skill.getName(), effectStrs.toString()));

            for (int i = 0; i < opponentParties.size(); i++) {
                final Party party = opponentParties.get(i);
                if (opponentPartiesAliveInfo.get(i) && !isPartyAlive(party)) {
                    reviveIfApplicable(party, RevivalType.AfterSkill, battleRecord);
                }
            }
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

    private boolean isPartiesAllAlive(final List<Party> parties) {
        boolean isAllAlive = true;
        for (final Party party : parties) {
            if (party.getHp() <= 0) {
                isAllAlive = false;
                break;
            }
        }
        return isAllAlive;
    }

    private boolean isPartyAlive(final Party party) {
        return party.getHp() > 0;
    }

    public ComboSkillService getComboSkillService() {
        return comboSkillService;
    }

    public void setComboSkillService(final ComboSkillService comboSkillService) {
        this.comboSkillService = comboSkillService;
    }

}
