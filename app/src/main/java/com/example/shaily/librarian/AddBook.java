package com.example.shaily.librarian;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Shaily on 25-06-2017.
 */

public class AddBook extends Activity implements View.OnClickListener{

    private ZXingScannerView scannerview;
    String isbn;
    Button but;
    static  String bname,authname,fine,issuedate,dueafter;
    static EditText bookname;
    static EditText authorname,dueAfter,issueDate,Fine;
    public static String API_URL = "https://www.googleapis.com/books/v1/volumes?q=isbn:";
    public String API_KEY = "AIzaSyDJoXTU9Cb4aAE_2rO7ooQAOkvd6N9kSIY";

    Context con;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_book);
        but=(Button)findViewById(R.id.Button);
        but.setOnClickListener(this);

        bookname=(EditText)findViewById(R.id.E1);
        authorname=(EditText)findViewById(R.id.E2);
        issueDate=(EditText)findViewById(R.id.E3);
        dueAfter=(EditText)findViewById(R.id.E4);
        Fine=(EditText)findViewById(R.id.E5);
    }





    public void ScanCode(View v) {
        scannerview = new ZXingScannerView(this);
        scannerview.setResultHandler(new ZxingScannerResultHandler());
        setContentView(scannerview);
        scannerview.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerview.stopCamera();

        setContentView(R.layout.add_book);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.Button){
            authname=authorname.getText().toString();
            bname=bookname.getText().toString();
            fine=Fine.getText().toString();
            issuedate=issueDate.getText().toString();
            dueafter=dueAfter.getText().toString();
            SaveEntry();
        }
    }

    public class ZxingScannerResultHandler implements ZXingScannerView.ResultHandler {


        @Override
        public void handleResult(Result result) {
            String resultcode = result.getText();
            isbn = resultcode;
            // Toast.makeText(MainActivity.this,resultcode,Toast.LENGTH_SHORT).show();
            setContentView(R.layout.add_book);
            scannerview.stopCamera();
            new ApiCall().execute();

        }
    }

    public class ApiCall extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... params) {


            try {
                URL url = new URL(API_URL + isbn + "&key=" + API_KEY);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String response) {

            bookname=(EditText)findViewById(R.id.E1);
            authorname=(EditText)findViewById(R.id.E2);
            // String title="helllo sexy";
            if (response == null) {
                Toast.makeText(AddBook.this,"Oops! The book isn't in our database!",Toast.LENGTH_SHORT);
                bookname.setText("Oops!Not found");
                authorname.setText("Oops!Not found");

            } else {
                Log.v("hi......",response);
                try {
                    JSONObject jsonResponse;
                    /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                    jsonResponse = new JSONObject(response);

                    /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                    /*******  Returns null otherwise.  *******/
                    JSONArray jsonMainNode = jsonResponse.optJSONArray("items");

                    /*********** Process each JSON Node ************/
                    if(jsonMainNode==null){
                        Toast.makeText(AddBook.this,"Oops! The book isn't in our database!",Toast.LENGTH_SHORT);
                        bookname.setText("Oops!Not found");
                        authorname.setText("Oops!Not found");
                    }
                    else {
                        int lengthJsonArr = jsonMainNode.length();
                        Log.v("sjs", jsonMainNode.getString(0));

                        for (int i = 0; i < lengthJsonArr; i++) {
                            /****** Get Object for each JSON node.***********/
                            JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                            /******* Fetch node values **********/
                            Log.v("hggggggggg", "hsdijaaaaaaaaaaaaaaaaaaaaa");
                            String title = jsonChildNode.optJSONObject("volumeInfo").optString("title").toString();
                            Log.v("shaaaaaaaaa", title);
                            bookname.setText(title);
                            String author = null;
                            JSONArray author_list = jsonChildNode.optJSONObject("volumeInfo").optJSONArray("authors");
                            int len = author_list.length();
                            for (int j = 0; j < len; j++) {
                                if (j == 0)
                                    author = author_list.get(j).toString();
                                else
                                    author += author_list.get(j).toString();
                            }
                            Log.v("jgjdgjg", author);
                            authorname.setText(author);

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    Context context;

    SQLiteDatabase data;

            void SaveEntry() {
                DbHelper db=new DbHelper(this);

                data = db.getWritableDatabase();
                db.onOpen(data);
                //db.onCreate(data);
                ContentValues values = new ContentValues();
                values.put(Contract.FeedEntry.COLUMN_NAME_Bname, bname);
                values.put(Contract.FeedEntry.COLUMN_NAME_AUTHOR, authname);
                values.put(Contract.FeedEntry.COLUMN_NAME_Issue, issuedate);
                values.put(Contract.FeedEntry.COLUMN_NAME_DUE, dueafter);
                values.put(Contract.FeedEntry.COLUMN_NAME_FINE_PER_DAY, fine);
                long rowId = data.insert(Contract.FeedEntry.TABLE_NAME, null, values);

            }



}
