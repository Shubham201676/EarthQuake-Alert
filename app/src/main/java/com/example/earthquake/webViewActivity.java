package com.example.earthquake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONObject;

public class webViewActivity extends AppCompatActivity {
    WebView webView;
    String  datafile;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView=(WebView)findViewById(R.id.webv);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
      progressBar=(ProgressBar)findViewById(R.id.pb);
      progressBar.setVisibility(View.VISIBLE);
        WebViewClient webViewClient = new WebViewClient();
        webView.setWebViewClient(webViewClient);
        datafile = getIntent().getStringExtra("data");


            try {
                JSONObject object = new JSONObject(datafile);
                String url = object.getString("url");
                webView.loadUrl(url);

                progressBar.setVisibility(View.INVISIBLE);
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
            }


       // Toast.makeText(getApplicationContext(),"received",Toast.LENGTH_SHORT).show();
    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            return false;
        }
    }
}