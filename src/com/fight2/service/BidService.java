package com.fight2.service;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fight2.dao.BidDao;
import com.fight2.dao.CardDao;
import com.fight2.dao.GuildCardDao;
import com.fight2.dao.GuildStoreroomDao;
import com.fight2.dao.UserStoreroomDao;
import com.fight2.model.Bid;
import com.fight2.model.Bid.BidItemType;
import com.fight2.model.Bid.BidStatus;
import com.fight2.model.Card;
import com.fight2.model.Card.CardStatus;
import com.fight2.model.GuildCard;
import com.fight2.model.GuildStoreroom;
import com.fight2.model.User;
import com.fight2.model.UserStoreroom;
import com.fight2.util.HibernateUtils;

@Service
public class BidService {
    @Autowired
    private BidDao bidDao;
    @Autowired
    private CardDao cardDao;
    @Autowired
    private UserStoreroomDao userStoreroomDao;
    @Autowired
    private GuildStoreroomDao guildStoreroomDao;
    @Autowired
    private GuildCardDao guildCardDao;

    public void closeBid(final int id) {
        final SessionFactory sessionFactory = bidDao.getSessionFactory();
        HibernateUtils.openSession(sessionFactory);
        final Bid bid = bidDao.get(id);
        if (bid != null && bid.getStatus() == BidStatus.Started) {
            bid.setStatus(BidStatus.Closed);
            bidDao.update(bid);
        }
        issueBid(bid);
        HibernateUtils.closeSession(sessionFactory);
    }

    private void issueBid(final Bid bid) {
        final BidItemType itemType = bid.getType();
        final User user = bid.getUser();
        final int amount = bid.getAmount();
        if (user == null) {
            final GuildStoreroom guildStoreroom = bid.getGuild().getStoreroom();
            if (itemType == BidItemType.ArenaTicket) {
                guildStoreroom.setTicket(guildStoreroom.getTicket() + amount);
                guildStoreroomDao.update(guildStoreroom);
            } else if (itemType == BidItemType.Stamina) {
                guildStoreroom.setStamina(guildStoreroom.getStamina() + amount);
                guildStoreroomDao.update(guildStoreroom);
            } else if (itemType == BidItemType.Card) {
                final GuildCard guildCard = bid.getGuildCard();
                guildCard.setGuildStoreroom(guildStoreroom);
                guildCardDao.update(guildCard);
            }
        } else {
            final UserStoreroom userStoreroom = user.getStoreroom();
            if (itemType == BidItemType.ArenaTicket) {
                userStoreroom.setTicket(userStoreroom.getTicket() + amount);
                userStoreroomDao.update(userStoreroom);
            } else if (itemType == BidItemType.Stamina) {
                userStoreroom.setStamina(userStoreroom.getStamina() + amount);
                userStoreroomDao.update(userStoreroom);
            } else if (itemType == BidItemType.Card) {
                final GuildCard guildCard = bid.getGuildCard();
                final Card card = guildCard.getCard();
                card.setStatus(CardStatus.InStoreroom);
                card.setUser(user);
                cardDao.update(card);
                guildCardDao.delete(guildCard);
            }
        }

    }

    public BidDao getBidDao() {
        return bidDao;
    }

    public void setBidDao(final BidDao bidDao) {
        this.bidDao = bidDao;
    }

    public CardDao getCardDao() {
        return cardDao;
    }

    public void setCardDao(final CardDao cardDao) {
        this.cardDao = cardDao;
    }

    public UserStoreroomDao getUserStoreroomDao() {
        return userStoreroomDao;
    }

    public void setUserStoreroomDao(final UserStoreroomDao userStoreroomDao) {
        this.userStoreroomDao = userStoreroomDao;
    }

    public GuildStoreroomDao getGuildStoreroomDao() {
        return guildStoreroomDao;
    }

    public void setGuildStoreroomDao(final GuildStoreroomDao guildStoreroomDao) {
        this.guildStoreroomDao = guildStoreroomDao;
    }

    public GuildCardDao getGuildCardDao() {
        return guildCardDao;
    }

    public void setGuildCardDao(final GuildCardDao guildCardDao) {
        this.guildCardDao = guildCardDao;
    }

}
