package com.example.myshow;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

//定义合约类
public final class UserContract {
    private UserContract(){}

    public static class UserEntry implements BaseColumns{
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_UID = "Uid";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_SEX = "sex";
        public static final String COLUMN_NAME_INDRODUCE = "indroduce";
        public static final String COLUMN_NAME_AVATAR = "avatar";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_ADMIN = "admin";
    }


    public static void upUserData(ContentValues values, user mUser, SQLiteDatabase db){
        values.put(UserContract.UserEntry.COLUMN_NAME_USERNAME,mUser.getmUserName());
        values.put(UserContract.UserEntry.COLUMN_NAME_SEX,mUser.getmSex());
        values.put(UserContract.UserEntry.COLUMN_NAME_INDRODUCE,mUser.getmIntroduce());
        values.put(UserContract.UserEntry.COLUMN_NAME_AVATAR,mUser.getmAvatar());
        db.update(UserEntry.TABLE_NAME,values,"uid =?",new String[] {String.valueOf(mUser.getmId())});
        db.close();
    }

    public static void getUserData(Cursor cursor,user mUser){
        int uidIndex = cursor.getColumnIndex(UserEntry.COLUMN_NAME_UID);
        int nameIndex = cursor.getColumnIndex(UserEntry.COLUMN_NAME_USERNAME);
        int sexIndex = cursor.getColumnIndex(UserEntry.COLUMN_NAME_SEX);
        int indroduceIndex = cursor.getColumnIndex(UserEntry.COLUMN_NAME_INDRODUCE);
        int avaterIndex = cursor.getColumnIndex(UserEntry.COLUMN_NAME_AVATAR);
        int i = 0;
        String sexres;
        while(cursor.moveToFirst()){
            if(mUser.getmId() == cursor.getLong(uidIndex)){
                mUser.setmUserName(cursor.getColumnName(nameIndex));
                sexres = cursor.getColumnName(sexIndex);
                if(EmptyUtils.isEmpty(sexres)) mUser.setmSex(0);
                else{
                    if(sexres == "1") mUser.setmSex(1);
                    else mUser.setmSex(0);

                }

                mUser.setmIntroduce(cursor.getColumnName(indroduceIndex));
                mUser.setmAvatar(cursor.getColumnName(avaterIndex));
                break;
            }
            if(i == 200)
                break;

        }
        cursor.close();

    }
}
