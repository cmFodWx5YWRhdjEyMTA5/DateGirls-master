package com.amsu.dategirls.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by HP on 2017/3/8.
 */

public class User extends BmobObject {
    private String userID;
    private String phone;
    private String nickName;
    private String iconUrl;
    private String city;
    private String birthday;
    private String sex;

    public User() {
    }

    public User(String userID, String phone, String nickName, String iconUrl, String city, String birthday, String sex) {
        this.userID = userID;
        this.phone = phone;
        this.nickName = nickName;
        this.iconUrl = iconUrl;
        this.city = city;
        this.birthday = birthday;
        this.sex = sex;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
