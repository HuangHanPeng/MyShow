package com.example.myshow;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class Data extends JSONObject {
    @SerializedName("total")
    public int total;
    @SerializedName("size")
    public int size;
    @SerializedName("current")
    public int current;
    @SerializedName("records")
    public List<mImage> records;

    public int getTotal() {
        return total;
    }

    public int getSize() {
        return size;
    }

    public List<mImage> getRecords() {
        return records;
    }
}
