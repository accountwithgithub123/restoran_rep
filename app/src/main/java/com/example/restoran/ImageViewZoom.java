package com.example.restoran;

import android.content.ContentValues;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

public class ImageViewZoom extends AppCompatActivity {

    ZoomageView ivpic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view_zoom);
        ActionBar abar = getSupportActionBar();
        assert abar != null;
        abar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        abar.setElevation(0.0f);
        abar.setDisplayHomeAsUpEnabled(true);
        ivpic = findViewById(R.id.myZoomageView);
        if (getIntent()!=null){
            abar.setTitle(getIntent().getStringExtra("name"));
            int imgId = getIntent().getIntExtra("img",R.drawable.team1);
            if (imgId!=R.drawable.team1)
                ivpic.setImageResource(imgId);
            else{
                String imgUrl = getIntent().getStringExtra("imgUrl");
                if (imgUrl!=null)
                    picassoFunction(Uri.parse(imgUrl));
            }

        }
    }
    private void picassoFunction(Uri url){
        try {
            Picasso.get()
                    .load(url)
                    .into(ivpic);
            Log.d(ContentValues.TAG, "IMG_URI_Frag_picasso: "+url);
        }
        catch (Exception e){
            Log.e(ContentValues.TAG, "PicassoFunction: " + e.getMessage() );
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