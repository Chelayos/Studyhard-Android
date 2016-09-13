package com.wordpressappexample;

//import android.app.ProgressDialog;
import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Post extends AppCompatActivity {
    WebView content;
    //ProgressDialog progressDialog;
    Gson gson;
    Map<String, Object> mapPost;
    Map<String, Object> mapTitle;
    Map<String, Object> mapContent;
    Map<String, Object> map_embedded;
    ArrayList map_replies;

    WPPost wp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        final String id = getIntent().getExtras().getString("id");
        content = (WebView)findViewById(R.id.content);

        String url = "https://studyhard.tk/wp-json/wp/v2/posts/"+id+"?_embed=1";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                gson = new Gson();
                mapPost = (Map<String, Object>) gson.fromJson(s, Map.class);
                mapTitle = (Map<String, Object>) mapPost.get("title");
                mapContent = (Map<String, Object>) mapPost.get("content");

                map_embedded = (Map<String, Object>) mapPost.get("_embedded");

                // Do we have replies?
                if(map_embedded.containsKey("replies"))
                {
                    map_replies = (ArrayList) map_embedded.get("replies");
                    content.loadData(map_replies.toString(), "text/html; charset=UTF-8", null);
                }

                //wp = (WPPost) gson.fromJson(s, WPPost.class);
                //content.loadData(wp.title.toString(),"text/html; charset=UTF-8", null);

                //set data
                getSupportActionBar().setTitle(mapTitle.get("rendered").toString());
                //content.loadData(mapContent.get("rendered").toString(), "text/html; charset=UTF-8", null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //progressDialog.dismiss();
                Toast.makeText(Post.this, id, Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Post.this);
        rQueue.add(request);
    }
}