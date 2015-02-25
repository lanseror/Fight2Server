package com.fight2.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.DialogDao;
import com.fight2.dao.UserDao;
import com.fight2.model.BaseEntity;
import com.fight2.model.Dialog;

@Namespace("/dialog")
public class DialogAction extends BaseAction {
    private static final long serialVersionUID = 1514171827851300722L;
    @Autowired
    private DialogDao dialogDao;
    @Autowired
    private UserDao userDao;
    private List<Dialog> datas;
    private Dialog dialog;
    private int id;

    @Action(value = "list", results = { @Result(name = SUCCESS, location = "dialog_list.ftl") })
    public String list() {
        datas = dialogDao.list();
        return SUCCESS;
    }

    @Action(value = "add", results = { @Result(name = SUCCESS, location = "dialog_form.ftl") })
    public String add() {
        return SUCCESS;
    }

    @Action(value = "edit", results = { @Result(name = SUCCESS, location = "dialog_form.ftl") })
    public String edit() {
        dialog = dialogDao.get(id);
        return SUCCESS;
    }

    @Action(value = "save", interceptorRefs = { @InterceptorRef("tokenSession"), @InterceptorRef("defaultStack") }, results = { @Result(
            name = SUCCESS, location = "list", type = "redirect") })
    public String save() {
        if (dialog.getId() == BaseEntity.EMPTY_ID) {
            return createSave();
        } else {
            return editSave();
        }
    }

    private String createSave() {
        dialogDao.add(dialog);
        return SUCCESS;
    }

    private String editSave() {
        dialogDao.update(dialog);
        return SUCCESS;
    }

    public List<Dialog> getDatas() {
        return datas;
    }

    public void setDatas(final List<Dialog> datas) {
        this.datas = datas;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(final Dialog dialog) {
        this.dialog = dialog;
    }

    public DialogDao getDialogDao() {
        return dialogDao;
    }

    public void setDialogDao(final DialogDao dialogDao) {
        this.dialogDao = dialogDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
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

}
