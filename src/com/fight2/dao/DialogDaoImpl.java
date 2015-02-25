package com.fight2.dao;

import org.springframework.stereotype.Repository;

import com.fight2.model.Dialog;

@Repository
public class DialogDaoImpl extends BaseDaoImpl<Dialog> implements DialogDao {

    public DialogDaoImpl() {
        super(Dialog.class);
    }

}
