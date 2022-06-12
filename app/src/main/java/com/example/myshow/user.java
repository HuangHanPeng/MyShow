package com.example.myshow;

import java.io.Serializable;

public class user implements Serializable {
    private String mUserName = null;
    private String mPassword = null;
    private int mSex;
    private String mAdmin = null;
    private String mIntroduce = null;
    private String mAvatar = null;
    private long mId = 0;
    private String sex;
    public void setmAdmin(String mAdmin) {
        this.mAdmin = mAdmin;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public String getmAdmin() {
        return mAdmin;
    }

    public void setmAvatar(String mAvatar) {
        this.mAvatar = mAvatar;
    }

    public String getmAvatar() {
        return mAvatar;
    }

    public void setmIntroduce(String introduce){this.mIntroduce = introduce;}

    public void setmId(long id){this.mId = id;}

    public void setmUserName(String userName){
        this.mUserName = userName;
    }

    public void setmPassword(String password){
        this.mPassword = password;
    }

    public String getmUserName() {
        return mUserName;
    }

    public String getmPassword(){
        return mPassword;
    }

    public long getmId(){ return  mId; }

    public int getmSex(){ return mSex;}

    String getmIntroduce(){
        return mIntroduce;
    }

    public void setmSex(int mSex) {
        this.mSex = mSex;
    }


}
