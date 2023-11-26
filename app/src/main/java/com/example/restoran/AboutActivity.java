package com.example.restoran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initializer();
    }

    private void initializer() {
        ActionBar abar = getSupportActionBar();
        abar.setTitle("About Us");
        abar.setDisplayHomeAsUpEnabled(true);

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