package com.fight2.dao;

import org.springframework.stereotype.Repository;

import com.fight2.model.Video;

@Repository
public class VideoDaoImpl extends BaseDaoImpl<Video> implements VideoDao {

    public VideoDaoImpl() {
        super(Video.class);
    }

}
