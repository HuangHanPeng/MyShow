package com.example.myshow;

import com.google.gson.annotations.SerializedName;

public class ResponseData <T>{
    @SerializedName("records")
    private T records;
    private int total;
    private int size;
    private int current;

    public void setCurrent(int current) {
        this.current = current;
    }

    public void setRecords(T records) {
        this.records = records;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrent() {
        return current;
    }

    public int getSize() {
        return size;
    }

    public int getTotal() {
        return total;
    }

    public T getRecords() {
        return records;
    }
}
