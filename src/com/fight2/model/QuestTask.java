package com.fight2.model;

import javax.persistence.Entity;

@Entity
public class QuestTask extends BaseEntity {
    private static final long serialVersionUID = 6785271692837283507L;

    private String title;

    private String dialog;

    private String tips;

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

    public String getTips() {
        return tips;
    }

    public void setTips(final String tips) {
        this.tips = tips;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
