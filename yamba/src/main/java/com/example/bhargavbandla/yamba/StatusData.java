package com.example.bhargavbandla.yamba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import winterwell.jtwitter.Twitter;

/**
 * Created by BhargavBandla on 25/01/15.
 */
public class StatusData {
    static final String TAG = "StatusData";
    public static final String DB_NAME = "timeline.db";
    public static final int DB_VERSION = 2;
    public static final String TABLE_NAME = "status";
    public static final String C_ID = "_id";
    public static final String C_CREATED_AT = "created_at";
    public static final String C_USER = "username";
    public static final String C_TEXT = "status_text";
    Context context;
    DBHelper dbHelper;
    SQLiteDatabase db;

    public StatusData(Context context) {
        this.context = context;
        dbHelper = new DBHelper();
    }

    public void insert(Twitter.Status status) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues(4);
        values.put(C_ID, status.id);
        values.put(C_CREATED_AT, status.createdAt.getTime());
        values.put(C_USER, status.user.name);
        values.put(C_TEXT, status.text);
//        db.insert(TABLE_NAME,null,values);
        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public Cursor query()
    {
        List<Twitter.Status> list=new ArrayList<Twitter.Status>();
        db=dbHelper.getReadableDatabase();
        Cursor cursor=db.query(TABLE_NAME, null, null, null, null, null, C_CREATED_AT + " DESC");

        return cursor;
    }
    class DBHelper extends SQLiteOpenHelper {

        public DBHelper() {
            super(context, DB_NAME, null, DB_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                String sql = String.format("create table %s (%s int primary key,%s int,%s text,%s text)", TABLE_NAME, C_ID, C_CREATED_AT, C_USER, C_TEXT);

                db.execSQL(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //Usually alters database
            Log.d(TAG, "on upgrade from " + oldVersion + "to" + newVersion);
            db.execSQL("drop table if exists " + TABLE_NAME);
            onCreate(db);
        }
    }
}
