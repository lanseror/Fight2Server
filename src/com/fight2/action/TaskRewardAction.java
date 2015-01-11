package com.fight2.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.CardImageDao;
import com.fight2.dao.CardTemplateDao;
import com.fight2.dao.QuestTaskDao;
import com.fight2.dao.TaskRewardDao;
import com.fight2.dao.TaskRewardItemDao;
import com.fight2.model.BaseEntity;
import com.fight2.model.Card;
import com.fight2.model.CardImage;
import com.fight2.model.CardTemplate;
import com.fight2.model.QuestTask;
import com.fight2.model.TaskReward;
import com.fight2.model.TaskRewardItem;
import com.fight2.model.TaskRewardItem.TaskRewardItemType;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/task-reward")
public class TaskRewardAction extends BaseAction {
    private static final long serialVersionUID = -8367061939120159572L;
    @Autowired
    private TaskRewardDao taskRewardDao;
    @Autowired
    private TaskRewardItemDao taskRewardItemDao;
    @Autowired
    private QuestTaskDao questTaskDao;
    @Autowired
    private CardImageDao cardImageDao;
    @Autowired
    private CardTemplateDao cardTemplateDao;
    private List<Card> cards;
    private List<TaskReward> datas;
    private TaskReward taskReward;
    private QuestTask questTask;
    private int id;
    private int taskId;
    private TaskRewardItemType[] rewardItemTypes;
    private int[] rewardItemAmounts;
    private int[] cardIds;

    @Action(value = "add", results = { @Result(name = SUCCESS, location = "task_reward_form.ftl") })
    public String add() {
        loadCardData();
        return SUCCESS;
    }

    @Action(value = "edit", results = { @Result(name = SUCCESS, location = "task_reward_form.ftl") })
    public String edit() {
        taskReward = taskRewardDao.get(id);
        taskId = taskReward.getTask().getId();
        loadCardData();
        return SUCCESS;
    }

