package com.example.myshow;

public class mImage {
    private Integer id;
    private Integer imageCode;
    private String content;
    private Integer pUserld;
    private String title;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImageCode(Integer imageCode) {
        this.imageCode = imageCode;
    }

    public void setpUserld(Integer pUserld) {
        this.pUserld = pUserld;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public Integer getImageCode() {
        return imageCode;
    }

    public Integer getpUserld() {
        return pUserld;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }
}

