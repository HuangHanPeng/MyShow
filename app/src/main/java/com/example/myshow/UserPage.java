package com.example.myshow;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
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
    private static final String ARG_PARAM2 = "UNAME";
    private static final String ARG_PARAM3 = "SEX";
    private static final String ARG_PARAM4 = "INRODUCE";
    private  View rootView = null;
    private String imagepath = null;

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
    public static UserPage newInstance(long uid,String uName,String sex, String introduce) {
        UserPage fragment = new UserPage();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, uid);
        args.putString(ARG_PARAM2,uName);
        args.putString(ARG_PARAM3,sex);
        args.putString(ARG_PARAM4,introduce);

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
                sIntent.putExtra("username",mUser.getmUserName());
                sIntent.putExtra("sex",mUser.getmSex());
                sIntent.putExtra("indroduce",mUser.getmIntroduce());
                sIntent.putExtra("ImagePath",imagepath);
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
        if (getArguments() != null) {
            mUser.setmId(getArguments().getLong(ARG_PARAM1));
            mUser.setmUserName(getArguments().getString(ARG_PARAM2));
            mUser.setmSex(getArguments().getString(ARG_PARAM3));
            mUser.setmIntroduce(getArguments().getString(ARG_PARAM4));
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
        if(EmptyUtils.isNotEmpty(mUser.getmUserName()))
            myName.setText(mUser.getmUserName());
        if(EmptyUtils.isNotEmpty(mUser.getmSex())) {
            String sexl = mUser.getmSex();
            if(sexl.equals("1")) mySex.setSelected(false);
            else mySex.setSelected(true);
        }
        if(EmptyUtils.isNotEmpty(mUser.getmIntroduce())){
            myInroduce.setText(mUser.getmIntroduce());
        }
        displayImage(imagepath,myavatar);

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