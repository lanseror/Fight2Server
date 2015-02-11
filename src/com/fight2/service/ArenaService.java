package com.fight2.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fight2.dao.ArenaDao;
import com.fight2.dao.ArenaRankingDao;
import com.fight2.dao.ArenaRewardDao;
import com.fight2.dao.CardDao;
import com.fight2.dao.GuildCardDao;
import com.fight2.dao.GuildStoreroomDao;
import com.fight2.dao.UserDao;
import com.fight2.dao.UserPropertiesDao;
import com.fight2.dao.UserStoreroomDao;
import com.fight2.model.Arena;
import com.fight2.model.ArenaRanking;
import com.fight2.model.ArenaReward;
import com.fight2.model.ArenaReward.ArenaRewardType;
import com.fight2.model.ArenaRewardItem;
import com.fight2.model.ArenaRewardItem.ArenaRewardItemType;
import com.fight2.model.ArenaStatus;
import com.fight2.model.Card;
import com.fight2.model.Card.CardStatus;
import com.fight2.model.CardTemplate;
import com.fight2.model.Guild;
import com.fight2.model.GuildCard;
import com.fight2.model.GuildStoreroom;
import com.fight2.model.User;
import com.fight2.model.UserProperties;
import com.fight2.model.UserStoreroom;
import com.fight2.util.HibernateUtils;

@Service
public class ArenaService {
    @Autowired
    private ArenaDao arenaDao;
    @Autowired
    private ArenaRewardDao arenaRewardDao;
    @Autowired
    private ArenaRankingDao arenaRankingDao;
    @Autowired
    private CardDao cardDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserPropertiesDao userPropertiesDao;
    @Autowired
    private UserStoreroomDao userStoreroomDao;
    @Autowired
    private GuildStoreroomDao guildStoreroomDao;
    @Autowired
    private GuildCardDao guildCardDao;

    public void stopArena(final int id) {
        final SessionFactory sessionFactory = arenaDao.getSessionFactory();
        HibernateUtils.openSession(sessionFactory);
        final Arena arena = arenaDao.get(id);
        if (arena != null && arena.getStatus() == ArenaStatus.Started) {
            arena.setStatus(ArenaStatus.Stopped);
            arenaDao.update(arena);
        }
        issueArenaRewards(arena);
        HibernateUtils.closeSession(sessionFactory);
    }

    private void issueArenaRewards(final Arena arena) {
        final boolean isGuildArena = arena.isGuildArena();
        final List<ArenaReward> arenaRewards = arenaRewardDao.listByArenaAndType(arena, ArenaRewardType.Ranking);
        for (final ArenaReward arenaReward : arenaRewards) {
            final List<ArenaRanking> arenaRankings = arenaRankingDao.listByArenaRange(arena, arenaReward.getMin(), arenaReward.getMax());
            for (final ArenaRanking arenaRanking : arenaRankings) {
                final User user = arenaRanking.getUser();
                final UserStoreroom userStoreroom = user.getStoreroom();
                final Guild guild = user.getGuild();
                final GuildStoreroom guildStoreroom = (isGuildArena && guild != null) ? guild.getStoreroom() : null;
                final List<ArenaRewardItem> rewardItems = arenaReward.getRewardItems();
                for (final ArenaRewardItem rewardItem : rewardItems) {
                    final ArenaRewardItemType rewardItemType = rewardItem.getType();
                    final int amount = rewardItem.getAmount();
                    if (rewardItemType == ArenaRewardItemType.ArenaTicket) {
                        if (isGuildArena && guildStoreroom != null) {
                            guildStoreroom.setTicket(guildStoreroom.getTicket() + amount);
                            guildStoreroomDao.update(guildStoreroom);
                        } else {
                            userStoreroom.setTicket(userStoreroom.getTicket() + amount);
                            userStoreroomDao.update(userStoreroom);
                        }
                    } else if (rewardItemType == ArenaRewardItemType.Stamina) {
                        if (isGuildArena && guildStoreroom != null) {
                            guildStoreroom.setStamina(guildStoreroom.getStamina() + amount);
                            guildStoreroomDao.update(guildStoreroom);
                        } else {
                            userStoreroom.setStamina(userStoreroom.getStamina() + amount);
                            userStoreroomDao.update(userStoreroom);
                        }

                    } else if (rewardItemType == ArenaRewardItemType.Card) {
                        final CardTemplate cardTemplate = rewardItem.getCardTemplate();
                        for (int i = 1; i <= amount; i++) {
                            final Card card = new Card();
                            card.setAtk(cardTemplate.getAtk());
                            card.setHp(cardTemplate.getHp());
                            card.setName(cardTemplate.getName());
                            card.setStar(cardTemplate.getStar());
                            card.setCardTemplate(cardTemplate);
                            if (!isGuildArena) {
                                card.setUser(user);
                            }
                            card.setStatus(CardStatus.InStoreroom);
                            if (isGuildArena && guildStoreroom != null) {
                                cardDao.add(card);
                                final GuildCard guildCard = new GuildCard();
                                guildCard.setCard(card);
                                guildCard.setGuildStoreroom(guildStoreroom);
                                guildCardDao.add(guildCard);
                            } else if (!isGuildArena) {
                                cardDao.add(card);
                            }
                        }
                        final List<Card> cards = cardDao.listByUser(user);
                        user.setCardCount(cards.size());
                        userDao.update(user);
                    } else if (rewardItemType == ArenaRewardItemType.GuildContribution) {
                        if (isGuildArena && guildStoreroom != null) {
                            final UserProperties userProperties = user.getUserProperties();
                            userProperties.setGuildContrib(userProperties.getGuildContrib() + amount);
                            userPropertiesDao.update(userProperties);
                        }
                    }
                }
            }
        }

    }

