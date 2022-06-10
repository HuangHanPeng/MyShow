package com.example.myshow;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class mImage implements Comparable<mImage> {
    @SerializedName("id")
    private int id;
    @SerializedName("pUserId")
    private long pUserId;
    @SerializedName("imageCode")
    private long imageCode;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("username")
    private String pUserName;
    @SerializedName("createTime")
    private long createtime;
    @SerializedName("likeId")
    private String likeId;
    @SerializedName("likeName")
    private String likeName;
    @SerializedName("hasLike")
    private boolean hasLike;
    @SerializedName("collectId")
    private String collectId;
    @SerializedName("collectNum")
    private String collectNum;
    @SerializedName("hasCollect")
    private boolean hasCollect;
    @SerializedName("hasFocus")
    private boolean hasFocus;
    @SerializedName("imageUrlList")
    private List<String> imageUrlList;


    public void setCollectId(String collectId) {
        this.collectId = collectId;
    }

    public void setCollectNum(String collectNum) {
        this.collectNum = collectNum;
    }

    public void setHasCollect(boolean hasCollect) {
        this.hasCollect = hasCollect;
    }

    public void setHasFocus(boolean hasFocus) {
        this.hasFocus = hasFocus;
    }

    public void setHasLike(boolean hasLike) {
        this.hasLike = hasLike;
    }

    public void setLikeId(String likeId) {
        this.likeId = likeId;
    }

    public void setLikeName(String likeName) {
        this.likeName = likeName;
    }

    public String getCollectId() {
        return collectId;
    }

    public String getCollectNum() {
        return collectNum;
    }

    public String getLikeId() {
        return likeId;
    }

    public String getLikeName() {
        return likeName;
    }







    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setpUserName(String pUserName) {
        this.pUserName = pUserName;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public void setImageCode(long imageCode) {
        this.imageCode = imageCode;
    }



    public void setpUserId(long pUserId) {
        this.pUserId = pUserId;
    }

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    public List<String> getImageUrlList() {
        return imageUrlList;
    }

    public long getCreatetime() {
        return createtime;
    }

    public long getId() {
        return id;
    }

    public long getImageCode() {
        return imageCode;
    }

    public long getpUserId() {
        return pUserId;
    }

    public String getContent() {
        return content;
    }

    public String getpUserName() {
        return pUserName;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int compareTo(mImage o) {

        return (int) (this.id - o.getId());
    }
}

