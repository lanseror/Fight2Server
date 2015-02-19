package com.fight2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fight2.dao.GameMineDao;
import com.fight2.dao.PartyInfoDao;
import com.fight2.dao.UserDao;
import com.fight2.model.BattleResult;
import com.fight2.model.PartyInfo;
import com.fight2.model.User;
import com.fight2.model.quest.GameMine;
import com.fight2.model.quest.QuestTile;

@Service
public class MineService {
    @Autowired
    private PartyInfoDao partyInfoDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private GameMineDao gameMineDao;
    @Autowired
    private ComboSkillService comboSkillService;

    public boolean check(final QuestTile desTile) {
        return gameMineDao.getByPosition(desTile.getRow(), desTile.getCol()) != null;
    }

    public boolean attack(final User attacker, final QuestTile desTile) {
        final GameMine gameMine = gameMineDao.getByPosition(desTile.getRow(), desTile.getCol());
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
        return isWinner;
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

}
