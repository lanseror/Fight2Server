package com.fight2.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class QuestTask extends BaseEntity {
    private static final long serialVersionUID = 6785271692837283507L;

    private String title;

    private String dialog;

    private String bossDialog;

    private String tips;

    private int x;

    private int y;

    private User boss;

    public QuestTask() {

    }

    public QuestTask(final QuestTask questTask) {
        super(questTask);
        this.title = questTask.getTitle();
        this.dialog = questTask.getDialog();
        this.bossDialog = questTask.getBossDialog();
        this.tips = questTask.getTips();
        this.x = questTask.getX();
        this.y = questTask.getY();
    }

    @ManyToOne
    @JoinColumn(name = "boss_id")
    public User getBoss() {
        return boss;
    }

    public void setBoss(final User boss) {
        this.boss = boss;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDialog() {
        return dialog;
    }

    public void setDialog(final String dialog) {
        this.dialog = dialog;
    }

    public String getBossDialog() {
        return bossDialog;
    }

    public void setBossDialog(final String bossDialog) {
        this.bossDialog = bossDialog;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(final String tips) {
        this.tips = tips;
    }

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
