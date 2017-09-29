package com.amsu.dategirls.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by HP on 2017/1/16.
 */

public class Dynamics  extends BmobObject implements Parcelable{
    private String dynamicsID;
    private String userID;
    private String time;
    private String content;
    private String userNickName;
    private String userIconUrl;
    private List<String> imageList;
    private int surnameCount;
    private int commentCount;

    public Dynamics() {
    }

    public Dynamics(String tableName) {
        super(tableName);
    }

    protected Dynamics(Parcel in) {
        dynamicsID = in.readString();
        userID = in.readString();
        time = in.readString();
        content = in.readString();
        userNickName = in.readString();
        userIconUrl = in.readString();
        imageList = in.createStringArrayList();
        surnameCount = in.readInt();
        commentCount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dynamicsID);
        dest.writeString(userID);
        dest.writeString(time);
        dest.writeString(content);
        dest.writeString(userNickName);
        dest.writeString(userIconUrl);
        dest.writeStringList(imageList);
        dest.writeInt(surnameCount);
        dest.writeInt(commentCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Dynamics> CREATOR = new Creator<Dynamics>() {
        @Override
        public Dynamics createFromParcel(Parcel in) {
            return new Dynamics(in);
        }

        @Override
        public Dynamics[] newArray(int size) {
            return new Dynamics[size];
        }
    };


    public Dynamics(String dynamicsID, String userID, String time, String content, String userNickName, String userIconUrl, List<String> imageList, int surnameCount, int commentCount) {
        this.dynamicsID = dynamicsID;
        this.userID = userID;
        this.time = time;
        this.content = content;
        this.userNickName = userNickName;
        this.userIconUrl = userIconUrl;
        this.imageList = imageList;
        this.surnameCount = surnameCount;
        this.commentCount = commentCount;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public int getSurnameCount() {
        return surnameCount;
    }

    public void setSurnameCount(int surnameCount) {
        this.surnameCount = surnameCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }


    @Override
    public String toString() {
        return "Dynamics{" +
                "dynamicsID='" + dynamicsID + '\'' +
                ", userID='" + userID + '\'' +
                ", time='" + time + '\'' +
                ", content='" + content + '\'' +
                ", userNickName='" + userNickName + '\'' +
                ", userIconUrl='" + userIconUrl + '\'' +
                ", imageList=" + imageList +
                ", surnameCount=" + surnameCount +
                ", commentCount=" + commentCount +
                '}';
    }
}
