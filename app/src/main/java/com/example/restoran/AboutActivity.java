package com.example.restoran;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class AboutActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initializer();
    }

    private void initializer() {
        ImageView abt1,abt3,abt2,abt4;
        abt1 = findViewById(R.id.aimg1);
        abt2 = findViewById(R.id.aimg2);
        abt3 = findViewById(R.id.aimg3);
        abt4 = findViewById(R.id.aimg4);
        abt1.setOnClickListener(v -> zoomImg(R.drawable.about_one));
        abt2.setOnClickListener(v -> zoomImg(R.drawable.about_two));
        abt3.setOnClickListener(v -> zoomImg(R.drawable.about_three));
        abt4.setOnClickListener(v -> zoomImg(R.drawable.about_four));
        ActionBar abar = getSupportActionBar();
        abar.setTitle("Welcome to Restoran");
        abar.setElevation(0.0f);
        abar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        abar.setDisplayHomeAsUpEnabled(true);

    }

    private void zoomImg(int imgId) {
        Intent intent = new Intent(AboutActivity.this, ImageViewZoom.class);
        intent.putExtra("img",imgId);
        intent.putExtra("name","Restoran");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            super.onBackPressed();
        }
        else if (item.getItemId()==R.id.support){
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.supp_dialog);
            ImageView ivClose = dialog.findViewById(R.id.ivClose2);
            ivClose.setOnClickListener(v -> dialog.dismiss());
            Button btnEmail = dialog.findViewById(R.id.btnEmail);
            btnEmail.setOnClickListener(v ->{
                emailMethod();
            });
            Button btnCall = dialog.findViewById(R.id.btnCall);
            btnCall.setOnClickListener(v -> {
                calMethod();
            });
            Button btnloc = dialog.findViewById(R.id.btneLoc);
            btnloc.setOnClickListener(v -> {
                locMethod();
            });
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void locMethod() {
        Intent intnt = new Intent(Intent.ACTION_DEFAULT,Uri.parse("geo:0,0?q=New York, USA"));
        startActivity(intnt);
    }

    private void calMethod(){
        Intent calli = new Intent(Intent.ACTION_DIAL);
        calli.setData(Uri.parse("tel:+1233202290183"));
        startActivity(Intent.createChooser(calli,"Call via"));
    }
    private void emailMethod() {
        Intent emaili = new Intent(Intent.ACTION_SEND);
        emaili.setType("sms/rfc822");
        emaili.putExtra(Intent.EXTRA_EMAIL, new String[]{"restoran@gmail.com"});
        emaili.putExtra(Intent.EXTRA_SUBJECT,"Querying");
        emaili.putExtra(Intent.EXTRA_TEXT,"I want to ask you questions about how I can make do this tack");
        startActivity(Intent.createChooser(emaili,"Email via:"));
    }


}