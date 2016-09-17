package tk.studyhard;

import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<Object> list;
    Gson gson;
    ListView postList;
    Map<String, Object> mapPost;
    Map<String, Object> mapTitle;
    int postID;
    String postTitle[];
    SimpleDateFormat f1, f2;
    String date;
    int cat = 0;
    String cat_name = "Home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setTitle(cat_name);
        loadActivity(0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.nav_calendar:
                // activity
                break;
            case R.id.nav_img:
                // another startActivity, this is for item with id "menu_item2"
                break;
            case R.id.cat1:
                loadActivity(1);
                getSupportActionBar().setTitle("News");
                cat_name = "News";
                break;
            case R.id.cat8:
                loadActivity(8);
                getSupportActionBar().setTitle("Mathematik");
                cat_name = "Math";
                break;
            case R.id.cat10:
                loadActivity(10);
                getSupportActionBar().setTitle("Wirtschaft");
                cat_name = "Wirtschaft";
                break;
            case R.id.cat11:
                loadActivity(11);
                getSupportActionBar().setTitle("English");
                cat_name = "Eng";
                break;
            case R.id.cat12:
                loadActivity(12);
                getSupportActionBar().setTitle("Wirtschaftsinformatik");
                cat_name = "WI";
                break;
            case R.id.cat13:
                loadActivity(13);
                getSupportActionBar().setTitle("Software Enigneering");
                cat_name = "SE";
                break;
            case R.id.cat0: default:
                cat = 0;
                loadActivity(0);
                getSupportActionBar().setTitle("Home");
                cat_name = "Home";
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadActivity(final int cat)
    {
        postList = (ListView) findViewById(R.id.postList);

        String url = "https://studyhard.tk/wp-json/wp/v2/posts?filter[posts_per_page]=100";

        if(cat!=0)
        {
            url += "&filter[cat]=" + cat;
        }

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
                intent.putExtra("cat_name", cat_name);
                intent.putExtra("cat", cat);
                startActivity(intent);
            }
        });
    }
}
