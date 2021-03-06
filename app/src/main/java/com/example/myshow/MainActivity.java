package com.example.myshow;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;
import static com.example.myshow.Contants.GetcollectUrl;
import static com.example.myshow.Contants.LoadUrl;
import static com.example.myshow.Contants.LoginUrl;
import static com.example.myshow.Contants.TAKE_PHOTO;
import static com.example.myshow.Contants.appId;
import static com.example.myshow.Contants.appSecret;
import static com.example.myshow.Contants.loginmsg;
import static com.example.myshow.Contants.myShareUrl;
import static com.example.myshow.Contants.postConnect;
import static com.example.myshow.Contants.shareMyself;
import static com.example.myshow.UserContract.upUserData;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.List;

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
    private LinearLayout t_home,t_collection,t_userpage,t_share;
    private ImageView ivhome,ivcollection,ivuser,ivCurrent,ivshare;
    private LocalSQLite mySql;
    private user mUser = new user();
    private ContentValues userValues;
    private SQLiteDatabase mydb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySql = new LocalSQLite(MainActivity.this);
        mydb = mySql.getWritableDatabase();
        userValues = new ContentValues();
        loginsys();
        Log.d("debug", "onCreate: iniPager");

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.NewPhoto:
                Intent tPhoto = new Intent(MainActivity.this,postImage.class);
                tPhoto.putExtra("pId",mUser.getmId());
                startActivityForResult(tPhoto,TAKE_PHOTO);
                break;

            case R.id.settle:
                return false;

            case R.id.cancel:
                return false;
            default:
                break;
        }
        return true;
    }


    private void initIamgeView() {

        ivhome = findViewById(R.id.ivHome);
        ivcollection = findViewById(R.id.ivCollection);
        ivuser = findViewById(R.id.ivUser);
        ivshare = findViewById(R.id.myshare);
        t_home = findViewById(R.id.home);
        t_home.setOnClickListener(this);
        t_collection = findViewById(R.id.collection);
        t_collection.setOnClickListener(this);
        t_userpage = findViewById(R.id.userpage);
        t_userpage.setOnClickListener(this);
        t_share = findViewById(R.id.sharepage);
        t_share.setOnClickListener(this);


        ivhome.setSelected(true);
        ivCurrent = ivhome;


    }

    private void initPager() {

        //?????????viewPager???????????????????????????
        viewPager = findViewById(R.id.viewpage);
        //?????????fragment????????????fragment??????
        ArrayList<Fragment> fragments = new ArrayList<>();
        /**
            fragment??????MainActivity,?????????????????????layout_main
        **/
            fragments.add(HomeFragement.newInstance(mUser.getmId(),LoadUrl));
            fragments.add(HomeFragement.newInstance(mUser.getmId(),GetcollectUrl));
            fragments.add(HomeFragement.newInstance(mUser.getmId(),myShareUrl));
            fragments.add(UserPage.newInstance(mUser.getmId()));

        //?????????Fragment?????????
        ViewPagerFragmentAdapter viewPagerAdapter
                = new ViewPagerFragmentAdapter(getSupportFragmentManager(),
                getLifecycle(),fragments);
        //viewPager??????
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                changView(position);
            }
        });
    }
    //?????????????????????????????????
    private void changView(int position) {
        ivCurrent.setSelected(false);
        Log.d(TAG, String.valueOf(position));
        switch (position){
            case R.id.home:
                viewPager.setCurrentItem(0);
            case 0:
                ivhome.setSelected(true);
                ivCurrent = ivhome;
                break;
            case R.id.collection:
                viewPager.setCurrentItem(1);
            case 1:
                ivcollection.setSelected(true);
                ivCurrent = ivcollection;
                break;
            case R.id.sharepage:
                viewPager.setCurrentItem(2);
            case 2:
                Log.d(TAG, "????????????"+String.valueOf(2));
                ivshare.setSelected(true);
                ivCurrent = ivshare;
                break;
            case R.id.userpage:
                viewPager.setCurrentItem(3);
            case 3:
                Log.d(TAG, "????????????"+String.valueOf(3));
                ivuser.setSelected(true);
                ivCurrent = ivuser;
                break;

        }

    }

    private void loginsys() {

        Intent loginIntent = new Intent(MainActivity.this,Login.class);
        //???????????????????????????????????????????????????onActivityResult????????????
        startActivityForResult(loginIntent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("debug", String.valueOf(requestCode));
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK) {
                    boolean tag = false;
                    mUser = (user) data.getSerializableExtra("user");
                    Log.d(TAG, "????????????"+String.valueOf(mUser.getSex()));


                    Cursor cursor = mydb.query(UserContract.UserEntry.TABLE_NAME,null,null,null,null,null,null);
                    int uidIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_NAME_UID);
                    while (cursor.moveToNext()){
                        if(mUser.getmId() == cursor.getLong(uidIndex)){
                            Log.d(TAG, "id = " + String.valueOf(cursor.getLong(uidIndex)));
                            tag = true;
                            break;
                        }
                    }
                    cursor.close();
                    //?????????????????????????????????????????????
                    if(!tag){
                        Log.d(TAG,  "save");
                        userValues.put(UserContract.UserEntry.COLUMN_NAME_UID,mUser.getmId());
                        userValues.put(UserContract.UserEntry.COLUMN_NAME_USERNAME,mUser.getmUserName());
                        userValues.put(UserContract.UserEntry.COLUMN_NAME_SEX,mUser.getmSex());
                        userValues.put(UserContract.UserEntry.COLUMN_NAME_INDRODUCE,mUser.getmIntroduce());
                        userValues.put(UserContract.UserEntry.COLUMN_NAME_AVATAR,mUser.getmAvatar());
                        userValues.put(UserContract.UserEntry.COLUMN_NAME_PASSWORD,mUser.getmPassword());
                        userValues.put(UserContract.UserEntry.COLUMN_NAME_ADMIN,mUser.getmUserName());
                        long r = mydb.insert(
                                UserContract.UserEntry.TABLE_NAME,
                                null,
                                userValues
                        );

                        mydb.close();
                    }else {
                        ContentValues values = new ContentValues();
                        upUserData(values,mUser,mydb);
                        mydb.close();

                    }


                    Log.d("debug", "login sucessful!");
                    //??????????????????
                    initPager();
                    //??????????????????
                    initIamgeView();
                }
                break;
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    Toast.makeText(MainActivity.this,data.getStringExtra("msg"),Toast.LENGTH_LONG).show();


                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {

        changView(view.getId());
    }

    private void addNewImage() {

    }
}