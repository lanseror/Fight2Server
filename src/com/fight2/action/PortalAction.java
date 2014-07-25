package com.fight2.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("/portal")
public class PortalAction extends ActionSupport {
	private static final long serialVersionUID = 1L;

	@Action(value = "list", results = { @Result(name = SUCCESS, location = "../module_list.ftl") })
    public String list() {
        return SUCCESS;
    }

}
