package com.example.myshow;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;
import static com.example.myshow.Contants.LoginUrl;
import static com.example.myshow.Contants.NETWORK_CRASH;
import static com.example.myshow.Contants.loginmsg;
import static com.example.myshow.Contants.postConnect;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class Login extends AppCompatActivity implements View.OnClickListener{


    private OkHttpClient okHttpClient;
    private Button lButton;
    private Button sButton;
    private EditText UserName;
    private EditText Password;
    private int code = 0;
    private String msg = "";
    private user mUser;
    private long id;
    private int sex;
    private String introduce;
    private String avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUser = new user();
        UserName = findViewById(R.id.username);
        Password = findViewById(R.id.Password);
        lButton = findViewById(R.id.login);
        sButton = findViewById(R.id.sign);

        sButton.setOnClickListener(this);
        lButton.setOnClickListener(this);
    }

    private Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();

            Log.d(TAG, "wrong!");
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {

            final String body = response.body().string();


            if(response.isSuccessful()){
                try {

                    JSONObject object = new JSONObject(body);
                    JSONObject data;
                    code = object.getInt("code");
                    msg = object.getString("msg");
                    //解析用户数据
                    data = object.getJSONObject("data");
                    id = data.getLong("id");
                    String sexres = String.valueOf(data.getString("sex"));
                    if(EmptyUtils.isEmpty(sexres)) sex = 0;
                    else{
                        if(sexres == "1") sex = 1;
                        else sex = 0;

                    }
                    introduce = data.getString("introduce");
                    avatar = data.getString("avatar");
                    mUser.setmId(id);
                    mUser.setmSex(sex);
                    mUser.setmIntroduce(introduce);
                    mUser.setmAvatar(avatar);

                    Intent nIntent = new Intent(Login.this,MainActivity.class);
                    nIntent.putExtra("user", mUser);
                    Log.d(TAG, String.valueOf(id));
                    setResult(RESULT_OK,nIntent);
                    finish();


                } catch (JSONException e) {
                    Log.d(TAG,"wrong!");
                    e.printStackTrace();
                }
                Log.d(TAG, body);
            }else{
                Log.d(TAG,body);
            }
        }
    };
    //注册成功后页面跳转
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        msg = "Sign sucessful!";
        switch (requestCode){
            case 1:
                code = data.getIntExtra("code",0);
                try{
                    msg = data.getStringExtra("sign_msg");
                } catch (Exception e) {
                    e.printStackTrace();
                }



                if(code == 200)
                    Toast.makeText(Login.this,msg,Toast.LENGTH_SHORT).show();
                else{
                    Toast.makeText(Login.this,msg,Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //按下的是登录按键
            case R.id.login:
                mUser.setmUserName(UserName.getText().toString());
                mUser.setmPassword(Password.getText().toString());
                Log.d(TAG,mUser.getmUserName());
                Log.d(TAG,mUser.getmPassword());
                if(mUser.getmUserName().isEmpty() || mUser.getmPassword().isEmpty()) {
                    Toast.makeText(Login.this, loginmsg, Toast.LENGTH_SHORT).show();
                    break;
                }
                FormBody formBody = new FormBody.Builder()
                        .add("username", mUser.getmUserName())
                        .add("password", mUser.getmPassword())
                        .build();
                postConnect(formBody,LoginUrl,callback);
                if(code == 500)
                    Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
                break;
            //按下注册按键
            case R.id.sign:
                Log.d(TAG, "sign: click");
                Intent signintent = new Intent(Login.this,sign.class);
                startActivityForResult(signintent,1);

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view);
        }
    }




}