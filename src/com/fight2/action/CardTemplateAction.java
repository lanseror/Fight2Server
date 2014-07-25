package com.fight2.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.CardTemplateDao;
import com.fight2.model.BaseEntity;
import com.fight2.model.CardTemplate;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@Namespace("/card-template")
public class CardTemplateAction extends ActionSupport {
    private static final long serialVersionUID = -4473064014262040889L;
    @Autowired
    private CardTemplateDao cardTemplateDao;
    private CardTemplate cardTemplate;
    private List<CardTemplate> datas;
    private int id;

    @Action(value = "list", results = { @Result(name = SUCCESS, location = "card_template_list.ftl") })
    public String list() {
        datas = cardTemplateDao.list();
        return SUCCESS;
    }

    @Action(value = "list-json", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String listJson() {
        final ActionContext context = ActionContext.getContext();
        final List<CardTemplate> list = cardTemplateDao.list();
        context.put("jsonMsg", new Gson().toJson(list));
        return SUCCESS;
    }

    @Action(value = "add", results = { @Result(name = SUCCESS, location = "card_template_form.ftl") })
    public String add() {
        return SUCCESS;
    }

    @Action(value = "edit", results = { @Result(name = SUCCESS, location = "card_template_form.ftl") })
    public String edit() {
        cardTemplate = cardTemplateDao.get(id);
        return SUCCESS;
    }
    
    @Action(value = "delete", results = { @Result(name = SUCCESS, location = "list", type = "redirect") })
    public String delete() {
        cardTemplate = cardTemplateDao.get(id);
        cardTemplateDao.delete(cardTemplate);
        return SUCCESS;
    }

    @Action(value = "save", interceptorRefs = { @InterceptorRef("tokenSession"), @InterceptorRef("defaultStack") }, results = { @Result(
            name = SUCCESS, location = "list", type = "redirect") })
    public String save() {
        if (cardTemplate.getId() == BaseEntity.EMPTY_ID) {
            return createSave();
        } else {
            return editSave();
        }
    }

    private String createSave() {
        cardTemplateDao.add(cardTemplate);
        return SUCCESS;
    }

    private String editSave() {
        cardTemplateDao.update(cardTemplate);
        return SUCCESS;
    }

    public CardTemplateDao getCardTemplateDao() {
        return cardTemplateDao;
    }

    public void setCardTemplateDao(final CardTemplateDao cardTemplateDao) {
        this.cardTemplateDao = cardTemplateDao;
    }

    public CardTemplate getCardTemplate() {
        return cardTemplate;
    }

    public void setCardTemplate(final CardTemplate cardTemplate) {
        this.cardTemplate = cardTemplate;
    }

    public List<CardTemplate> getDatas() {
        return datas;
    }

    public void setDatas(final List<CardTemplate> datas) {
        this.datas = datas;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

}
