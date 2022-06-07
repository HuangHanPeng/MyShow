package com.example.myshow;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.security.PublicKey;

public class LocalSQLite extends SQLiteOpenHelper {
    //创建用户表保存用户数据
    public static final String CREATE_USER =
            "CREATE TABLE " + UserContract.UserEntry.TABLE_NAME + " (" +
                    UserContract.UserEntry._ID + " INTEGER PRIMARY KEY, "+
                    UserContract.UserEntry.COLUMN_NAME_UID + " INTERGER, " +
                    UserContract.UserEntry.COLUMN_NAME_USERNAME+" VARCHAR(100), "+
                    UserContract.UserEntry.COLUMN_NAME_SEX+" VARCGAR(100), "+
                    UserContract.UserEntry.COLUMN_NAME_INDRODUCE+" TEXT, " +
                    UserContract.UserEntry.COLUMN_NAME_AVATAR+" TEXT, "+
                    UserContract.UserEntry.COLUMN_NAME_PASSWORD + " TEXT, " +
                    UserContract.UserEntry.COLUMN_NAME_ADMIN + " TEXT " +
                    ")" ;

    public static final String SQL_DELETE_ENTRIST =
            "DROP TABLE IF EXISTS " + UserContract.UserEntry.TABLE_NAME;
    private Context mContext;
    public static final int DATABESE_VERSION = 6;
    public static final String DATABASE_NAME = "user.db";
    public LocalSQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABESE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        Log.d(Contants.TAG, "create db: ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIST);
        onCreate(db);
    }
}
