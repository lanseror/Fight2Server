package com.fight2.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class TaskReward extends BaseEntity {
    private static final long serialVersionUID = -6584879750459892301L;
    private QuestTask task;
    private List<TaskRewardItem> rewardItems;

    @ManyToOne
    @JoinColumn(name = "task_id")
    public QuestTask getTask() {
        return task;
    }

    public void setTask(final QuestTask task) {
        this.task = task;
    }

    @OneToMany(mappedBy = "taskReward", cascade = { CascadeType.REMOVE })
    public List<TaskRewardItem> getRewardItems() {
        return rewardItems;
    }

    public void setRewardItems(final List<TaskRewardItem> rewardItems) {
        this.rewardItems = rewardItems;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
