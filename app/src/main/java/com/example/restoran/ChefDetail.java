package com.example.restoran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ChefDetail extends AppCompatActivity {
    ImageView ivPic;
    TextView tvname,tvage,tvexp,tvdesc;
    Button btnEmail,btnPhone,btnLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_detail);
        initializer();
        if (getIntent()!=null){
            int i = getIntent().getIntExtra("chef",0);
            if (i==1){
                setData(R.drawable.team1,"Alexandra Grant","  50 years","  15+ years",getString(R.string.desc),"+923202290183","alexandra@gmail.com","6301 Elgin St. California, US");
            }
            else if (i==2){
                setData(R.drawable.team2,"Javier Ramirez","  53 years","  10+ years",getString(R.string.desc),"+923207654321","javier@gmail.com","6301 Elgin St. California, US");
            }
            else if (i==3){
                setData(R.drawable.team3,"Santos Joseph","  46 years","  8+ years",getString(R.string.desc),"+923017654365","joseph@gmail.com","6301 Elgin St. California, US");
            }
            else if (i==4){
                setData(R.drawable.team4,"David Jguyen","  49 years","  17+ years",getString(R.string.desc),"+923582494365","David@gmail.com","6301 Elgin St. California, US");
            }
        }
        onClicks();
    }

    private void onClicks() {
        btnPhone.setOnClickListener(v -> {
            calMethod();
        });
        btnEmail.setOnClickListener(v -> {
            emailMethod();
        });
        btnLoc.setOnClickListener(v -> {
            locMethod();
        });
    }

    private void locMethod() {
        Intent intnt = new Intent(Intent.ACTION_DEFAULT,Uri.parse("geo:0,0?q=" + btnLoc.getText().toString()));
        startActivity(intnt);
    }
    private void calMethod(){
        Intent calli = new Intent(Intent.ACTION_DIAL);
        calli.setData(Uri.parse("tel:"+btnPhone.getText().toString()));
        startActivity(Intent.createChooser(calli,"Call via"));
    }
    private void emailMethod() {
        Intent emaili = new Intent(Intent.ACTION_SEND);
        emaili.setType("sms/rfc822");
        emaili.putExtra(Intent.EXTRA_EMAIL, new String[]{btnEmail.getText().toString()});
        emaili.putExtra(Intent.EXTRA_SUBJECT,"Querying");
        emaili.putExtra(Intent.EXTRA_TEXT,"I want to ask you questions...");
        startActivity(Intent.createChooser(emaili,"Email via:"));
    }
    private void setData(int team1, String name, String age, String exp, String desc, String no, String em, String add) {
        ivPic.setImageResource(team1);
        tvname.setText(name);
        tvage.setText(age);
        tvexp.setText(exp);
        tvdesc.setText(desc);
        btnPhone.setText(no);
        btnEmail.setText(em);
        btnLoc.setText(add);
    }

    private void initializer() {
        tvname = findViewById(R.id.tvName);
        tvage = findViewById(R.id.tvage);
        tvdesc = findViewById(R.id.tvdesc);
        tvexp = findViewById(R.id.tvexp);
        ivPic = findViewById(R.id.ivPic);
        btnEmail = findViewById(R.id.btntvemail);
        btnPhone = findViewById(R.id.btntvPhone);
        btnLoc = findViewById(R.id.btntvaddress);

        ActionBar abar = getSupportActionBar();
        abar.setTitle("Meet Our Team");
        abar.setDisplayHomeAsUpEnabled(true);

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}