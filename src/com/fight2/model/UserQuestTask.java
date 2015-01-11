package com.fight2.model;

import javax.persistence.Entity;

@Entity
public class UserQuestTask extends BaseEntity {
    private static final long serialVersionUID = -1159247933482212997L;

    private int taskId;

    private UserTaskStatus status;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(final int taskId) {
        this.taskId = taskId;
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
        Started,
        Finished;
    }

}
