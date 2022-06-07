package com.example.myshow;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import static com.example.myshow.UserContract.getUserData;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.File;
import java.lang.ref.PhantomReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "UID";
    private  View rootView = null;
    private String imagepath = null;
    private LocalSQLite mySql;
    private SQLiteDatabase mydab;
    private TextView myName,myInroduce;
    private ImageView mySex,myavatar;
    private user mUser;
    // TODO: Rename and change types of parameters

    public UserPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment UserPage.
     */
    // TODO: Rename and change types and number of parameters
    public static UserPage newInstance(long id) {
        UserPage fragment = new UserPage();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1,id);
        Log.d(TAG, "Fragment"+String.valueOf(id));
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.tooluserbar,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.settle:
                Intent sIntent = new Intent(getActivity(),settle.class);
                sIntent.putExtra("UID",mUser.getmId());
                startActivityForResult(sIntent,1);
                break;
                case R.id.cancel:
                return false;
            default:
                break;

        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK)
                {

                    int code = data.getIntExtra("code",-1);
                    String msg = data.getStringExtra("msg");
                    int back = data.getIntExtra("back",0);
                    Log.d(TAG, "back = " + back);
                    if(back == 1)
                        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
                    reViewUser();
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mUser = new user();
        mySql = new LocalSQLite(getActivity());
        mydab = mySql.getReadableDatabase();
        if (getArguments() != null) {
            long uid = getArguments().getLong(ARG_PARAM1);
            Log.d(TAG, "muid"+String.valueOf(uid));
            mUser.setmId(uid);
            Cursor cursor = mydab.query(UserContract.UserEntry.TABLE_NAME,null,null,null,null,null,null);
            getUserData(cursor,mUser);
            Log.d(Contants.TAG, "username = " + mUser.getmUserName());
        }

    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(rootView == null){
            rootView = inflater.inflate(R.layout.fragment_user_page,
                    container,
                    false);
        }
        myName = rootView.findViewById(R.id.uName);
        myavatar = rootView.findViewById(R.id.avatar);
        mySex = rootView.findViewById(R.id.sexlabel);
        myInroduce = rootView.findViewById(R.id.introduce);

        // Inflate the layout for this fragment
        reViewUser();
        return rootView;
    }


    private void reViewUser(){
        Cursor cursor = mydab.query(UserContract.UserEntry.TABLE_NAME,null,null,null,null,null,null);
        getUserData(cursor,mUser);
        if(EmptyUtils.isNotEmpty(mUser.getmUserName()))
            myName.setText(mUser.getmUserName());
        if(EmptyUtils.isNotEmpty(mUser.getmSex())) {
            int sexl = mUser.getmSex();
            if(sexl == 1) mySex.setSelected(false);
            else mySex.setSelected(true);
        }
        if(EmptyUtils.isNotEmpty(mUser.getmIntroduce())){
            myInroduce.setText(mUser.getmIntroduce());
        }
        displayImage(mUser.getmAvatar(),myavatar);

    }

    private void displayImage(String imagePath, ImageView picture) {
        if(imagePath!=null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        }else{
            Toast.makeText(rootView.getContext(), "failed to get image", Toast.LENGTH_SHORT).show();
        }

    }


}