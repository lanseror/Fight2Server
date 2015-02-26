package com.fight2.dao;

import java.util.List;

import com.fight2.model.Dialog;
import com.fight2.model.Dialog.OrderType;
import com.fight2.model.Dialog.Speaker;

public interface DialogDao extends BaseDao<Dialog> {
    public List<Dialog> listByType(OrderType orderType);

    public List<Dialog> listByTypeAndSpeakers(OrderType orderType, Speaker... speakers);
}
