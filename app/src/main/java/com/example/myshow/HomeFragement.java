package com.example.myshow;

import static com.example.myshow.Contants.LoadUrl;
import static com.example.myshow.Contants.TAG;
import static com.example.myshow.Contants.postConnect;
import static com.example.myshow.Contants.usrurl;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
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

    private long mId;
    public HomeFragement() {
        // Required empty public constructor
    }

    public static HomeFragement newInstance(long UID) {
        HomeFragement fragment = new HomeFragement();
        Bundle args = new Bundle();
        args.putLong(ARG_UID, UID);

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
                       Log.d(TAG, body);

                   }
               }).start();

            }
        }

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            Log.d(TAG, String.valueOf(e));
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mId =  getArguments().getLong(ARG_UID);
        LoadImageData();
    }

    private void LoadImageData() {
        FormBody formBody = new FormBody.Builder()
                .add("userId", String.valueOf(mId))
                .build();
        postConnect(formBody,LoadUrl,Loadcallback);

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