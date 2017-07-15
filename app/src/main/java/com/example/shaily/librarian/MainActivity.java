package com.example.shaily.librarian;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public SQLiteDatabase d;
    public DbHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db=new DbHelper(this);
        d=db.getReadableDatabase();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Intent in =new Intent(this,CheckingDue.class);
        startService(in);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MainActivity.this,AddBook.class);
                startActivity(in);

            }
        });
        new LoadList(this).execute();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class LoadList extends AsyncTask<Void,BookItem,String>{
        Context cx;
        Adapter ladap;
        Activity act;
        ListView list;
        public LoadList(Context ctx){
            this.cx=ctx;
            act=(Activity)ctx;

        }
        @Override
        protected String doInBackground(Void... params) {

            String[] projection = {
                    Contract.FeedEntry.COLUMN_NAME_FINE_PER_DAY,
                    Contract.FeedEntry.COLUMN_NAME_Bname,
                    Contract.FeedEntry.COLUMN_NAME_AUTHOR,
                    Contract.FeedEntry.COLUMN_NAME_DUE
            };

            Cursor cursor=d.query(Contract.FeedEntry.TABLE_NAME,projection,null,null,null,null,null);
            ladap=new Adapter(cx,R.layout.list_item);
            String bn,au,issue,due;
            int tf,yy,mm,dd,fi;
            list=(ListView)act.findViewById(R.id.List);
            while(cursor.moveToNext()){
                fi=cursor.getInt(cursor.getColumnIndex(Contract.FeedEntry.COLUMN_NAME_FINE_PER_DAY));
                bn=cursor.getString(cursor.getColumnIndex(Contract.FeedEntry.COLUMN_NAME_Bname));
                au=cursor.getString(cursor.getColumnIndex(Contract.FeedEntry.COLUMN_NAME_AUTHOR));
                due=cursor.getString(cursor.getColumnIndex(Contract.FeedEntry.COLUMN_NAME_DUE));
                Calendar c = Calendar.getInstance();
                yy = c.get(Calendar.YEAR);
                mm = c.get(Calendar.MONTH);
                dd = c.get(Calendar.DAY_OF_MONTH);
                int days=0;
                // set current date into textview
                String s=(new StringBuilder()
                        // Month is 0 based, just add 1
                         .append(dd).append("/").append(mm + 1).append("/").append(yy))
                        .toString();
                Log.v("fdf",s);
                SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date1 = myFormat.parse(due);
                    Date date2 = myFormat.parse(s);
                    long diff = date2.getTime() - date1.getTime();
                    days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    Log.v("NO OF DAYSSSSSS" ,Integer.toString(days));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int fine=0 ;
                if(days>0){
                    fine=fi*days;
                }

                BookItem item=new BookItem(bn,au,due,fine);
                publishProgress(item);

            }
            return "get_info";
        }



        @Override
        protected void onProgressUpdate(BookItem... values){

            ladap.add(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result){
            if(result.equals("get_info")){
                list.setAdapter(ladap);
            }
            else{
                Toast.makeText(cx,result,Toast.LENGTH_SHORT).show();
            }
        }
    }


}
