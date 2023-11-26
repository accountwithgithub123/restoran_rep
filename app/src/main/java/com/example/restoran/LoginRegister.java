package com.example.restoran;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.restoran.Adapters.FPager_Adapter;
import com.example.restoran.Fragements.SginUpFragment;
import com.example.restoran.Fragements.SigninFragment;
import com.google.android.material.tabs.TabLayout;

public class LoginRegister extends AppCompatActivity {
    ViewPager viewPager ;
    TabLayout tab;
    FPager_Adapter adapter;
    LottieAnimationView anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        viewPager = findViewById(R.id.viewpager);
        anim = findViewById(R.id.animationView);
        tab = findViewById(R.id.tabLayout);
        adapter = new FPager_Adapter(getSupportFragmentManager());
        adapter.setContext(LoginRegister.this,this);
        viewPager.setAdapter(adapter);
        tab.setupWithViewPager(viewPager);
    }
    public void switchTab(){
        if (viewPager!=null && adapter!=null){
            Log.d("LOGIN", "replaceFragment: Adapter_Was_Not_Null");
            if (viewPager.getCurrentItem()==1){
                viewPager.setCurrentItem(0);
                adapter.notifyDataSetChanged();
            }
        }
        else
            Log.d("LOGIN", "replaceFragment: Adapter_Was_Null");

    }
    public void showLoading(){
        anim.setVisibility(View.VISIBLE);
    }
    public void hideLoading(){
        anim.setVisibility(View.GONE);
    }
    public void keepLogged() {
        SharedPreferences.Editor editor = getSharedPreferences("Loged",MODE_PRIVATE).edit();
        editor.putBoolean("isloged",true);
        editor.apply();
    }

}