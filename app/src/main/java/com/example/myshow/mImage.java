package com.example.myshow;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class mImage {
    @SerializedName("id")
    private long id;
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
    @SerializedName("imageUrlList")
    private String imageUrL;

    public void setId(long id) {
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

    public void setImageUrL(String imageUrL) {
        this.imageUrL = imageUrL;
    }

    public void setpUserId(long pUserId) {
        this.pUserId = pUserId;
    }

    public long getCreatetime() {
        return createtime;
    }

    public String getImageUrL() {
        return imageUrL;
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

}

