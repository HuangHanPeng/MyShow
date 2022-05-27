package com.example.myshow;

import static com.example.myshow.R.id.sign1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class sign extends AppCompatActivity {


    public int code = 0;
    public String msg = "用户名已存在";
    private EditText _user;
    private EditText _psw;
    Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            final String body = response.body().string();

            JSONObject object = null;
            try {
                object = new JSONObject(body);
                code = object.getInt("code");
                msg = object.getString("msg");
                Log.d(Contants.TAG,msg);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d(Contants.TAG, "onResponse: "+ body);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        Button btn = findViewById(sign1);
        _user = findViewById(R.id.username2);
        _psw = findViewById(R.id.Password2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("password",_user.getText());
                    jsonObject.put("username",_psw.getText());
                    Contants.postConnect(jsonObject.toString(),Contants.signUrl,callback);

                    intent.putExtra("code",code);
                    intent.putExtra("sign_msg",msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setResult(RESULT_OK,intent);
                finish();
            }
        });




    }
}