package com.fight2.action;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
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
import com.fight2.model.ArenaRanking;
import com.fight2.model.ArenaStatus;
import com.fight2.model.BaseEntity;
import com.fight2.model.BattleResult;
import com.fight2.model.PartyInfo;
import com.fight2.model.User;
import com.fight2.model.UserArenaInfo;
import com.fight2.model.UserArenaRecord;
import com.fight2.model.UserArenaRecord.UserArenaRecordStatus;
import com.fight2.model.json.ArenaJson;
import com.fight2.service.BattleService;
import com.fight2.util.ArenaUtils;
import com.fight2.util.DateUtils;
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
    private int index;

    private static final int[] MIGHTS = { 10, 8, 5, 2 };

    @Action(value = "enter", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String enter() {
        final User currentUser = (User) this.getSession().get(LOGIN_USER);
        final Arena arena = arenaDao.load(id);
        final int userId = currentUser.getId();
        final HttpServletRequest request = ServletActionContext.getRequest();
        ArenaUtils.enter(id, request.getSession(), userId);

        ArenaRanking arenaRanking = arenaRankingDao.getByUserArena(currentUser, arena);
        if (arenaRanking == null) {
            arenaRanking = new ArenaRanking();
            arenaRanking.setUser(currentUser);
            arenaRanking.setArena(arena);
            arenaRankingDao.add(arenaRanking);
        }
        final UserArenaInfo userArenaInfo = ArenaUtils.getUserArenaInfo(id, userId);
        userArenaInfo.setLose(arenaRanking.getLose());
        userArenaInfo.setMight(arenaRanking.getMight());
        userArenaInfo.setWin(arenaRanking.getWin());
        userArenaInfo.setRankNumber(arenaRanking.getRankNumber());
        userArenaInfo.setRemainTime(DateUtils.getRemainTime(arena.getEndDate()));
        final List<UserArenaRecord> arenaRecords = userArenaInfo.getArenaRecords();
        if (arenaRecords.isEmpty()) {
            refreshArenaRecords(arenaRanking, arenaRecords);
        }

        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", new Gson().toJson(userArenaInfo));
        return SUCCESS;
    }

    @Action(value = "ranking", results = { @Result(name = SUCCESS, location = "arena_ranking.ftl") })
    public String ranking() {
        final List<ArenaRanking> rankings = arenaRankingDao.list();
        final ActionContext context = ActionContext.getContext();
        context.put("ranks", rankings);
        return SUCCESS;
    }

    private void refreshArenaRecords(final ArenaRanking arenaRanking, final List<UserArenaRecord> arenaRecords) {
        final Random random = new Random();
        arenaRecords.clear();
        final Arena arena = arenaRanking.getArena();
        final int rank = arenaRanking.getRankNumber();
        final int maxRank = arenaRankingDao.getArenaMaxRank(arenaRanking.getArena());

        // Rank == 0 means the user is the first time enter into the arena, give him all NPC.
        if (rank == 0) {
            for (int i = 0; i < 3; i++) {
                final UserArenaRecord arenaRecord = new UserArenaRecord();
                arenaRecord.setStatus(UserArenaRecordStatus.NoAction);
                arenaRecord.setUser(toArenaJsonUser(randomNPC()));
                arenaRecords.add(arenaRecord);
            }
            return;
        }

        // Get first position
        // TODO: handle the situation when rank = 1;
        final UserArenaRecord p1ArenaRecord = new UserArenaRecord();
        p1ArenaRecord.setStatus(UserArenaRecordStatus.NoAction);
        int p1RankNum = 0;
        if (rank == 1) {
            p1RankNum = rank + random.nextInt(5) + 1;
        } else {
            p1RankNum = random.nextInt(rank);
        }
        if (p1RankNum > maxRank || p1RankNum <= 0) {
            p1ArenaRecord.setUser(toArenaJsonUser(randomNPC()));
        } else {
            final ArenaRanking p1Ranking = arenaRankingDao.getByArenaRank(arena, p1RankNum);
            final User p1User = p1Ranking.getUser();
            p1ArenaRecord.setUser(toArenaJsonUser(p1User));
        }
        arenaRecords.add(p1ArenaRecord);
        // Get second position
        final UserArenaRecord p2ArenaRecord = new UserArenaRecord();
        p2ArenaRecord.setStatus(UserArenaRecordStatus.NoAction);
        final int p2RankNumSeed = 5 - random.nextInt(11);
        final int p2RankNum = rank + p2RankNumSeed;
        if (p2RankNum == rank || p2RankNum == p1RankNum || p2RankNum > maxRank || p2RankNum <= 0) {
            p2ArenaRecord.setUser(toArenaJsonUser(randomNPC()));
        } else {
            final ArenaRanking p2Ranking = arenaRankingDao.getByArenaRank(arena, p2RankNum);
            final User p2User = p2Ranking.getUser();
            p2ArenaRecord.setUser(toArenaJsonUser(p2User));
        }
        arenaRecords.add(p2ArenaRecord);
        // Get third position
        final UserArenaRecord p3ArenaRecord = new UserArenaRecord();
        p3ArenaRecord.setStatus(UserArenaRecordStatus.NoAction);
        if (rank == maxRank) {
            p3ArenaRecord.setUser(toArenaJsonUser(randomNPC()));
        } else {
            final int p3RankNum = rank + random.nextInt(maxRank - rank) + 1;
            if (p3RankNum == p2RankNum) {
                p3ArenaRecord.setUser(toArenaJsonUser(randomNPC()));
            } else {
                final ArenaRanking p3Ranking = arenaRankingDao.getByArenaRank(arena, p3RankNum);
                final User p3User = p3Ranking.getUser();
                p3ArenaRecord.setUser(toArenaJsonUser(p3User));
            }
        }
        arenaRecords.add(p3ArenaRecord);

    }

    private User randomNPC() {
        final Random random = new Random();
        final List<User> npcs = userDao.getAllNpc();
        return npcs.get(random.nextInt(npcs.size()));
    }

    private User toArenaJsonUser(final User user) {
        final User jsonUser = new User();
        jsonUser.setId(user.getId());
        jsonUser.setAvatar(user.getAvatar());
        jsonUser.setName(user.getName());
        jsonUser.setNpc(user.isNpc());
        return jsonUser;
    }

    @Action(value = "exit", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String exit() {
        final HttpServletRequest request = ServletActionContext.getRequest();
        ArenaUtils.exit(id, request.getSession());
        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", new Gson().toJson("ok"));
        return SUCCESS;
    }

    @Action(value = "attack", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String attack() {
        final User attacker = (User) this.getSession().get(LOGIN_USER);
        final UserArenaInfo userArenaInfo = ArenaUtils.getUserArenaInfo(id, attacker.getId());
        final List<UserArenaRecord> arenaRecords = userArenaInfo.getArenaRecords();
        final UserArenaRecord arenaRecord = arenaRecords.get(index);
        if (arenaRecord.getStatus() != UserArenaRecordStatus.NoAction) {
            return INPUT;
        }
        final User defender = userDao.get(arenaRecord.getUser().getId());

        final PartyInfo attackerPartyInfo = partyInfoDao.getByUser(attacker);
        final PartyInfo defenderPartyInfo = partyInfoDao.getByUser(defender);

        final BattleService battleService = new BattleService(attacker, defender, attackerPartyInfo, defenderPartyInfo);
        final BattleResult battleResult = battleService.fight();
        final int arenaId = ArenaUtils.getEnteredArena(attacker.getId());
        final Arena arena = arenaDao.load(arenaId);
        final ArenaRanking arenaRanking = arenaRankingDao.getByUserArena(attacker, arena);
        if (battleResult.isWinner()) {
            arenaRanking.setWin(arenaRanking.getWin() + 1);
            arenaRecord.setStatus(UserArenaRecordStatus.Win);
            final int winMight = MIGHTS[index];
            arenaRanking.setMight(arenaRanking.getMight() + winMight);
        } else {
            arenaRanking.setLose(arenaRanking.getLose() + 1);
            final int loseMight = MIGHTS[3];
            arenaRanking.setMight(arenaRanking.getMight() + loseMight);
            arenaRecord.setStatus(UserArenaRecordStatus.Lose);
        }
        arenaRankingDao.update(arenaRanking);

        int remainNoAction = 0;
        for (final UserArenaRecord arenaRecordTmp : arenaRecords) {
            if (arenaRecordTmp.getStatus() == UserArenaRecordStatus.NoAction) {
                remainNoAction++;
            }
        }
        if (remainNoAction == 0) {
            refreshArenaRecords(arenaRanking, arenaRecords);
        }

        // Sort and update ranking, need to be re-factored
        final List<ArenaRanking> arenaRankingsPo = arenaRankingDao.listByArena(arena);
        final List<ArenaRanking> arenaRankings = Lists.newArrayList();
        for (final ArenaRanking arenaRankingPo : arenaRankingsPo) {
            final ArenaRanking arenaRankingVo = new ArenaRanking();
            arenaRankingVo.setId(arenaRankingPo.getId());
            arenaRankingVo.setMight(arenaRankingPo.getMight());
            arenaRankings.add(arenaRankingVo);
        }
        Collections.sort(arenaRankings, new Comparator<ArenaRanking>() {
            @Override
            public int compare(final ArenaRanking o1, final ArenaRanking o2) {
                return o2.getMight() - o1.getMight();
            }

        });
        for (int i = 0; i < arenaRankings.size(); i++) {
            final ArenaRanking arenaRankingSort = arenaRankings.get(i);
            final ArenaRanking arenaRankingSortUpdate = arenaRankingDao.get(arenaRankingSort.getId());
            arenaRankingSortUpdate.setRankNumber(i + 1);
            arenaRankingDao.update(arenaRankingSortUpdate);
        }

        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", new Gson().toJson(battleResult));
        return SUCCESS;
    }

    @Action(value = "list", results = { @Result(name = SUCCESS, location = "arena_list.ftl") })
    public String list() {
        datas = arenaDao.list();
        return SUCCESS;
    }

    @Action(value = "list-started", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String getStartedArenas() {
        final List<Arena> arenas = arenaDao.getStartedArenas();
        final List<ArenaJson> arenaJsons = Lists.newArrayList();
        for (final Arena arena : arenas) {
            final ArenaJson arenaJson = new ArenaJson();
            arenaJson.setId(arena.getId());
            arenaJson.setName(arena.getName());
            arenaJson.setOnlineNumber(ArenaUtils.getOnlineNumber(arena.getId()));
            arenaJson.setRemainTime(DateUtils.getRemainTime(arena.getEndDate()));
            arenaJsons.add(arenaJson);
        }
        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", new Gson().toJson(arenaJsons));
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

    public int getIndex() {
        return index;
    }

    public void setIndex(final int index) {
        this.index = index;
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
