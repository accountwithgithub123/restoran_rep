package com.example.restoran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.jsibbold.zoomage.ZoomageView;

public class ImageViewZoom extends AppCompatActivity {

    ZoomageView ivpic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view_zoom);
        ActionBar abar = getSupportActionBar();
        abar.setDisplayHomeAsUpEnabled(true);
        ivpic = findViewById(R.id.myZoomageView);
        if (getIntent()!=null){
            abar.setTitle(getIntent().getStringExtra("name"));
            ivpic.setImageResource(getIntent().getIntExtra("img",R.drawable.team1));
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}