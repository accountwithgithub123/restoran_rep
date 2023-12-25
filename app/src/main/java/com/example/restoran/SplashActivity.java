package com.example.restoran;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    Handler handler = new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SharedPreferences pref = getSharedPreferences("Loged",MODE_PRIVATE);
        boolean isloged =  pref.getBoolean("isloged",false);
        ImageView ivhost = findViewById(R.id.ghLogo);
        TextView tvhost = findViewById(R.id.tvHost);
        TextView tvload = findViewById(R.id.tvload);
        ivhost.setVisibility(View.VISIBLE);
        tvhost.setVisibility(View.VISIBLE);
        tvload.setVisibility(View.VISIBLE);
        ivhost.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.down_img));
        tvhost.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.upward_text));
        tvload.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink_loading));

        tvhost.setOnClickListener(v ->{
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                });
        runnable = new Runnable() {
            @Override
            public void run() {
                if (isloged){
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                }
                else{
//                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    startActivity(new Intent(SplashActivity.this,LoginRegister.class));
                }
                finish();
            }
        };
        handler.postDelayed(runnable,2500);
    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacks(runnable);
        super.onBackPressed();
    }
}