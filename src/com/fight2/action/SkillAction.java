package com.fight2.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.CardTemplateDao;
import com.fight2.dao.SkillDao;
import com.fight2.dao.SkillOperationDao;
import com.fight2.dao.UserDao;
import com.fight2.model.BaseEntity;
import com.fight2.model.CardTemplate;
import com.fight2.model.Skill;
import com.fight2.model.SkillApplyParty;
import com.fight2.model.SkillOperation;
import com.fight2.model.SkillPointType;

@Namespace("/skill")
public class SkillAction extends BaseAction {
    private static final long serialVersionUID = -4473064014262040889L;
    @Autowired
    private UserDao userDao;
    @Autowired
    private SkillDao skillDao;
    @Autowired
    private CardTemplateDao cardTemplateDao;
    @Autowired
    private SkillOperationDao skillOperationDao;
    private Skill skill;
    private List<Skill> datas;
    private int id;
    private int cardTemplateId;
    private int[] signs;
    private int[] points;
    private SkillPointType[] skillPointTypes;
    private SkillApplyParty[] skillApplyPartys;

    @Action(value = "add", results = { @Result(name = SUCCESS, location = "skill_form.ftl") })
    public String add() {
        return SUCCESS;
    }

    @Action(value = "edit", results = { @Result(name = SUCCESS, location = "skill_form.ftl") })
    public String edit() {
        skill = skillDao.get(id);
        return SUCCESS;
    }

    @Action(value = "save", interceptorRefs = { @InterceptorRef("tokenSession"), @InterceptorRef("defaultStack") }, results = { @Result(
            name = SUCCESS, location = "/card-template/edit.action", type = "redirect", params = { "id", "${cardTemplateId}" }) })
    public String save() {
        if (skill.getId() == BaseEntity.EMPTY_ID) {
            return createSave();
        } else {
            return editSave();
        }
    }

    private String createSave() {
        final CardTemplate cardTemplate = cardTemplateDao.get(cardTemplateId);
        skill.setCardTemplate(cardTemplate);
        skillDao.add(skill);
        for (int i = 0; i < points.length; i++) {
            final SkillOperation operation = new SkillOperation();
            final int sign = signs[i];
            final int point = points[i];
            final SkillPointType skillPointType = skillPointTypes[i];
            final SkillApplyParty skillApplyParty = skillApplyPartys[i];
            operation.setSign(sign);
            operation.setPoint(point);
            operation.setSkillPointType(skillPointType);
            operation.setSkillApplyParty(skillApplyParty);
            operation.setSkill(skill);
            skillOperationDao.add(operation);
        }
        return SUCCESS;
    }

    private String editSave() {
        final CardTemplate cardTemplate = cardTemplateDao.get(cardTemplateId);
        skill.setCardTemplate(cardTemplate);
        skillDao.update(skill);
        for (int i = 0; i < points.length; i++) {
            final SkillOperation operation = new SkillOperation();
            final int sign = signs[i];
            final int point = points[i];
            final SkillPointType skillPointType = skillPointTypes[i];
            final SkillApplyParty skillApplyParty = skillApplyPartys[i];
            operation.setSign(sign);
            operation.setPoint(point);
            operation.setSkillPointType(skillPointType);
            operation.setSkillApplyParty(skillApplyParty);
            operation.setSkill(skill);
            skillOperationDao.add(operation);
        }

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

    public int[] getSigns() {
        return signs;
    }

    public void setSigns(final int[] signs) {
        this.signs = signs;
    }

    public int[] getPoints() {
        return points;
    }

    public void setPoints(final int[] points) {
        this.points = points;
    }

    public SkillPointType[] getSkillPointTypes() {
        return skillPointTypes;
    }

    public void setSkillPointTypes(final SkillPointType[] skillPointTypes) {
        this.skillPointTypes = skillPointTypes;
    }

    public SkillApplyParty[] getSkillApplyPartys() {
        return skillApplyPartys;
    }

    public void setSkillApplyPartys(final SkillApplyParty[] skillApplyPartys) {
        this.skillApplyPartys = skillApplyPartys;
    }

    public SkillDao getSkillDao() {
        return skillDao;
    }

    public void setSkillDao(final SkillDao skillDao) {
        this.skillDao = skillDao;
    }

    public CardTemplateDao getCardTemplateDao() {
        return cardTemplateDao;
    }

    public void setCardTemplateDao(final CardTemplateDao cardTemplateDao) {
        this.cardTemplateDao = cardTemplateDao;
    }

    public SkillOperationDao getSkillOperationDao() {
        return skillOperationDao;
    }

    public void setSkillOperationDao(final SkillOperationDao skillOperationDao) {
        this.skillOperationDao = skillOperationDao;
    }

}
