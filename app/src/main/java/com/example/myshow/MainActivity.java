package com.example.myshow;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;
import static com.example.myshow.Contants.LoginUrl;
import static com.example.myshow.Contants.appId;
import static com.example.myshow.Contants.appSecret;
import static com.example.myshow.Contants.loginmsg;
import static com.example.myshow.Contants.postConnect;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.PhantomReference;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ViewPager2 viewPager;
    user mUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //进入登录系统
        loginsys();
        //主页面初始化
        initPager();


    }

    private void initPager() {

        //创建了viewPager控件，实现滑动效果
        viewPager = findViewById(R.id.viewpage);
        //创建了fragment列表存储fragment对象
        ArrayList<Fragment> fragments = new ArrayList<>();
        /**
            fragment依赖MainActivity,画面渲染主要在layout_main
        **/
        fragments.add(HomeFragement.newInstance("first"));
        fragments.add(HomeFragement.newInstance("sencond"));
        fragments.add(HomeFragement.newInstance("thirth"));
        //创建了Fragment适配器
        ViewPagerFragmentAdapter viewPagerAdapter
                = new ViewPagerFragmentAdapter(getSupportFragmentManager(),
                getLifecycle(),fragments);
        //viewPager渲染
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void loginsys() {

        Intent loginIntent = new Intent(MainActivity.this,Login.class);
        //切换页面，当另一页面完成任务时，在onActivityResult获取参数
        startActivityForResult(loginIntent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(requestCode == RESULT_OK){
                    mUser = (user) data.getSerializableExtra("user");
                    Log.d(TAG, "login sucessful!");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {

    }
}