package com.example.myshow;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;
import static com.example.myshow.Contants.LoginUrl;
import static com.example.myshow.Contants.appId;
import static com.example.myshow.Contants.appSecret;
import static com.example.myshow.Contants.postConnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.PhantomReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private OkHttpClient okHttpClient;
    private Button lButton;
    private Button sButton;
    private EditText UserName;
    private EditText Password;

    private user mUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUser = new user();
        UserName = findViewById(R.id.username);
        Password = findViewById(R.id.Password);
        lButton = findViewById(R.id.login);



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
            Log.d(TAG, body);
        }
    };


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
                case R.id.login:
                    mUser.setmUserName(UserName.getText().toString());
                    mUser.setmPassword(Password.getText().toString());
                    FormBody formBody = new FormBody.Builder()
                            .add("password", mUser.getmPassword())
                            .add("username", mUser.getmUserName())
                            .build();
                    postConnect(formBody,LoginUrl,callback);
                break;
            case R.id.sign:

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view);
        }
    }




}