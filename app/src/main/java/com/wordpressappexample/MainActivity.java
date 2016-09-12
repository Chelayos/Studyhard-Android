package com.wordpressappexample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.DateTimeKeyListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.internal.ObjectConstructor;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    String url = "https://studyhard.tk/wp-json/wp/v2/posts?filter[posts_per_page]=99";
    List<Object> list;
    Gson gson;
    //ProgressDialog progressDialog;
    ListView postList;
    Map<String,Object> mapPost;
    Map<String,Object> mapTitle;
    Map<String,Object> mapDate;
    int postID;
    String postTitle[];
    String date;
    Date date2;
    Date date3;
    String date4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postList = (ListView)findViewById(R.id.postList);

        //progressDialog = new ProgressDialog(MainActivity.this);
        //progressDialog.setMessage("Loading...");
        //progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                gson = new Gson();
                list = (List) gson.fromJson(s, List.class);
                postTitle = new String[list.size()];

                for (int i = 0; i < list.size(); ++i) {
                    mapPost = (Map<String, Object>) list.get(i);
                    mapTitle = (Map<String, Object>) mapPost.get("title");

                    date = mapPost.get("date").toString();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

                    try{
                        date2 = (sdf.parse(date));
                        java.sql.Date date3 = new java.sql.Date(date2.getTime());

                        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy");
                        String formattedDate = formatter.format(date3);

                        date4 = formattedDate;

                    }
                    catch (java.text.ParseException e)
                    {

                    }

                    postTitle[i] = (String) mapTitle.get("rendered") + " " + date4;

                    postList.setAdapter(new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, postTitle));
                    //progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, "Some error occurred", Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
        rQueue.add(request);

        postList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mapPost = (Map<String,Object>)list.get(position);
                postID = ((Double)mapPost.get("id")).intValue();

                Intent intent = new Intent(getApplicationContext(),Post.class);
                intent.putExtra("id", ""+postID);
                startActivity(intent);
            }
        });
    }
}