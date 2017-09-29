package com.amsu.dategirls.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by HP on 2017/3/8.
 */

public class Comment extends BmobObject {
    private String commentID;
    private String dynamicsID;
    private String userID;
    private String userNickName;
    private String userIconUrl;
    private String content;
    private String time;

    public Comment(String commentID, String dynamicsID, String userID, String userNickName, String userIconUrl, String content, String time) {
        this.commentID = commentID;
        this.dynamicsID = dynamicsID;
        this.userID = userID;
        this.userNickName = userNickName;
        this.userIconUrl = userIconUrl;
        this.content = content;
        this.time = time;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getDynamicsID() {
        return dynamicsID;
    }

    public void setDynamicsID(String dynamicsID) {
        this.dynamicsID = dynamicsID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserIconUrl() {
        return userIconUrl;
    }

    public void setUserIconUrl(String userIconUrl) {
        this.userIconUrl = userIconUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
