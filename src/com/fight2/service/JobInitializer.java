package com.fight2.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.fight2.dao.ArenaDao;
import com.fight2.model.Arena;
import com.fight2.model.ArenaStatus;
import com.fight2.util.ArenaUtils;
import com.fight2.util.HibernateUtils;

@Component
public class JobInitializer implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private ArenaDao arenaDao;
    @Autowired
    private ArenaService arenaService;
    @Autowired
    private TaskScheduler taskScheduler;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
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
        HibernateUtils.closeSession(sessionFactory);
    }

    public ArenaDao getArenaDao() {
        return arenaDao;
    }

    public void setArenaDao(final ArenaDao arenaDao) {
        this.arenaDao = arenaDao;
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
