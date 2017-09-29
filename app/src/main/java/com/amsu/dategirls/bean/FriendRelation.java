package com.amsu.dategirls.bean;

import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.v3.BmobObject;

/**
 * Created by HP on 2017/3/29.
 */

public class FriendRelation extends BmobObject implements Parcelable{
    private String friendRelationID;
    private String otherUserID;
    private String myUserID;
    private String otherNickName;
    private String otherIconUrl;
    private String otherCity;
    private String otherSex;

    public FriendRelation() {
    }

    public FriendRelation(String friendRelationID, String otherUserID, String myUserID, String otherNickName, String otherIconUrl, String otherCity, String otherSex) {
        this.friendRelationID = friendRelationID;
        this.otherUserID = otherUserID;
        this.myUserID = myUserID;
        this.otherNickName = otherNickName;
        this.otherIconUrl = otherIconUrl;
        this.otherCity = otherCity;
        this.otherSex = otherSex;
    }

    protected FriendRelation(Parcel in) {
        friendRelationID = in.readString();
        otherUserID = in.readString();
        myUserID = in.readString();
        otherNickName = in.readString();
        otherIconUrl = in.readString();
        otherCity = in.readString();
        otherSex = in.readString();
        setObjectId(in.readString());
    }

    public static final Creator<FriendRelation> CREATOR = new Creator<FriendRelation>() {
        @Override
        public FriendRelation createFromParcel(Parcel in) {
            return new FriendRelation(in);
        }

        @Override
        public FriendRelation[] newArray(int size) {
            return new FriendRelation[size];
        }
    };

    public String getOtherUserID() {
        return otherUserID;
    }

    public void setOtherUserID(String otherUserID) {
        this.otherUserID = otherUserID;
    }

    public String getMyUserID() {
        return myUserID;
    }

    public void setMyUserID(String myUserID) {
        this.myUserID = myUserID;
    }

    public String getOtherNickName() {
        return otherNickName;
    }

    public void setOtherNickName(String otherNickName) {
        this.otherNickName = otherNickName;
    }

    public String getOtherCity() {
        return otherCity;
    }

    public void setOtherCity(String otherCity) {
        this.otherCity = otherCity;
    }

    public String getOtherSex() {
        return otherSex;
    }

    public void setOtherSex(String otherSex) {
        this.otherSex = otherSex;
    }

    public String getFriendRelationID() {
        return friendRelationID;
    }

    public void setFriendRelationID(String friendRelationID) {
        this.friendRelationID = friendRelationID;
    }

    public String getOtherIconUrl() {
        return otherIconUrl;
    }

    public void setOtherIconUrl(String otherIconUrl) {
        this.otherIconUrl = otherIconUrl;
    }

    @Override
    public String toString() {
        return "FriendRelation{" +
                "friendRelationID='" + friendRelationID + '\'' +
                ", otherUserID='" + otherUserID + '\'' +
                ", myUserID='" + myUserID + '\'' +
                ", otherNickName='" + otherNickName + '\'' +
                ", otherIconUrl='" + otherIconUrl + '\'' +
                ", otherCity='" + otherCity + '\'' +
                ", otherSex='" + otherSex + '\'' +
                ", ObjectId='" + getObjectId() + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(friendRelationID);
        dest.writeString(otherUserID);
        dest.writeString(myUserID);
        dest.writeString(otherNickName);
        dest.writeString(otherIconUrl);
        dest.writeString(otherCity);
        dest.writeString(otherSex);
        dest.writeString(getObjectId());
    }


}
