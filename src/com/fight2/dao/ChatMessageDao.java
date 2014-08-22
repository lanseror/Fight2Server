package com.fight2.dao;

import java.util.List;

import com.fight2.model.ChatMessage;

public interface ChatMessageDao extends BaseDao<ChatMessage> {

    public List<ChatMessage> listFromId(int id, final int maxResults);

    public int getMaxId();

}
