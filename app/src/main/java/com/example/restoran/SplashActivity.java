package com.example.restoran;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    Handler handler = new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
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

        runnable = () -> {
            if (isloged){
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
            }
            else{
                startActivity(new Intent(SplashActivity.this,LoginRegister.class));
            }
            finish();
        };
        handler.postDelayed(runnable,1500);
    }

    @Override
    public void onBackPressed() {
        handler.removeCallbacks(runnable);
        super.onBackPressed();
    }
}