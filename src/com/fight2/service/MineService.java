package com.fight2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fight2.dao.GameMineDao;
import com.fight2.dao.PartyInfoDao;
import com.fight2.dao.UserDao;
import com.fight2.dao.UserPropertiesDao;
import com.fight2.model.BattleResult;
import com.fight2.model.PartyInfo;
import com.fight2.model.User;
import com.fight2.model.UserProperties;
import com.fight2.model.quest.GameMine;
import com.fight2.model.quest.QuestTile;
import com.fight2.util.Constants;

@Service
public class MineService {
    @Autowired
    private PartyInfoDao partyInfoDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserPropertiesDao userPropertiesDao;
    @Autowired
    private GameMineDao gameMineDao;
    @Autowired
    private ComboSkillService comboSkillService;

    public boolean check(final QuestTile mineTile) {
        return gameMineDao.getByPosition(mineTile.getRow(), mineTile.getCol()) != null;
    }

    public BattleResult attack(final User attacker, final QuestTile mineTile) {
        final GameMine gameMine = gameMineDao.getByPosition(mineTile.getRow(), mineTile.getCol());
        final User defender = userDao.get(gameMine.getOwnerId());
        final PartyInfo attackerPartyInfo = partyInfoDao.getByUser(attacker);
        final PartyInfo defenderPartyInfo = partyInfoDao.getByUser(defender);

        final BattleService battleService = new BattleService(attacker, defender, attackerPartyInfo, defenderPartyInfo, null);
        battleService.setComboSkillService(comboSkillService);
        final BattleResult battleResult = battleService.fight(0);
        final boolean isWinner = battleResult.isWinner();
        if (isWinner) {
            gameMine.setOwnerId(attacker.getId());
            gameMineDao.update(gameMine);
        }
        final UserProperties userProps = attacker.getUserProperties();
        userProps.setDiamon(userProps.getDiamon() - Constants.MINE_ATTACK_COST);
        userPropertiesDao.update(userProps);
        return battleResult;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }

    public ComboSkillService getComboSkillService() {
        return comboSkillService;
    }

    public void setComboSkillService(final ComboSkillService comboSkillService) {
        this.comboSkillService = comboSkillService;
    }

    public PartyInfoDao getPartyInfoDao() {
        return partyInfoDao;
    }

    public void setPartyInfoDao(final PartyInfoDao partyInfoDao) {
        this.partyInfoDao = partyInfoDao;
    }

    public GameMineDao getGameMineDao() {
        return gameMineDao;
    }

    public void setGameMineDao(final GameMineDao gameMineDao) {
        this.gameMineDao = gameMineDao;
    }

    public UserPropertiesDao getUserPropertiesDao() {
        return userPropertiesDao;
    }

    public void setUserPropertiesDao(final UserPropertiesDao userPropertiesDao) {
        this.userPropertiesDao = userPropertiesDao;
    }

}
