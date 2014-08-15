package com.fight2.action;

import java.util.Map;

import com.fight2.model.User;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport {
    private static final long serialVersionUID = -1813468590478293643L;
    protected static final String LOGIN_USER = "LoginUser";

    protected boolean isUserLogged() {
        final Map<String, Object> session = getSession();
        if (session.containsKey(LOGIN_USER)) {
            return true;
        } else {
            return false;
        }
    }

    protected Map<String, Object> getSession() {
        return ActionContext.getContext().getSession();
    }

    protected User getLoginUser() {
        final User sessionUser = (User) this.getSession().get(LOGIN_USER);
        return sessionUser;
    }
}
