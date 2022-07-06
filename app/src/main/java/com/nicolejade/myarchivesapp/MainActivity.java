package com.nicolejade.myarchivesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.nicolejade.myarchivesapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH = 3300;

    Animation TopAnim, BottomAnim;
    ImageView ImageView;
    TextView Title;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        TopAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        BottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        ImageView = findViewById(R.id.image_view);
        Title = findViewById(R.id.textview_title);

        ImageView.setAnimation(TopAnim);
        Title.setAnimation(BottomAnim);

        firebaseAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUser();
            }
        }, SPLASH);
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            //if user is not logged in then open main screen
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        else{
            //if user is logged in
            startActivity(new Intent(MainActivity.this, ViewCollections.class));
        }
        finish();
    }
}