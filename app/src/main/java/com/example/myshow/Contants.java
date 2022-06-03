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
/**
   Contants类定义常用的变量，以及函数
 **/
public final class Contants {

    public final static String TAG = "debug";
    public static final String loginmsg = "用户名或密码不能为空！";
    //通用的url地址
    public static final String usrurl = "http://47.107.52.7:88/member/photo/user/";
    //api接口地址
    public static final String login = "login";
    public static final String sign = "register";


    //请求头添加
    public static String appId = "4d0b26ccf9804ddebec579d5fee73713";
    public static String appSecret = "5320843e542ad80474f6183a866ab69069f2e";
    public static final String token = "bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2NTQyMjEzODYsInVzZXJfbmFtZSI6IjE2IiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImp0aSI6ImFjOTk3ZDIwLWU0ODItNDkwMy04ODVjLTE1M2E5OWMyMjdjYyIsImNsaWVudF9pZCI6ImNvaW4tYXBpIiwic2NvcGUiOlsiYWxsIl19.HklB2yJ1-wkVhQJi01OsWAGnUTZSwVNEb8KDgV_wS1aaVVSmr5fCwqF8eTgBTImks7YFKHvAAM8Zi39uBONIeKCVAZ0Z4-4e2O7w3Kz-WIFGbF3OqywUsnnw3MVanG_9-CSBnM2YFjBjgsnFuI_-4MA7bpMWb8ffo__ja9P5xQXYFLEJcrTEdZnfPBejFPbMz1aS34g9asRHVnUIR_RbV5NPjvQhKNstHQp7CuKGcs3kDn5sJxAU6l3Qj0t7TjcxeAR_j1X8o0j_Vfek_TX4apzHYiOJkjiGLcgxOsksOjtT9l5g10XAqrRB4qWYMxoh07mgplG_WDk93D7Y2tWkPQ";

    //请求url地址
    public static final String LoginUrl = usrurl + login;
    public static final String signUrl = usrurl + sign;




    //postConnect方法，用于申请post请求，传入表单数据，以及请求url，和功能使用的回调函数
    public static final void postConnect(FormBody formBody, String url, Callback callback){

        Request request = new Request.Builder()
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
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

       //postConnect方法，用于申请post请求，传入json数据格式，以及请求url，和功能使用的回调函数
        public static final void postConnect(String json,String url,Callback callback){
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = new Request.Builder()
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
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
