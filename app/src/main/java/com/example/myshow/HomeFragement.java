package com.example.myshow;

import static com.example.myshow.Contants.LoadUrl;
import static com.example.myshow.Contants.TAG;
import static com.example.myshow.Contants.getConnect;
import static com.example.myshow.Contants.postConnect;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
    public FragmentActivity activty;
    private String mId;
    public HomeFragement() {
        // Required empty public constructor
    }

    public static HomeFragement newInstance(String UID) {
        HomeFragement fragment = new HomeFragement();
        Bundle args = new Bundle();
        args.putString(ARG_UID, UID);

        fragment.setArguments(args);
        fragment.getActivity();
        return fragment;
    }

    private void initView() {
        lvImageList = rootView.findViewById(R.id.lv_image_lsit);
        lvImageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"HELLO");
            }
        });


    }
    private void LoadImageData() {

        getAsyn();
    }


    private Callback Loadcallback = new Callback() {

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {


            final String body = response.body().string();

            if(response.isSuccessful()){
                if(activty ==null){
                    Log.d(TAG,"null activty");

                }
                activty.runOnUiThread(() -> {
                    Log.d(TAG,"callck");
                    Log.d("debug", body);
                    Gson gson = new Gson();
                    //定义反序列化类型
                    Type jsonType = new TypeToken<BaseResponse<Data>>(){}.getType();
                    //解析
                    BaseResponse<Data> dataResponse = gson.fromJson(body,jsonType);
                    for(mImage mImg:dataResponse.getData().getRecords()){

                        ImgAdapter.add(mImg);

                    }
                    ImgAdapter.notifyDataSetChanged();
                });

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
        activty = getActivity();
        mId =  getArguments().getString(ARG_UID);

    }

    private void InitData() {
        images = new ArrayList<>();
        ImgAdapter = new ImageAdapter(rootView.getContext(),R.layout.list_image_item,images);
        lvImageList.setAdapter(ImgAdapter);
        Log.d(TAG,"InitDATA");
        LoadImageData();
    }



    private void getAsyn(){
        Map<String, Object> hmap;
        hmap = new HashMap<>();
        hmap.put("userId",mId);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getConnect(hmap,LoadUrl,Loadcallback);
            }
        }).start();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceStat) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_home_fragement,
                    container, 
                    false);
        }
        initView();
        InitData();

        // Inflate the layout for this fragment
        return rootView;
    }
}