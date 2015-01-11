package com.fight2.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class TaskRewardItem extends BaseEntity {
    private static final long serialVersionUID = -7485815241564723537L;
    private TaskReward taskReward;
    private TaskRewardItemType type;
    private int amount;
    private CardTemplate cardTemplate;
    private Card card; // Use for JSON

    @ManyToOne
    @JoinColumn(name = "task_reward_id")
    public TaskReward getTaskReward() {
        return taskReward;
    }

    public void setTaskReward(final TaskReward taskReward) {
        this.taskReward = taskReward;
    }

    public TaskRewardItemType getType() {
        return type;
    }

    public void setType(final TaskRewardItemType type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

    @ManyToOne
    @JoinColumn(name = "cardTemplate_id")
    public CardTemplate getCardTemplate() {
        return cardTemplate;
    }

    @Transient
    public Card getCard() {
        return card;
    }

    public void setCard(final Card card) {
        this.card = card;
    }

    public void setCardTemplate(final CardTemplate cardTemplate) {
        this.cardTemplate = cardTemplate;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public static enum TaskRewardItemType {
        ArenaTicket,
        Stamina,
        Card,
        GuildContribution;
    }

}
