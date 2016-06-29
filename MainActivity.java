package com.owndevs.neele.neelepossiblemobilechallenge;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static String url = "http://de-coding-test.s3.amazonaws.com/books.json";

    private static final String TAG_TITLE = "title";
    private static final String TAG_AUTHOR = "author";
    private static final String TAG_IMAGEURL = "imageURL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new getBooks().execute();

    }

    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        ArrayList<HashMap<String, String>> bookSale = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> book;
        JSONObject jsonObject;

        String title;
        String author;
        String imageURL;

        if (json != null){
            try {
                jsonObject = new JSONObject(json);

                JSONArray books = jsonObject.getJSONArray("");
                for (int i = 0; i < books.length(); i++){
                    JSONObject cursor = books.getJSONObject(i);

                    title = cursor.getString(TAG_TITLE);
                    if (cursor.getString(TAG_AUTHOR) != "")
                        author = cursor.getString(TAG_AUTHOR);
                    else
                        author = "";
                    if (cursor.getString(TAG_IMAGEURL) != "")
                        imageURL = cursor.getString(TAG_IMAGEURL);
                    else
                        imageURL = "";

                    book = new HashMap<String,String>();
                    book.put(TAG_TITLE, title);
                    book.put(TAG_AUTHOR, author);
                    book.put(TAG_IMAGEURL, imageURL);

                    bookSale.add(book);
                }
                return bookSale;

            }catch (JSONException e){
                e.printStackTrace();
                return null;
            }
        }else {
            Toast.makeText(this, "No HTTP Data received", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private class getBooks extends AsyncTask<Void, Void, Void> {

        ArrayList<HashMap<String, String>> books;
        ProgressDialog proDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Showing progress loading dialog
            proDialog = new ProgressDialog(MainActivity.this);
            proDialog.setMessage("Please wait...");
            proDialog.setCancelable(false);
            proDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            WebRequestHelper webreq = new WebRequestHelper();

            String jsonString = webreq.makeWebServiceCall(url, WebRequestHelper.GET);
            books = ParseJSON(jsonString);
            return null;
        }

        @Override
        protected void onPostExecute(Void requestresult) {
            super.onPostExecute(requestresult);

            if (proDialog.isShowing()){
                proDialog.dismiss();
            }

        }
    }
}


