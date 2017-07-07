package com.example.shaily.librarian;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Shaily on 28-06-2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {

        super(context, Contract.FeedEntry.TABLE_NAME, null, 4);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_ENTRIES =
                "CREATE TABLE BookDb( id INTEGER PRIMARY KEY, " +
                        "BookName TEXT, " +
                        "Author TEXT, " +
                        "IssuedOn , DATE " +
                        "DueDate DATE, " +
                        "Fine INT, " +
                        "Totalfine INT )" ;
        db.execSQL(SQL_CREATE_ENTRIES);
        Log.v("Create","TABLE CREATED!!!!!!!!!!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS BookDb");
        onCreate(db);
    }




}
