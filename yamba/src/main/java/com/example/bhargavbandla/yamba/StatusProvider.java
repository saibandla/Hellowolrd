package com.example.bhargavbandla.yamba;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import winterwell.jtwitter.Twitter;

/**
 * Created by BhargavBandla on 07/02/15.
 */
public class StatusProvider extends ContentProvider {
    public static final String AUTHORITY = "content://com.example.bhargavbandla.yamba.provider";
    public static final Uri CONTENT_URI = Uri.parse(AUTHORITY);
    SQLiteDatabase db;
    DBHelper dbHelper;
    static final String TAG = "Status Provider";
    public static final String DB_NAME = "timeline.db";
    public static final int DB_VERSION = 2;
    public static final String TABLE_NAME = "status";
    public static final String C_ID = "_id";
    public static final String C_CREATED_AT = "created_at";
    public static final String C_USER = "username";
    public static final String C_TEXT = "status_text";


    public static ContentValues statusToValues(Twitter.Status status) {
        ContentValues values = new ContentValues(4);
        values.put(C_ID, status.id);
        values.put(C_CREATED_AT, status.createdAt.getTime());
        values.put(C_USER, status.user.name);
        values.put(C_TEXT, status.text);
        return values;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        db = dbHelper.getReadableDatabase();

        return db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

    }

    @Override
    public String getType(Uri uri) {
        if (uri.getLastPathSegment() == null) {
            return "vnd.android.cursor.item/vnd.example.bhargavbandla.yamba.status";
        } else {
            return "vnd.android.cursor.dir/vnd.example.bhargavbandla.yamba.status";

        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db = dbHelper.getWritableDatabase();
        long id = db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        if (id != -1) {
            return Uri.withAppendedPath(uri, String.valueOf(id));
        } else {
            return uri;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
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
