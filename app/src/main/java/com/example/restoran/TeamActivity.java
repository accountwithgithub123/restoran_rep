package com.example.restoran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class TeamActivity extends AppCompatActivity {
    LinearLayout l1,l2,l3,l4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        initializer();
        onClicks();
    }

    private void onClicks() {
        l1.setOnClickListener(v -> {
            IntentStarter(1);
        });
        l2.setOnClickListener(v -> {
            IntentStarter(2);
        });
        l3.setOnClickListener(v -> {
            IntentStarter(3);
        });
        l4.setOnClickListener(v -> {
            IntentStarter(4);
        });
    }

    private void IntentStarter(int i) {
        Intent intent =new Intent(TeamActivity.this,ChefDetail.class);
        intent.putExtra("chef",i);
        startActivity(intent);
    }

    private void initializer() {
        l1 = findViewById(R.id.l1);
        l2 = findViewById(R.id.l2);
        l3 = findViewById(R.id.l3);
        l4 = findViewById(R.id.l4);
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