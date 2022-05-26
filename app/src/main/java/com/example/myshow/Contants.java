package com.example.myshow;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class Contants {
    public final static String TAG = "debug";
    public static final String usrurl = "http://47.107.52.7:88/member/photo/user/";
    public static final String login = "login";
    public static String appId = "4d0b26ccf9804ddebec579d5fee73713";
    public static String appSecret = "5320843e542ad80474f6183a866ab69069f2e";
    public static final String sign = "register";

    public static final String LoginUrl = usrurl + login;
    public static final String signUrl = usrurl + sign;

    public static final String token = "bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2NTQxNjIxNDEsInVzZXJfbmFtZSI6IjE2IiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6IjI0MTEyM2NhLTQ3NDAtNDZhOC1iMDNmLWU2NmYwNTYxOWQ5ZiIsImNsaWVudF9pZCI6ImNvaW4tYXBpIiwic2NvcGUiOlsiYWxsIl19.k7sfoy3mLZjZSNHZGdc0GPpG8yv4On5Ew2jMN7TpT4o9TsXS0IzdM-Fu3W9YHHoQKlgg3ZvJlZF6MBPv1BQQETF5IymkASVBnrrJOTbwQeMfPg13jyNOXp9FmqoRgMuKlHTEryhea5VBH4MBf3atWLpFsh8eCvX0X_j0tFiwz98K9iZBjTz-oqesmSRWj6Bjw1APlwA2fPYamwfqAQaXSjYyTeFbpwSErmg7aDNDSd6NtgjjKmqB4RQ9WYNpE_MTZ7Hx_-Tf1gtvSQi6S3e0VPHagitp4EkMzAcC8Js5iBnFcvt-m2ASt7GhoZ6lmkJFPPlpsERzMrF3rtmT80ImMw";



    public static final void postConnect(FormBody formBody, String url, Callback callback){

        Request request = new Request.Builder()
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
                .addHeader("Authorization",token)
                .url(url)
                .post(formBody)
                .build();

        try {
            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        public static final void postConnect(String json,String url,Callback callback){
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = new Request.Builder()
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
                .addHeader("Authorization",token)
                .url(url)
                .post(requestBody)
                .build();

        try {
            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
