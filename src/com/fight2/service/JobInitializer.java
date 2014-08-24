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
import com.fight2.util.HibernateUtils;

@Component
public class JobInitializer implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private ArenaDao arenaDao;
    @Autowired
    private TaskScheduler taskScheduler;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        final SessionFactory sessionFactory = arenaDao.getSessionFactory();
        HibernateUtils.openSession(sessionFactory);
        final List<Arena> arenas = arenaDao.getAliveArenas();
        for (final Arena arena : arenas) {
            final Runnable stopSchedule = createStopSchedule(arena.getId());
            if (arena.getStatus() == ArenaStatus.Scheduled) {
                final Runnable startSchedule = createStartSchedule(arena.getId());
                taskScheduler.schedule(startSchedule, arena.getStartDate());
                taskScheduler.schedule(stopSchedule, arena.getEndDate());
            } else if (arena.getStatus() == ArenaStatus.Started) {
                taskScheduler.schedule(stopSchedule, arena.getEndDate());
            }
        }
        HibernateUtils.closeSession(sessionFactory);
    }

    private Runnable createStartSchedule(final int id) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final SessionFactory sessionFactory = arenaDao.getSessionFactory();
                HibernateUtils.openSession(sessionFactory);
                final Arena arena = arenaDao.get(id);
                if (arena != null && arena.getStatus() == ArenaStatus.Scheduled) {
                    arena.setStatus(ArenaStatus.Started);
                    arenaDao.update(arena);
                }
                HibernateUtils.closeSession(sessionFactory);
            }

        };
        return runnable;
    }

    private Runnable createStopSchedule(final int id) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final SessionFactory sessionFactory = arenaDao.getSessionFactory();
                HibernateUtils.openSession(sessionFactory);
                final Arena arena = arenaDao.get(id);
                if (arena != null && arena.getStatus() == ArenaStatus.Started) {
                    arena.setStatus(ArenaStatus.Stopped);
                    arenaDao.update(arena);
                }
                HibernateUtils.closeSession(sessionFactory);
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

    public TaskScheduler getTaskScheduler() {
        return taskScheduler;
    }

    public void setTaskScheduler(final TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

}
