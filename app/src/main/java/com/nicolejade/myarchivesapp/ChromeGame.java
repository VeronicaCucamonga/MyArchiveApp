package com.nicolejade.myarchivesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;

import com.nicolejade.myarchivesapp.R;
import com.nicolejade.myarchivesapp.databinding.ActivityChromeGameBinding;

//nicole started
//----------CODE ATTRIBUTION----------
//Author: Chrome Dino game App in Android Studio
//Title: Dalvik Bytes
//Publish Date: 1 July 2020
//URL: https://www.youtube.com/watch?v=rZ-idvvsm_w
//connects the web to the game
public class ChromeGame extends AppCompatActivity {

    private WebView webView;
    private Button backButton;
    private ActivityChromeGameBinding binding;
    String url = "file:///android_asset/index.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chrome_game);

        webView = findViewById(R.id.wv);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
}
//----------CODE ATTRIBUTION----------
//nicole ends
