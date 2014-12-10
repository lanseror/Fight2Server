package com.fight2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fight2.dao.PartyDao;
import com.fight2.dao.PartyGridDao;
import com.fight2.dao.PartyInfoDao;
import com.fight2.dao.UserDao;
import com.fight2.model.Card;
import com.fight2.model.Party;
import com.fight2.model.PartyGrid;
import com.fight2.model.PartyInfo;
import com.fight2.model.User;

@Service
public class PartyService {
    @Autowired
    private PartyDao partyDao;
    @Autowired
    private PartyInfoDao partyInfoDao;
    @Autowired
    private PartyGridDao partyGridDao;
    @Autowired
    private UserDao userDao;

    public void refreshPartyHpAtk(final int userId) {
        final User user = userDao.get(userId);
        final PartyInfo partyInfo = partyInfoDao.getByUser(user);
        final List<Party> parties = partyInfo.getParties();
        int partyInfoAtk = 0;
        int partyInfoHp = 0;
        for (final Party party : parties) {
            final List<PartyGrid> partyGrids = party.getPartyGrids();
            int partyAtk = 0;
            int partyHp = 0;
            for (final PartyGrid partyGrid : partyGrids) {
                final Card card = partyGrid.getCard();
                if (card != null) {
                    partyAtk += card.getAtk();
                    partyHp += card.getHp();
                }
            }
            party.setAtk(partyAtk);
            party.setHp(partyHp);
            partyDao.update(party);
            partyInfoAtk += partyAtk;
            partyInfoHp += partyHp;
        }
        partyInfo.setAtk(partyInfoAtk);
        partyInfo.setHp(partyInfoHp);
        partyInfoDao.update(partyInfo);
    }

    public boolean isCardInParty(final Card card) {
        return partyGridDao.isCardInParty(card);
    }

    public PartyDao getPartyDao() {
        return partyDao;
    }

    public void setPartyDao(final PartyDao partyDao) {
        this.partyDao = partyDao;
    }

    public PartyInfoDao getPartyInfoDao() {
        return partyInfoDao;
    }

    public void setPartyInfoDao(final PartyInfoDao partyInfoDao) {
        this.partyInfoDao = partyInfoDao;
    }

    public PartyGridDao getPartyGridDao() {
        return partyGridDao;
    }

    public void setPartyGridDao(final PartyGridDao partyGridDao) {
        this.partyGridDao = partyGridDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }

}
