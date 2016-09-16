package com.wordpressappexample;

//import android.app.ProgressDialog;
import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.SimpleCursorAdapter;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Post extends AppCompatActivity {
    WebView view;
    Gson gson;
    WPPost wp;
    String content;
    SimpleDateFormat f1, f2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        final String id = getIntent().getExtras().getString("id");
        view = (WebView)findViewById(R.id.content);

        String url = "https://studyhard.tk/wp-json/wp/v2/posts/"+id+"?_embed=1";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                // Create WPPost Class from JSON
                gson = new Gson();
                wp = gson.fromJson(s, WPPost.class);
                wp = (WPPost) gson.fromJson(s, WPPost.class);

                // Get WPPost content
                content = wp.content.get("rendered").toString();

                // Do we have replies?
                if(wp._embedded.replies != null) {
                    for (Replies r : wp._embedded.replies[0]) {
                        try
                        {
                            f1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            f2 = new SimpleDateFormat("dd.MM.yy");
                            content += "<hr>" + f2.format(f1.parse(r.date));
                        }
                        catch (ParseException e)
                        {
                            //invalid date
                        }
                        content += " " + r.author_name + ":" + r.content.get("rendered");
                    }
                    content = content.replaceAll("<p>", "");
                }

                // set view data
                getSupportActionBar().setTitle(wp.title.get("rendered").toString());
                view.loadData(content,"text/html; charset=UTF-8", null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(Post.this, id, Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(Post.this);
        rQueue.add(request);
    }
}