    private void loadCardData() {
        final List<CardTemplate> cardTemplates = cardTemplateDao.list();
        cards = Lists.newArrayList();
        for (final CardTemplate cardTemplate : cardTemplates) {
            final Card card = new Card();
            card.setId(cardTemplate.getId());
            card.setName(cardTemplate.getName());
            final CardImage avatarObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_AVATAR, 1, cardTemplate);
            card.setAvatar(avatarObj.getUrl());
            cards.add(card);
        }
    }

    @Action(value = "delete", results = { @Result(name = SUCCESS, location = "list-by-task", type = "redirect", params = { "taskId", "${taskId}" }) })
    public String delete() {
        taskReward = taskRewardDao.get(id);
        taskId = taskReward.getTask().getId();
        taskRewardDao.delete(taskReward);
        return SUCCESS;
    }

    @Action(value = "save", interceptorRefs = { @InterceptorRef("tokenSession"), @InterceptorRef("defaultStack") }, results = { @Result(
            name = SUCCESS, location = "list-by-task", type = "redirect", params = { "taskId", "${taskId}" }) })
    public String save() {
        if (taskReward.getId() == BaseEntity.EMPTY_ID) {
            return createSave();
        } else {
            return editSave();
        }
    }

    private String createSave() {
        final QuestTask task = questTaskDao.load(taskId);
        taskReward.setTask(task);
        taskRewardDao.add(taskReward);
        saveRewardItems(taskReward);
        return SUCCESS;
    }

    private void saveRewardItems(final TaskReward taskReward) {
        int cardIdIndex = 0;
        for (int i = 0; i < rewardItemTypes.length; i++) {
            final TaskRewardItemType rewardItemType = rewardItemTypes[i];
            final int amount = rewardItemAmounts[i];
            final TaskRewardItem taskRewardItem = new TaskRewardItem();
            taskRewardItem.setTaskReward(taskReward);
            taskRewardItem.setAmount(amount);
            taskRewardItem.setType(rewardItemType);
            if (rewardItemType == TaskRewardItemType.Card) {
                final int cardId = cardIds[cardIdIndex++];
                final CardTemplate cardTemplate = cardTemplateDao.load(cardId);
                taskRewardItem.setCardTemplate(cardTemplate);
            }
            taskRewardItemDao.add(taskRewardItem);
        }
    }

    private String editSave() {
        final TaskReward taskRewardPo = taskRewardDao.load(taskReward.getId());
        // Delete all items first.
        final List<TaskRewardItem> rewardItems = taskRewardPo.getRewardItems();
        for (final TaskRewardItem rewardItem : rewardItems) {
            taskRewardItemDao.delete(rewardItem);
        }
        // Then save new items.
        saveRewardItems(taskReward);
        return SUCCESS;
    }

    @Action(value = "list-by-task", results = { @Result(name = SUCCESS, location = "task_reward_list.ftl") })
    public String listByTask() {
        questTask = questTaskDao.load(taskId);
        datas = taskRewardDao.listByTask(questTask);
        return SUCCESS;
    }

    @Action(value = "list-json", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String listJson() {
        questTask = questTaskDao.load(taskId);
        datas = taskRewardDao.listByTask(questTask);
        final List<TaskReward> rewards = Lists.newArrayList();
        for (final TaskReward data : datas) {
            final TaskReward taskReward = new TaskReward();
            taskReward.setId(data.getId());
            final List<TaskRewardItem> rewardItems = Lists.newArrayList();
            for (final TaskRewardItem rewardItemTemp : data.getRewardItems()) {
                final TaskRewardItem rewardItem = new TaskRewardItem();
                rewardItem.setId(rewardItemTemp.getId());
                rewardItem.setAmount(rewardItemTemp.getAmount());
                rewardItem.setType(rewardItemTemp.getType());
                final CardTemplate cardTemplate = rewardItemTemp.getCardTemplate();
                if (cardTemplate != null) {
                    final CardImage thumbObj = cardImageDao.getByTypeTierAndCardTemplate(CardImage.TYPE_THUMB, 1, cardTemplate);
                    final Card card = new Card();
                    card.setAtk(cardTemplate.getAtk());
                    card.setHp(cardTemplate.getHp());
                    card.setName(cardTemplate.getName());
                    card.setStar(cardTemplate.getStar());
                    card.setImage(thumbObj.getUrl());
                    card.setRace(cardTemplate.getRace());
                    rewardItem.setCard(card);
                }
                rewardItems.add(rewardItem);
            }
            taskReward.setRewardItems(rewardItems);
            rewards.add(taskReward);
        }

        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", new Gson().toJson(rewards));
        return SUCCESS;
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

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(final List<Card> cards) {
        this.cards = cards;
    }

    public CardImageDao getCardImageDao() {
        return cardImageDao;
    }

    public void setCardImageDao(final CardImageDao cardImageDao) {
        this.cardImageDao = cardImageDao;
    }

    public CardTemplateDao getCardTemplateDao() {
        return cardTemplateDao;
    }

    public void setCardTemplateDao(final CardTemplateDao cardTemplateDao) {
        this.cardTemplateDao = cardTemplateDao;
    }

    public QuestTask getQuestTask() {
        return questTask;
    }

    public void setQuestTask(final QuestTask questTask) {
        this.questTask = questTask;
    }

    public TaskRewardItemType[] getRewardItemTypes() {
        return rewardItemTypes;
    }

    public void setRewardItemTypes(final TaskRewardItemType[] rewardItemTypes) {
        this.rewardItemTypes = rewardItemTypes;
    }

    public List<TaskReward> getDatas() {
        return datas;
    }

    public int[] getRewardItemAmounts() {
        return rewardItemAmounts;
    }

    public void setRewardItemAmounts(final int[] rewardItemAmounts) {
        this.rewardItemAmounts = rewardItemAmounts;
    }

    public int[] getCardIds() {
        return cardIds;
    }

    public void setCardIds(final int[] cardIds) {
        this.cardIds = cardIds;
    }

    public TaskRewardDao getTaskRewardDao() {
        return taskRewardDao;
    }

    public void setTaskRewardDao(final TaskRewardDao taskRewardDao) {
        this.taskRewardDao = taskRewardDao;
    }

    public TaskRewardItemDao getTaskRewardItemDao() {
        return taskRewardItemDao;
    }

    public void setTaskRewardItemDao(final TaskRewardItemDao taskRewardItemDao) {
        this.taskRewardItemDao = taskRewardItemDao;
    }

    public QuestTaskDao getQuestTaskDao() {
        return questTaskDao;
    }

    public void setQuestTaskDao(final QuestTaskDao questTaskDao) {
        this.questTaskDao = questTaskDao;
    }

    public TaskReward getTaskReward() {
        return taskReward;
    }

    public void setTaskReward(final TaskReward taskReward) {
        this.taskReward = taskReward;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(final int taskId) {
        this.taskId = taskId;
    }

    public void setDatas(final List<TaskReward> datas) {
        this.datas = datas;
    }

}
