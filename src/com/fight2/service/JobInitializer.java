package com.fight2.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.fight2.dao.ArenaDao;
import com.fight2.dao.BidDao;
import com.fight2.model.Arena;
import com.fight2.model.ArenaStatus;
import com.fight2.model.Bid;
import com.fight2.util.ArenaUtils;
import com.fight2.util.BidUtils;
import com.fight2.util.HibernateUtils;
import com.fight2.util.QuestUtils;

@Component
public class JobInitializer implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private ArenaDao arenaDao;
    @Autowired
    private ArenaService arenaService;
    @Autowired
    private BidDao bidDao;
    @Autowired
    private BidService bidService;
    @Autowired
    private TaskScheduler taskScheduler;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        QuestUtils.init();
        final Runnable refreshTreasureSchedule = createRefreshTreasureSchedule();
        taskScheduler.scheduleAtFixedRate(refreshTreasureSchedule, TimeUnit.MINUTES.toMillis(1));
        final SessionFactory sessionFactory = arenaDao.getSessionFactory();
        HibernateUtils.openSession(sessionFactory);
        final List<Arena> arenas = arenaDao.getAliveArenas();
        for (final Arena arena : arenas) {
            final Runnable stopSchedule = ArenaUtils.createStopSchedule(arena.getId(), arenaService);
            if (arena.getStatus() == ArenaStatus.Scheduled) {
                final Runnable startSchedule = ArenaUtils.createStartSchedule(arena.getId(), arenaDao);
                taskScheduler.schedule(startSchedule, arena.getStartDate());
                taskScheduler.schedule(stopSchedule, arena.getEndDate());
            } else if (arena.getStatus() == ArenaStatus.Started) {
                taskScheduler.schedule(stopSchedule, arena.getEndDate());
            }
        }
        final List<Bid> bids = bidDao.listOpenBids();
        for (final Bid bid : bids) {
            if (bid.getEndDate() != null) {
                final Runnable stopSchedule = BidUtils.createCloseSchedule(bid.getId(), bidService);
                taskScheduler.schedule(stopSchedule, bid.getEndDate());
            }
        }

        HibernateUtils.closeSession(sessionFactory);
    }

    public static Runnable createRefreshTreasureSchedule() {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                QuestUtils.refreshUserDatas();
            }

        };
        return runnable;
    }

    public ArenaDao getArenaDao() {
        return arenaDao;
    }

    public void setArenaDao(final ArenaDao arenaDao) {
        this.arenaDao = arenaDao;
    }

    public BidDao getBidDao() {
        return bidDao;
    }

    public void setBidDao(final BidDao bidDao) {
        this.bidDao = bidDao;
    }

    public TaskScheduler getTaskScheduler() {
        return taskScheduler;
    }

    public void setTaskScheduler(final TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public ArenaService getArenaService() {
        return arenaService;
    }

    public void setArenaService(final ArenaService arenaService) {
        this.arenaService = arenaService;
    }

}