    public void issueMightRewardsIfApplicable(final Arena arena, final ArenaRanking arenaRanking) {
        final List<ArenaReward> arenaRewards = arenaRewardDao.listByArenaAndType(arena, ArenaRewardType.Might);
        final int issuedReward = arenaRanking.getIssuedReward();
        if (arenaRewards.size() == issuedReward) {
            return;
        }
        final ArenaReward arenaReward = arenaRewards.get(issuedReward);
        if (arenaRanking.getMight() < arenaReward.getMin()) {
            return;
        }

        final User user = arenaRanking.getUser();
        final UserStoreroom userStoreroom = user.getStoreroom();
        final List<ArenaRewardItem> rewardItems = arenaReward.getRewardItems();
        for (final ArenaRewardItem rewardItem : rewardItems) {
            final ArenaRewardItemType rewardItemType = rewardItem.getType();
            final int amount = rewardItem.getAmount();
            if (rewardItemType == ArenaRewardItemType.ArenaTicket) {
                userStoreroom.setTicket(userStoreroom.getTicket() + amount);
                userStoreroomDao.update(userStoreroom);
            } else if (rewardItemType == ArenaRewardItemType.Stamina) {
                userStoreroom.setStamina(userStoreroom.getStamina() + amount);
                userStoreroomDao.update(userStoreroom);
            } else if (rewardItemType == ArenaRewardItemType.Card) {
                final CardTemplate cardTemplate = rewardItem.getCardTemplate();
                for (int i = 1; i <= amount; i++) {
                    final Card card = new Card();
                    card.setAtk(cardTemplate.getAtk());
                    card.setHp(cardTemplate.getHp());
                    card.setName(cardTemplate.getName());
                    card.setStar(cardTemplate.getStar());
                    card.setCardTemplate(cardTemplate);
                    card.setUser(user);
                    card.setStatus(CardStatus.InStoreroom);
                    cardDao.add(card);
                }
                final List<Card> cards = cardDao.listByUser(user);
                user.setCardCount(cards.size());
                userDao.update(user);
            }
        }

        arenaRanking.setIssuedReward(issuedReward + 1);
    }

    public ArenaDao getArenaDao() {
        return arenaDao;
    }

    public void setArenaDao(final ArenaDao arenaDao) {
        this.arenaDao = arenaDao;
    }

    public ArenaRewardDao getArenaRewardDao() {
        return arenaRewardDao;
    }

    public void setArenaRewardDao(final ArenaRewardDao arenaRewardDao) {
        this.arenaRewardDao = arenaRewardDao;
    }

    public ArenaRankingDao getArenaRankingDao() {
        return arenaRankingDao;
    }

    public void setArenaRankingDao(final ArenaRankingDao arenaRankingDao) {
        this.arenaRankingDao = arenaRankingDao;
    }

    public CardDao getCardDao() {
        return cardDao;
    }

    public void setCardDao(final CardDao cardDao) {
        this.cardDao = cardDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }

    public UserPropertiesDao getUserPropertiesDao() {
        return userPropertiesDao;
    }

    public void setUserPropertiesDao(final UserPropertiesDao userPropertiesDao) {
        this.userPropertiesDao = userPropertiesDao;
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
