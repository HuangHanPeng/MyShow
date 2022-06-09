package com.example.myshow;

import static android.content.ContentValues.TAG;
import static com.example.myshow.Contants.CHOOSE_PHOTO;

import static com.example.myshow.Contants.PFileUrl;
import static com.example.myshow.Contants.PublishUrl;

import static com.example.myshow.Contants.postConnect;
import static com.example.myshow.Contants.postFile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;

import android.app.Dialog;
import android.content.ContentUris;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class postImage extends AppCompatActivity implements View.OnClickListener {

    private EditText MutlText,pTitle;
    private ImageView AddPhotos;
    private String mContent = "new share";
    private Uri imageUri = null;
    private File outputImage;
    private String pushTitle = "new";
    private long imageCode;
    public static int TakePs = 1;
    private long pId;
    private List<File> pushPlist;
    private Intent resIntent;
    private int code = -1;
    private String msg;
    private Dialog mydialog;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbarphoto,menu);

        return true;


    }

    private void mResIntent(int code,String msg) {
        resIntent.putExtra("code",code);
        resIntent.putExtra("msg",msg);
        setResult(RESULT_OK,resIntent);
        finish();
    }

    Callback PublishCall = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            e.printStackTrace();
            msg = "上传图片出错!";

        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String body = response.body().string();
                        Gson gson = new Gson();
                        Log.d(TAG, body);
                        Type jsonType = new TypeToken<BaseResponse<String>>(){}.getType();
                        BaseResponse<String> baseResponse = gson.fromJson(body,jsonType);
                        code = baseResponse.getCode();
                        msg = baseResponse.getMsg();
                        Log.d(TAG, "publish" + String.valueOf(code));
                        msg = "成功上传图片";
                        mResIntent(code,msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                        msg = "上传图片出错!";
                    }


                }
            });
        }
    };

    Callback pFileCall = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            msg = "上传图片出错!";
            e.printStackTrace();
            Log.d(TAG,"fileCall wrong!");
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String body = response.body().string();
                        Log.d(TAG, body);
                        Gson gson = new Gson();
                        Type jsonType = new TypeToken<BaseResponse<ReFileData>>(){}.getType();
                        BaseResponse<ReFileData> baseResponse = gson.fromJson(body,jsonType);
                        ReFileData resData = baseResponse.getData();
                        code = baseResponse.getCode();
                        Log.d(TAG,String.valueOf(code));
                        msg = baseResponse.getMsg();
                        imageCode = resData.getImageCode();
                        Log.d(TAG,String.valueOf(imageCode));
                        if(code == 200){
                            Log.d(TAG, "publish");
                            JSONObject pushJson = new JSONObject();
                            try {
                                pushJson.put("content",mContent);
                                pushJson.put("imageCode",imageCode);
                                pushJson.put("pUserId",pId);
                                pushJson.put("title",pushTitle);
                                postConnect(pushJson.toString(),PublishUrl,PublishCall);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                msg = "上传图片出错!";
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        msg = "上传图片出错!";
                    }
                }
            });

     }
    };


    public void publish(){
        postFile(PFileUrl,pushPlist,pFileCall);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.pushPhoto:
                Log.d(Contants.TAG, "push");
                if(EmptyUtils.isNotEmpty(MutlText.getText().toString()))
                    mContent = MutlText.getText().toString();
                if(EmptyUtils.isNotEmpty(pTitle.getText().toString()))
                    pushTitle = pTitle.getText().toString();

                if(imageUri != null){
                    Log.d(TAG, "发布");
                    publish();

                }else {
                    Toast.makeText(postImage.this,"不能上传空图像",Toast.LENGTH_LONG).show();
                }

                break;
            case R.id._return:
                mResIntent(-1,"取消");
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_image);
        resIntent = new Intent(postImage.this,MainActivity.class);
        Intent intent = getIntent();
        pId = intent.getLongExtra("pId",0);
        pushPlist = new ArrayList<>();
        initView();
        oporation();


    }



    private void oporation() {
        AddPhotos.setOnClickListener(this);

    }

    void setDialog(){

        Window window = mydialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        lp.width = (int) (defaultDisplay.getWidth() * 0.7);
        window.setAttributes(lp);
    }
    private void initView() {
        MutlText = findViewById(R.id.MutilText);
        AddPhotos = findViewById(R.id.AddPhotos);
        pTitle = findViewById(R.id.ptitle);
        mydialog = new Dialog(this,R.style.MyDialog);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog,null);
        View btn_cancel =  dialogView.findViewById(R.id._cancel);
        btn_cancel.setOnClickListener(this);
        View btn_album = dialogView.findViewById(R.id._album);
        btn_album.setOnClickListener(this);
        View btn_photo = dialogView.findViewById(R.id._photo);
        btn_photo.setOnClickListener(this);
        mydialog.setContentView(dialogView);

    }

    private void CheckPermissions(){
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
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
            case R.id.AddPhotos:

                CheckPermissions();

                //tokePhoto();
                //openAlbum();
                mydialog.setCancelable(true);
                mydialog.show();
                setDialog();
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
            imageUri = FileProvider.getUriForFile(postImage.this,
                    "com.example.cameraalbumtest.fileprovider",outputImage);
        }else {
            imageUri = Uri.fromFile(outputImage);
        }


        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TakePs);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK)
                {   
                    
                    try {

                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                                .openInputStream(imageUri));
                        postImage.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                imageUri));
                        try {
                            OutputStream outputStream = getContentResolver().openOutputStream(imageUri, "rw");
                            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)) {
                                Log.e("保存成功", "success");
                            } else {
                                Log.e("保存失败", "fail");
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        pushPlist.add(outputImage);
                        AddPhotos.setImageBitmap(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                mydialog.dismiss();
                break;
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    if(Build.VERSION.SDK_INT >= 19){
                        handleImageOnKitKat(data,AddPhotos);
                    }else {
                        handleImageBeforeKitKat(data,AddPhotos);
                    }
                }
                mydialog.dismiss();
                break;
            default:
               break;
        }

    }

    private void handleImageOnKitKat(Intent data,ImageView picture) {
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                //document类型uri
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.
                        parse("content://downloads/public_downloads")
                ,Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }else if("content".equalsIgnoreCase(uri.getScheme())){
                //普通的uri
                imagePath = getImagePath(uri,null);

            }else if("file".equalsIgnoreCase(uri.getScheme())){
                //file 类型uri
                imagePath = uri.getPath();
            }
        }
        displayImage(imagePath,picture);
    }

    private void displayImage(String imagePath,ImageView picture) {
        if(imagePath!=null){
            pushPlist.add(new File(imagePath));
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }

    }

    private void handleImageBeforeKitKat(Intent data,ImageView picture){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath,picture);
    }

    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.
                        PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }


}