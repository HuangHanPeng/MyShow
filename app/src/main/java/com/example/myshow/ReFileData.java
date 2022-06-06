package com.example.myshow;

import java.util.List;

public class ReFileData {

    //图片上传后的数据解析
    private long imageCode;
    private List<String> imageUrlList;

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    public void setImageCode(long imageCode) {
        this.imageCode = imageCode;
    }

    public List<String> getImageUrlList() {
        return imageUrlList;
    }

    public long getImageCode() {
        return imageCode;
    }
}
