package tk.studyhard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class Timetable extends AppCompatActivity {

    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        getSupportActionBar().setTitle("Stundenplan");
        webView = (WebView) findViewById(R.id.view_calendar);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.loadUrl("https://studyhard.tk/timetable/i.jpg");
    }
}
