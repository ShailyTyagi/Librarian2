package com.example.shaily.librarian;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Shaily on 14-07-2017.
 */

public class CheckingDue extends Service {

    SQLiteDatabase d;
    DbHelper db;


    @Override
    public void onCreate(){
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){

        Log.v("innn serviiiiice","helllllllo");
        db=new DbHelper(this);
        d=db.getReadableDatabase();
        db.onOpen(d);
        String[] projection = {
                Contract.FeedEntry.COLUMN_NAME_FINE_PER_DAY,
                Contract.FeedEntry.COLUMN_NAME_Bname,
                Contract.FeedEntry.COLUMN_NAME_AUTHOR,
                Contract.FeedEntry.COLUMN_NAME_DUE
        };

        Cursor cursor=d.query(Contract.FeedEntry.TABLE_NAME,projection,null,null,null,null,null);


        String bn,au,issue,due;
        int tf,yy,mm,dd,fi;

        while(cursor.moveToNext()) {
            fi = cursor.getInt(cursor.getColumnIndex(Contract.FeedEntry.COLUMN_NAME_FINE_PER_DAY));
            bn = cursor.getString(cursor.getColumnIndex(Contract.FeedEntry.COLUMN_NAME_Bname));
            au = cursor.getString(cursor.getColumnIndex(Contract.FeedEntry.COLUMN_NAME_AUTHOR));
            due = cursor.getString(cursor.getColumnIndex(Contract.FeedEntry.COLUMN_NAME_DUE));
            Calendar c = Calendar.getInstance();
            yy = c.get(Calendar.YEAR);
            mm = c.get(Calendar.MONTH);
            dd = c.get(Calendar.DAY_OF_MONTH);
            int days = 0;
            // set current date into textview
            String s = (new StringBuilder()
                    // Month is 0 based, just add 1
                    .append(dd).append("/").append(mm + 1).append("/").append(yy))
                    .toString();
            // Log.v("fdf",s);
            SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date1 = myFormat.parse(due);
                Date date2 = myFormat.parse(s);
                long diff = date2.getTime() - date1.getTime();
                days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                //Log.v("NO OF DAYSSSSSS" ,Integer.toString(days));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(days>0){
                showNotification();
            }
        }


        return START_STICKY;
    }


    public void showNotification(){

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.camera);
        builder.setContentTitle("Book Due Date Reached");
        builder.setContentText("You have a book which is overdue");
        Intent in=new Intent(this,MainActivity.class);
        TaskStackBuilder tsc=TaskStackBuilder.create(this);
        tsc.addParentStack(MainActivity.class);
        tsc.addNextIntent(in);
        PendingIntent pn= tsc.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pn);
        Log.v("in show "," in noootiiificaation");
        NotificationManager nm=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0,builder.build());
    }
    @Override
    public void onDestroy(){

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
