package com.example.shaily.librarian;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Shaily on 28-06-2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 9;
    public DbHelper(Context context) {

        super(context, Contract.FeedEntry.TABLE_NAME, null,DATABASE_VERSION);
        Log.v("in db","in dataaaa");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Contract.FeedEntry.TABLE_NAME
                        + " ( " +
                        Contract.FeedEntry._ID + " INTEGER PRIMARY KEY, " +
                        Contract.FeedEntry.COLUMN_NAME_Bname + " TEXT, " +
                        Contract.FeedEntry.COLUMN_NAME_AUTHOR + " TEXT, " +
                        Contract.FeedEntry.COLUMN_NAME_Issue +" TEXT , " +
                        Contract.FeedEntry.COLUMN_NAME_DUE + " TEXT, " +
                        Contract.FeedEntry.COLUMN_NAME_FINE_PER_DAY +" INT, " +
                        Contract.FeedEntry.COLUMN_NAME_TFINE + " INT )" ;
        db.execSQL(SQL_CREATE_ENTRIES);
        Log.v("Create","TABLE CREATED!!!!!!!!!!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS BookDatabase");
        Log.v("bjbds","in upgrade");
        onCreate(db);
    }




}
