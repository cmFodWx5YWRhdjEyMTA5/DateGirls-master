package com.amsu.dategirls.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by hai on 2017/9/28.
 */

public class Article extends BmobObject {
    private String articleID;
    private String articleTitle;
    private String articleUrl;
    private String articleImageUrl;
    private boolean isHomeArticle;

    public String getArticleID() {
        return articleID;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getArticleImageUrl() {
        return articleImageUrl;
    }

    public void setArticleImageUrl(String articleImageUrl) {
        this.articleImageUrl = articleImageUrl;
    }

    public boolean isHomeArticle() {
        return isHomeArticle;
    }

    public void setHomeArticle(boolean homeArticle) {
        isHomeArticle = homeArticle;
    }
}
