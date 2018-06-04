package com.lvr.livecircle.bean;

import com.lvr.livecircle.home.ShellActivity;

import java.io.Serializable;

public class Notice implements Serializable{
    private String id;
    private String title;
    private String content;
    private String memo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
