package com.example.restoran;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.restoran.Adapters.FPager_Adapter;
import com.google.android.material.tabs.TabLayout;

public class LoginRegister extends AppCompatActivity{
    ViewPager viewPager ;
    TabLayout tab;
    FPager_Adapter adapter;
    ProgDialog progDialog;
    Button btnStab;
    TextView tvLR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        viewPager = findViewById(R.id.viewpager);
        tab = findViewById(R.id.tabLayout);
        tvLR = findViewById(R.id.tvLR);
        btnStab = findViewById(R.id.btnStab);
        progDialog = new ProgDialog(LoginRegister.this);
        adapter = new FPager_Adapter(getSupportFragmentManager());
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

        btnStab.setOnClickListener(v -> switchTab());
    }
    @SuppressLint("SetTextI18n")
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
    @SuppressLint("SetTextI18n")
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
}