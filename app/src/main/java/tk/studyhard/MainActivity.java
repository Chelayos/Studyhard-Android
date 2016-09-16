package tk.studyhard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String url = "https://studyhard.tk/wp-json/wp/v2/posts?filter[posts_per_page]=99";
    List<Object> list;
    Gson gson;
    ListView postList;
    Map<String,Object> mapPost;
    Map<String,Object> mapTitle;
    int postID;
    String postTitle[];
    SimpleDateFormat f1, f2;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postList = (ListView)findViewById(R.id.postList);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                gson = new Gson();
                list = (List) gson.fromJson(s, List.class);
                postTitle = new String[list.size()];

                for (int i = 0; i < list.size(); ++i) {
                    mapPost = (Map<String, Object>) list.get(i);
                    mapTitle = (Map<String, Object>) mapPost.get("title");

                    try
                    {
                        f1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        f2 = new SimpleDateFormat("dd.MM.yy");
                        date = f2.format(f1.parse(mapPost.get("date").toString()));
                    }
                    catch (java.text.ParseException e)
                    {
                        // invalid date
                    }

                    postTitle[i] = (String) mapTitle.get("rendered") + " " + date;
                    postList.setAdapter(new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, postTitle));
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