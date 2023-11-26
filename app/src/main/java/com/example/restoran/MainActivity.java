package com.example.restoran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navView;
    ImageView toggle;
    ImageView ivrotate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializer();
        ivrotate.setAnimation(AnimationUtils.loadAnimation(this,R.anim.rotate_img));
        toggle.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.homme){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else if (item.getItemId()==R.id.about){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                }
                else if (item.getItemId()==R.id.team){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(MainActivity.this, TeamActivity.class));
                }
                else if (item.getItemId()==R.id.tables){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(new Intent(MainActivity.this, TableOrder.class));
                }
                else if (item.getItemId()==R.id.share){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    shareApp();
                }
                else if (item.getItemId()==R.id.rate){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    rateDialog();
                }
                else if (item.getItemId()==R.id.logout){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    logoutDialog();
                }
                return false;
            }
        });


    }
    private void logoutDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Logout!")
                .setMessage("Are you sure?")
                .setIcon(R.drawable.outline_mobile_screen_share_24)
                .setPositiveButton("Logout", (dialog1, which) -> {
                    if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                        FirebaseAuth.getInstance().signOut();
                        SharedPreferences.Editor editor = getSharedPreferences("Loged", MODE_PRIVATE).edit();
                        editor.putBoolean("isloged",false);
                        editor.apply();
                        startActivity(new Intent(MainActivity.this, LoginRegister.class));
                        finish();
                    }
                })
                .setNeutralButton("Cancel", (dialog12, which) -> {

                });
        dialog.show();
    }

    private void rateDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.rate_dialog);
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(v -> dialog.dismiss());
        Button btnNot = dialog.findViewById(R.id.btnNot);
        btnNot.setOnClickListener(v -> dialog.dismiss());
        Button btnRate = dialog.findViewById(R.id.btnrate);
        btnRate.setOnClickListener(v -> {
            Intent updInt = new Intent(Intent.ACTION_DEFAULT, Uri.parse("https://www.play.google.com/store/apps/details?id=com.acedevelopers.figmaexport"));
            startActivity(updInt);
        });
        dialog.show();
    }

    private void initializer() {
        ivrotate = findViewById(R.id.ivhero);
        toggle = findViewById(R.id.toggle);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navView = findViewById(R.id.nav);

    }
    public void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,"I like this app and want you to share this link, https://play.google.com/store/apps/detials?id=com.acedevelopers.figmaexport");
            startActivity(Intent.createChooser(shareIntent,"Share this via:"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}