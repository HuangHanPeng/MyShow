package com.example.myshow;

import static com.example.myshow.Contants.LoadUrl;
import static com.example.myshow.Contants.TAG;
import static com.example.myshow.Contants.getConnect;
import static com.example.myshow.Contants.postConnect;

import static java.util.Comparator.reverseOrder;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private static final String ARG_URL = "url";
    private View rootView = null;
    private int current_page = 1;
    private ListView lvImageList;
    private List<mImage> images;
    private ImageAdapter ImgAdapter;
    public FragmentActivity activty;
    private long mId;
    private boolean addFlag = false;
    private int count = 1;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String mUrl;

    public HomeFragement() {
        // Required empty public constructor
    }

    public static HomeFragement newInstance(long UID,String url) {
        HomeFragement fragment = new HomeFragement();
        Bundle args = new Bundle();
        args.putLong(ARG_UID, UID);
        args.putString(ARG_URL,url);
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

    //异步请求，加载分享数据
    private void LoadImageData(int current) {

        getAsyn(current);
    }

    //异步请求的回调函数
    private Callback Loadcallback = new Callback() {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {


            final String body = response.body().string();
            int id = 0;
            if(response.isSuccessful()){
                if(activty ==null){
                    Log.d(TAG,"null activty");

                }
                activty.runOnUiThread(() -> {
                    Log.d("debug", body);
                    Gson gson = new Gson();
                    //定义反序列化类型
                    Type jsonType = new TypeToken<BaseResponse<Data>>(){}.getType();
                    //解析
                    BaseResponse<Data> dataResponse = gson.fromJson(body,jsonType);
                    boolean r = ImgAdapter.isEmpty();
                    if(!r) count++;
                    if(dataResponse.getData() != null)
                        for(mImage mImg:dataResponse.getData().getRecords()){

                            if(dataResponse.getData().current == 1 && count > 1){
                               if(mImg.getId() > ImgAdapter.getItem(0).getId()){
                                   Log.d(TAG, "加载用户名" + mImg.getpUserName());
                                   ImgAdapter.add(mImg);
                                   ImgAdapter.sort(Comparator.reverseOrder());
                                   addFlag = true;
                               }
                            }else {
                                 addFlag = true;
                                 ImgAdapter.add(mImg);
                            }

                        }
                    if(addFlag) {
                        ImgAdapter.notifyDataSetChanged();
                        current_page =  dataResponse.getData().current + 1;
                        Log.d(TAG, "当前页"+String.valueOf(current_page));
                    }
                    addFlag = false;
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
        mId =  getArguments().getLong(ARG_UID);
        mUrl = getArguments().getString(ARG_URL);
    }

    private void InitData() {
        images = new ArrayList<>();

        ImgAdapter = new ImageAdapter(rootView.getContext(),R.layout.list_image_item,images);
        lvImageList.setAdapter(ImgAdapter);
        Log.d(TAG,"InitDATA");
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srf1);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.darker_gray);
        LoadImageData(1);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.sendEmptyMessageDelayed(199,1000);
            }
        });
        lvImageList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if(view.getLastVisiblePosition() == (view.getCount())-1){
                                LoadImageData(current_page);
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {

            if(msg.what == 199){
                getAsyn(1);
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    };

    private void getAsyn(int current){
        Map<String, Object> hmap;
        hmap = new HashMap<>();
        hmap.put("userId",mId);
        hmap.put("current",current);

        new Thread(new Runnable() {
            @Override

            public void run() {
                getConnect(hmap,mUrl,Loadcallback);
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