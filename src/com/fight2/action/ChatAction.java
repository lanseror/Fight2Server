package com.fight2.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.fight2.model.ChatMessage;
import com.fight2.model.User;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;

@Namespace("/chat")
public class ChatAction extends BaseAction {
    private static final long serialVersionUID = 5916134694406462553L;
    private static final int MAX_MSG_SIZE = 1000;
    private static final Map<Integer, List<ChatMessage>> MSG_DATA = Maps.newConcurrentMap();

    private String msg;
    private int index;

    @Action(value = "send", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String send() {
        final User user = getLoginUser();
        final Calendar calendar = Calendar.getInstance();
        final int dateKey = calendar.get(Calendar.DAY_OF_MONTH);
        final DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        final String dateString = dateFormat.format(calendar.getTime());
        final List<ChatMessage> messages = MSG_DATA.containsKey(dateKey) ? MSG_DATA.get(dateKey) : newDayMessage(calendar, dateKey);
        final ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(user.getName());
        chatMessage.setContent(msg);
        chatMessage.setDate(dateString);
        messages.add(chatMessage);
        final ActionContext context = ActionContext.getContext();
        context.put("jsonMsg", "ok");
        return SUCCESS;
    }

    @Action(value = "get", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String get() {
        final Map<String, Object> data = Maps.newHashMap();
        final ActionContext context = ActionContext.getContext();
        final Calendar calendar = Calendar.getInstance();
        final int dateKey = calendar.get(Calendar.DAY_OF_MONTH);
        final List<ChatMessage> messages = MSG_DATA.containsKey(dateKey) ? MSG_DATA.get(dateKey) : previousDayMessage(calendar);
        final int msgSize = messages.size();
        if (msgSize == 0 || index < -1) {
            data.put("index", -1);
            data.put("msg", Collections.EMPTY_LIST);
        } else if (index < msgSize - MAX_MSG_SIZE) {
            final List<ChatMessage> subMessages = msgSize > MAX_MSG_SIZE ? messages.subList(msgSize - MAX_MSG_SIZE, msgSize) : messages;
            data.put("index", msgSize - 1);
            data.put("msg", subMessages);
        } else if (index < msgSize - 1) {
            final List<ChatMessage> subMessages = messages.subList(index + 1, msgSize);
            data.put("index", msgSize - 1);
            data.put("msg", subMessages);
        } else {
            data.put("index", msgSize - 1);
            data.put("msg", Collections.EMPTY_LIST);
        }
        context.put("jsonMsg", new Gson().toJson(data));
        return SUCCESS;
    }

    private static synchronized List<ChatMessage> previousDayMessage(final Calendar calendar) {
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        final int oldDate = calendar.get(Calendar.DAY_OF_MONTH);
        return MSG_DATA.containsKey(oldDate) ? MSG_DATA.get(oldDate) : new ArrayList<ChatMessage>();
    }

    /**
     * 
     * We will only keep 2 days messages, while insert new day messages, remove the messages before yesterday.
     * 
     * @param calendar
     * @param date
     * @return
     */
    private static synchronized List<ChatMessage> newDayMessage(final Calendar calendar, final int date) {
        final List<ChatMessage> messages = Lists.newArrayList();
        MSG_DATA.put(date, messages);
        calendar.add(Calendar.DAY_OF_MONTH, -2);
        final int oldDate = calendar.get(Calendar.DAY_OF_MONTH);
        MSG_DATA.remove(oldDate);
        return messages;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(final String msg) {
        this.msg = msg;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(final int index) {
        this.index = index;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
