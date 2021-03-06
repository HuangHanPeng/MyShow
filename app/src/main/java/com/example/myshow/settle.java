package com.example.myshow;

import static com.example.myshow.Contants.CHOOSE_PHOTO;
import static com.example.myshow.Contants.PFileUrl;
import static com.example.myshow.Contants.PubAheadUrl;
import static com.example.myshow.Contants.TAKE_PHOTO;
import static com.example.myshow.Contants.login;
import static com.example.myshow.Contants.postConnect;
import static com.example.myshow.Contants.postFile;
import static com.example.myshow.UserContract.upUserData;
import static com.example.myshow.postImage.TakePs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.InetAddresses;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class settle extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "debug";

    private RadioGroup radioGroup;
    private RadioButton mbtn,fmbtn;
    private ImageView head;
    private EditText inputName,inputIndroduce;
    private View mSave;
    private String Imagepath = null;
    private List<File> fileList;
    private String imageUrl = null;
    private File outputImage;
    private LocalSQLite mySQL;
    private SQLiteDatabase myDb;
    private user mUser;
    private Dialog mydialog;
    private Uri imageUri;
    public static final String staUrl = "https://guet-lab.oss-cn-guangzhou.aliyuncs.com/api/2022/06/07/outputImage1";

    //??????????????????
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setlayout);
        mySQL = new LocalSQLite(settle.this);
        myDb = mySQL.getWritableDatabase();
        Cursor cursor = myDb.query(UserContract.UserEntry.TABLE_NAME,null,null,null,null,null,null);
        Intent getintent = getIntent();
        mydialog = new Dialog(this,R.style.MyDialog);
        mUser = new user();
        mUser.setmId(getintent.getLongExtra("UID",0));
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog,null);
        View btn_cancel =  dialogView.findViewById(R.id._cancel);
        btn_cancel.setOnClickListener(this);
        View btn_album = dialogView.findViewById(R.id._album);
        btn_album.setOnClickListener(this);
        View btn_photo = dialogView.findViewById(R.id._photo);
        btn_photo.setOnClickListener(this);
        UserContract.getUserData(cursor,mUser);
        initData();
        mydialog.setContentView(dialogView);

    }

    //??????????????????
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mtoolbar,menu);
        return true;
    }

    //????????????
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //?????????????????????????????????
        switch (item.getItemId()){
            case R.id.cancel:
                Log.d(TAG, "click");
                Intent cancleIntent = new Intent(settle.this,UserPage.class);
                cancleIntent.putExtra("back",0);
                setResult(RESULT_OK,cancleIntent);
                finish();
            default:
                break;
        }
        return true;
    }

    //???????????????
    @SuppressLint("WrongViewCast")
    private void initData() {
        //???????????????
        fileList = new ArrayList<>();
        head = findViewById(R.id.setAvatar);
        mSave = findViewById(R.id.saveMy);
        mSave.setOnClickListener(this);
        head.setOnClickListener(this);

        radioGroup = findViewById(R.id.select_sex);
        mbtn = findViewById(R.id.smale);
        fmbtn = findViewById(R.id.sfale);

        inputName = findViewById(R.id.setName);
        inputIndroduce = findViewById(R.id.setIndroduce);
        //????????????
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.smale:
                        Log.d(TAG, "male");
                        mUser.setmSex(0);
                        break;
                    case R.id.sfale:
                        Log.d(TAG, "fmale");
                        mUser.setmSex(1);
                        break;
                    default:
                        break;

                }
            }
        });
    }
    //?????????????????????
    private void handleImageOnKitKat(Intent data,ImageView picture) {
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                //document??????uri
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.
                                parse("content://downloads/public_downloads")
                        ,Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }else if("content".equalsIgnoreCase(uri.getScheme())){
                //?????????uri
                imagePath = getImagePath(uri,null);

            }else if("file".equalsIgnoreCase(uri.getScheme())){
                //file ??????uri
                imagePath = uri.getPath();
            }
        }


        displayImage(imagePath,picture);
    }

    //????????????
    private void displayImage(String imagePath,ImageView picture) {

        if(imagePath!=null){
            fileList.add(new File(imagePath));
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }

    }
    //?????????????????????
    private void handleImageBeforeKitKat(Intent data,ImageView picture){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        Imagepath = imagePath;
        displayImage(imagePath,picture);
    }

    //????????????????????????
    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // ??????Uri???selection??????????????????????????????
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

        //????????????
    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }
    //????????????????????????
    Callback callback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String body = null;
                    try {
                        body = response.body().string();
                        Log.d(TAG, body);
                        Gson gson = new Gson();
                        Type jsonType = new TypeToken<BaseResponse<ReFileData>>(){}.getType();
                        BaseResponse<ReFileData> baseResponse = gson.fromJson(body,jsonType);

                        ReFileData resData = baseResponse.getData();

                        if(EmptyUtils.isNotEmpty(resData)){
                            imageUrl = resData.getImageUrlList().get(0);
                            mUser.setmAvatar(imageUrl);
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });
        }
    };

    //??????????????????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    if(Build.VERSION.SDK_INT >= 19){
                        handleImageOnKitKat(data,head);
                        if(!fileList.isEmpty())
                            postFile(PFileUrl,fileList,callback);
                    }else {
                        handleImageBeforeKitKat(data,head);
                        if(!fileList.isEmpty())
                            postFile(PFileUrl,fileList,callback);
                    }
                    mydialog.dismiss();
                }
                break;
            case TAKE_PHOTO:
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver()
                            .openInputStream(imageUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                fileList.add(outputImage);
                head.setImageBitmap(bitmap);
                if(!fileList.isEmpty())
                    postFile(PFileUrl,fileList,callback);
                mydialog.dismiss();
                break;
            default:
                break;
        }

    }

    void CheckPermissions(){
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
            //????????????????????????
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //????????????
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }

    }
    //??????????????????
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setAvatar:
                mydialog.show();
                setDialog();
                Log.d(TAG, "onClick: openAlbum");
                break;
            case R.id.saveMy:
                //????????????????????????????????????
                Log.d(TAG, "onClick: push");
                if(EmptyUtils.isNotEmpty(inputName.getText().toString()))
                    mUser.setmAdmin(inputName.getText().toString());
                Log.d(TAG, "??????admin" + mUser.getmAdmin());
                if(EmptyUtils.isNotEmpty(inputIndroduce.getText().toString()))
                    mUser.setmIntroduce(inputIndroduce.getText().toString());
                try {

                    boolean res1 = push();
                    if(!res1){
                        Toast.makeText(settle.this,"??????????????????",Toast.LENGTH_LONG).show();

                    }else {
                        ContentValues values = new ContentValues();
                        upUserData(values,mUser,myDb);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id._cancel:
                mydialog.dismiss();
                break;
            case R.id._photo:
                CheckPermissions();
                tokePhoto();
                break;
            case R.id._album:
                CheckPermissions();
                openAlbum();
                break;
            default:
                break;
        }
    }


    private void tokePhoto(){

        outputImage = new File(getExternalCacheDir(),
                "output_image.jpg");

        try{
            if(outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(Build.VERSION.SDK_INT >= 24){
            imageUri = FileProvider.getUriForFile(settle.this,
                    "com.example.cameraalbumtest.fileprovider",outputImage);
        }else {
            imageUri = Uri.fromFile(outputImage);
        }


        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TAKE_PHOTO);

    }

    void setDialog(){

        Window window = mydialog.getWindow();
        WindowManager m = window.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) (d.getWidth());
        lp.height = (int) (d.getHeight());
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
    }



    //????????????????????????
    Callback Acallback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {

        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                    try {

                        String body = response.body().string();
                        Gson gson = new Gson();
                        String msg = null;
                        //???????????????????????????
                        Type jsonType = new TypeToken<BaseResponse<ReFileData>>(){}.getType();
                        BaseResponse<ReFileData> baseResponse = gson.fromJson(body,jsonType);
                        //????????????????????????
                        int code = baseResponse.getCode();
                        msg = baseResponse.getMsg();
                        Log.d(TAG,body);
                        if(msg == null) {
                            if (code != 200) {
                                msg = "????????????";
                            } else {
                                msg = "????????????";
                                ContentValues values = new ContentValues();
                                myDb = mySQL.getWritableDatabase();
                                upUserData(values, mUser, myDb);
                            }
                        }
                        //??????????????????
                        Intent res = new Intent(settle.this, UserPage.class);
                        res.putExtra("msg",msg);
                        res.putExtra("code",code);
                        res.putExtra("back",1);
                        setResult(RESULT_OK,res);
                        finish();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }


        }
    };
    //??????????????????
    private boolean push() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        if(imageUrl != null){
            Log.d(TAG, imageUrl);
            jsonObject.put("avatar",imageUrl);

        }else {
            return false;
        }
        if(EmptyUtils.isEmpty(mUser.getmSex())){
            mUser.setmSex(0);
        }
        //???????????????json??????
        jsonObject.put("introduce",mUser.getmIntroduce());
        jsonObject.put("sex",mUser.getmSex());
        jsonObject.put("username",mUser.getmUserName());
        jsonObject.put("id",mUser.getmId());
        Log.d(TAG, jsonObject.toString());
        postConnect(jsonObject.toString(),PubAheadUrl,Acallback);
        return true;
    }
}