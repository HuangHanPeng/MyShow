package com.example.myshow;

import java.io.Serializable;

public class user implements Serializable {
    private String mUserName = null;
    private String mPassword = null;
    private String mSex = null;
    private String mIntroduce = null;

    private long mId = 0;

    public void setSex(String sex){this.mSex = sex;}

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

    public String getmSex(){ return mSex;}

    private String getmIntroduce(){return mIntroduce;}
}
