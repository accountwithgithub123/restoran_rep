package com.example.restoran;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.example.restoran.Adapters.FPager_Adapter;
import com.google.android.material.tabs.TabLayout;

public class LoginRegister extends AppCompatActivity implements Parcelable {
    ViewPager viewPager ;
    TabLayout tab;
    FPager_Adapter adapter;
    LottieAnimationView anim;
    ProgDialog progDialog;
    Button btnStab;
    TextView tvLR;

    public LoginRegister() {

    }

    protected LoginRegister(Parcel in) {
    }

    public static final Creator<LoginRegister> CREATOR = new Creator<LoginRegister>() {
        @Override
        public LoginRegister createFromParcel(Parcel in) {
            return new LoginRegister(in);
        }

        @Override
        public LoginRegister[] newArray(int size) {
            return new LoginRegister[size];
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        viewPager = findViewById(R.id.viewpager);
        anim = findViewById(R.id.animationView);
        tab = findViewById(R.id.tabLayout);
        tvLR = findViewById(R.id.tvLR);
        btnStab = findViewById(R.id.btnStab);
        progDialog = new ProgDialog(LoginRegister.this);
        adapter = new FPager_Adapter(getSupportFragmentManager());
        adapter.setContext(LoginRegister.this,this);
        viewPager.setAdapter(adapter);
        tab.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                changeText();
            }

            @Override
            public void onPageSelected(int position) {
                changeText();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnStab.setOnClickListener(v -> {
            switchTab();
        });
    }
    public void changeText(){
        if (viewPager.getCurrentItem()==0){
            btnStab.setText("Sign Up?");
            tvLR.setText("Don't have an account.");
        }
        else {
            btnStab.setText("Sign In?");
            tvLR.setText("Already have an account");
        }
    }
    public void switchTab(){
        if (viewPager!=null && adapter!=null){
            Log.d("LOGIN", "replaceFragment: Adapter_Was_Not_Null");
            if (viewPager.getCurrentItem()==1){
                btnStab.setText("Sign Up?");
                tvLR.setText("Don't have an account.");
                viewPager.setCurrentItem(0);
                adapter.notifyDataSetChanged();
            }
            else {
                btnStab.setText("Sign In?");
                tvLR.setText("Already have an account");
                viewPager.setCurrentItem(1);
                adapter.notifyDataSetChanged();
            }
        }
        else
            Log.d("LOGIN", "replaceFragment: Adapter_Was_Null");

    }
    public void showLoading(){
        try {
            progDialog.show();
        }
        catch (Exception e){
            Log.e("ProgDialog Error:", "showLoading: " + e.getMessage() );
        }
//        anim.setVisibility(View.VISIBLE);
    }
    public void hideLoading(){
        try {
            progDialog.hide();
        }
        catch (Exception e){
            Log.e("ProgDialog Error:", "showLoading: " + e.getMessage() );
        }
//        anim.setVisibility(View.GONE);
    }
    public void keepLogged() {
        SharedPreferences.Editor editor = getSharedPreferences("Loged",MODE_PRIVATE).edit();
        editor.putBoolean("isloged",true);
        editor.apply();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }
}