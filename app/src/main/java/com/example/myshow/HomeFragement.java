package com.example.myshow;

import static com.example.myshow.Contants.LoadUrl;
import static com.example.myshow.Contants.TAG;
import static com.example.myshow.Contants.getConnect;
import static com.example.myshow.Contants.postConnect;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragement extends Fragment {


    private static final String ARG_UID = "uId";

    private View rootView = null;

    private ListView lvImageList;
    private List<mImage> images;
    private ImageAdapter ImgAdapter;

    private String mId;
    public HomeFragement() {
        // Required empty public constructor
    }

    public static HomeFragement newInstance(String UID) {
        HomeFragement fragment = new HomeFragement();
        Bundle args = new Bundle();
        args.putString(ARG_UID, UID);

        fragment.setArguments(args);

        return fragment;
    }

    private void initView() {
        lvImageList = rootView.findViewById(R.id.lv_image_lsit);
        lvImageList.setAdapter(ImgAdapter);
    }



    private Callback Loadcallback = new Callback() {

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {


            final String body = response.body().string();
            if(response.isSuccessful()){
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       Log.d("debug", body);
                       Gson gson = new Gson();
                       //定义反序列化类型
                       Type jsonType = new TypeToken<BaseResponse<Data>>(){}.getType();
                       //解析
                       BaseResponse<Data> dataResponse = gson.fromJson(body,jsonType);


                   }
               }).start();

            }
        }

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            Log.d("debug", String.valueOf(e));
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mId =  getArguments().getString(ARG_UID);
        LoadImageData();
    }

    private void LoadImageData() {

        getAsyn();
    }

    private void getAsyn(){
        Map<String, Object> hmap;
        hmap = new HashMap<>();
        hmap.put("userId",mId);
        getConnect(hmap,LoadUrl,Loadcallback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_home_fragement,
                    container, 
                    false);
        }

        // Inflate the layout for this fragment
        return rootView;
    }
}