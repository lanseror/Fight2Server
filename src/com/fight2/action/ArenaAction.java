package com.fight2.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;

import com.fight2.dao.ArenaDao;
import com.fight2.dao.ArenaRankingDao;
import com.fight2.dao.PartyDao;
import com.fight2.dao.PartyGridDao;
import com.fight2.dao.PartyInfoDao;
import com.fight2.dao.UserDao;
import com.fight2.model.Arena;
import com.fight2.model.ArenaStatus;
import com.fight2.model.BaseEntity;
import com.fight2.model.PartyInfo;
import com.fight2.model.User;
import com.fight2.service.BattleService;
import com.fight2.util.HibernateUtils;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/arena")
public class ArenaAction extends BaseAction {
    private static final long serialVersionUID = -4473064014262040889L;
    @Autowired
    private ArenaDao arenaDao;
    @Autowired
    private ArenaRankingDao arenaRankingDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PartyInfoDao partyInfoDao;
    @Autowired
    private PartyDao partyDao;
    @Autowired
    private PartyGridDao partyGridDao;

    @Autowired
    private TaskScheduler taskScheduler;
    private List<Arena> datas;
    private Arena arena;
    private int id;

    @Action(value = "competitors", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String getArenaCompetitors() {
        final User currentUser = (User) this.getSession().get(LOGIN_USER);
        final List<User> datas = userDao.list();
        final List<User> competitors = Lists.newArrayList();
        for (final User user : datas) {
            if (user.getId() != currentUser.getId() && !user.isDisabled()) {
                final User competitor = new User();
                competitor.setId(user.getId());
                competitor.setAvatar(user.getAvatar());
                competitor.setName(user.getName());
                competitors.add(competitor);
            }
        }
        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", new Gson().toJson(competitors));
        return SUCCESS;
    }

    @Action(value = "attack", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String attack() {
        final User attacker = (User) this.getSession().get(LOGIN_USER);
        final User defender = userDao.get(id);

        final PartyInfo attackerPartyInfo = partyInfoDao.getByUser(attacker);
        final PartyInfo defenderPartyInfo = partyInfoDao.getByUser(defender);

        final BattleService battleService = new BattleService(attacker, defender, attackerPartyInfo, defenderPartyInfo);
        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", new Gson().toJson(battleService.fight()));
        return SUCCESS;
    }

    @Action(value = "list", results = { @Result(name = SUCCESS, location = "arena_list.ftl") })
    public String list() {
        datas = arenaDao.list();
        return SUCCESS;
    }

    @Action(value = "add", results = { @Result(name = SUCCESS, location = "arena_form.ftl") })
    public String add() {
        return SUCCESS;
    }

    @Action(value = "edit", results = { @Result(name = SUCCESS, location = "arena_form.ftl") })
    public String edit() {
        arena = arenaDao.get(id);
        return SUCCESS;
    }

    @Action(value = "save", interceptorRefs = { @InterceptorRef("tokenSession"), @InterceptorRef("defaultStack") }, results = { @Result(
            name = SUCCESS, location = "list", type = "redirect") })
    public String save() {
        if (arena.getId() == BaseEntity.EMPTY_ID) {
            return createSave();
        } else {
            return editSave();
        }
    }

    private String createSave() {
        arena.setStatus(ArenaStatus.Scheduled);
        arenaDao.add(arena);
        final Runnable startSchedule = createStartSchedule(arena.getId());
        taskScheduler.schedule(startSchedule, arena.getStartDate());
        final Runnable stopSchedule = createStopSchedule(arena.getId());
        taskScheduler.schedule(stopSchedule, arena.getEndDate());
        return SUCCESS;
    }

    private String editSave() {
        arenaDao.update(arena);
        return SUCCESS;
    }

    @Action(value = "delete", results = { @Result(name = SUCCESS, location = "list", type = "redirect") })
    public String delete() {
        arena = arenaDao.load(id);
        arenaDao.delete(arena);
        return SUCCESS;
    }

    @Action(value = "cancel", results = { @Result(name = SUCCESS, location = "list", type = "redirect") })
    public String cancel() {
        arena = arenaDao.get(id);
        arena.setStatus(ArenaStatus.Cancelled);
        arenaDao.update(arena);
        return SUCCESS;
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

    public Arena getArena() {
        return arena;
    }

    public void setArena(final Arena arena) {
        this.arena = arena;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }

    public List<Arena> getDatas() {
        return datas;
    }

    public void setDatas(final List<Arena> datas) {
        this.datas = datas;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public PartyInfoDao getPartyInfoDao() {
        return partyInfoDao;
    }

    public void setPartyInfoDao(final PartyInfoDao partyInfoDao) {
        this.partyInfoDao = partyInfoDao;
    }

    public PartyDao getPartyDao() {
        return partyDao;
    }

    public void setPartyDao(final PartyDao partyDao) {
        this.partyDao = partyDao;
    }

    public PartyGridDao getPartyGridDao() {
        return partyGridDao;
    }

    public void setPartyGridDao(final PartyGridDao partyGridDao) {
        this.partyGridDao = partyGridDao;
    }

    public ArenaDao getArenaDao() {
        return arenaDao;
    }

    public void setArenaDao(final ArenaDao arenaDao) {
        this.arenaDao = arenaDao;
    }

    public ArenaRankingDao getArenaRankingDao() {
        return arenaRankingDao;
    }

    public void setArenaRankingDao(final ArenaRankingDao arenaRankingDao) {
        this.arenaRankingDao = arenaRankingDao;
    }

    public TaskScheduler getTaskScheduler() {
        return taskScheduler;
    }

    public void setTaskScheduler(final TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

}
