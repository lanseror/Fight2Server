package com.fight2.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.fight2.dao.VideoDao;
import com.fight2.model.BaseEntity;
import com.fight2.model.Video;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@Namespace("/video")
public class VideoAction extends ActionSupport {
    private static final long serialVersionUID = 6875466947374614454L;
    @Autowired
    private VideoDao videoDao;
    private Video video;
    private List<Video> datas;
    private int id;

    @Action(value = "list", results = { @Result(name = SUCCESS, location = "video_list.ftl") })
    public String list() {
        datas = videoDao.list();
        return SUCCESS;
    }

    @Action(value = "list-json", results = { @Result(name = SUCCESS, location = "../jsonMsg.ftl") })
    public String listJson() {
        final ActionContext context = ActionContext.getContext();
        final List<Video> list = videoDao.list();
        context.put("jsonMsg", new Gson().toJson(list));
        return SUCCESS;
    }

    @Action(value = "add", results = { @Result(name = SUCCESS, location = "video_form.ftl") })
    public String add() {
        return SUCCESS;
    }

    @Action(value = "edit", results = { @Result(name = SUCCESS, location = "video_form.ftl") })
    public String edit() {
        video = videoDao.get(id);
        return SUCCESS;
    }

    @Action(value = "save", interceptorRefs = { @InterceptorRef("tokenSession"), @InterceptorRef("defaultStack") }, results = { @Result(
            name = SUCCESS, location = "list", type = "redirect") })
    public String save() {
        if (video.getId() == BaseEntity.EMPTY_ID) {
            return createSave();
        } else {
            return editSave();
        }
    }

    private String createSave() {
        videoDao.add(video);
        return SUCCESS;
    }

    private String editSave() {
        videoDao.update(video);
        return SUCCESS;
    }

    public VideoDao getVideoDao() {
        return videoDao;
    }

    public void setVideoDao(final VideoDao videoDao) {
        this.videoDao = videoDao;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(final Video video) {
        this.video = video;
    }

    public List<Video> getDatas() {
        return datas;
    }

    public void setDatas(final List<Video> datas) {
        this.datas = datas;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

}
