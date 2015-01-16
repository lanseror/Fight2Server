package com.fight2.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class UserQuestTask extends BaseEntity {
    private static final long serialVersionUID = -1159247933482212997L;

    private QuestTask task;

    private User user;

    private UserTaskStatus status = UserTaskStatus.Ready;

    public UserQuestTask() {

    }

    public UserQuestTask(final UserQuestTask userQuestTask) {
        super(userQuestTask);
        this.task = new QuestTask(userQuestTask.getTask());
        this.status = userQuestTask.getStatus();
    }

    @ManyToOne
    @JoinColumn(name = "task_id")
    public QuestTask getTask() {
        return task;
    }

    public void setTask(final QuestTask task) {
        this.task = task;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public UserTaskStatus getStatus() {
        return status;
    }

    public void setStatus(final UserTaskStatus status) {
        this.status = status;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public enum UserTaskStatus {
        Ready,
        Started,
        Finished;
    }

}
