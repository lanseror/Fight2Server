package com.fight2.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.UserDao;
import com.fight2.model.Skill;

@Namespace("/skill")
public class SkillAction extends BaseAction {
    private static final long serialVersionUID = -4473064014262040889L;
    @Autowired
    private UserDao userDao;
    private Skill skill;
    private List<Skill> datas;
    private int id;
    private int cardTemplateId;

    @Action(value = "add", results = { @Result(name = SUCCESS, location = "skill_form.ftl") })
    public String add() {
        return SUCCESS;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(final Skill skill) {
        this.skill = skill;
    }

    public List<Skill> getDatas() {
        return datas;
    }

    public void setDatas(final List<Skill> datas) {
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

    public int getCardTemplateId() {
        return cardTemplateId;
    }

    public void setCardTemplateId(final int cardTemplateId) {
        this.cardTemplateId = cardTemplateId;
    }

}
