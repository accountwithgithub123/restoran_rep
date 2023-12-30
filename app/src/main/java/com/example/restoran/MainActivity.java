package com.example.restoran;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    TextView tvUname;
    CircleImageView profImg;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navView;
    ImageView toggle;
    FloatingActionButton btnBookt;
    String imgUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializer();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            if (user.getEmail()!=null){
                tvUname.setText(user.getEmail());
                imgUrl = String.valueOf(user.getPhotoUrl());
                if (user.getPhotoUrl()!=null){
                    Log.e("MainActivity", "onCreate: user.getPhotourl() === " + user.getPhotoUrl() );
                    picassoFunction(user.getPhotoUrl());
                }else{
                    Log.e("MainActivity", "onCreate: Photo url is else running ");
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("ProfileImgLinks")
                            .child(user.getUid());
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            imgUrl = dataSnapshot.getValue(String.class);
                            Log.d("readingData from Database", "Value is: " + imgUrl);
                            Log.e("MainActivity", "onCreate: imgUrl === " + imgUrl );
                            if (imgUrl!=null)
                                picassoFunction(Uri.parse(imgUrl));
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }

                Log.e("MainActivity", "onCreate: user.getEmail() === " + user.getEmail() );
            }
            else{
                Log.e("MainActivity", "onCreate: user.getEmail() is  null in main activity" );
            }

        }
        toggle.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        btnBookt.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,BookTable.class)));

        profImg.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,ImageViewZoom.class);
            intent.putExtra("name","Profile Image")
                            .putExtra("imgUrl",imgUrl);
            startActivity(intent);
        });
        navView.setNavigationItemSelectedListener(item -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            if (item.getItemId()==R.id.about){
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
            else if (item.getItemId()==R.id.team){
                startActivity(new Intent(MainActivity.this, TeamActivity.class));
            }
            else if (item.getItemId()==R.id.tables){
                startActivity(new Intent(MainActivity.this, TableOrder.class));
            }
            else if (item.getItemId()==R.id.share){
                shareApp();
            }
            else if (item.getItemId()==R.id.rate){
                rateDialog();
            }
            else if (item.getItemId()==R.id.logout){
                logoutDialog();
            }
//            item.setChecked(true);
            return false;
        });


    }

    private void picassoFunction(Uri url){
        try {
            Picasso.get()
                    .load(url)
                    .into(profImg);
            Log.d(ContentValues.TAG, "IMG_URI_Frag_picasso: "+url);
        }
        catch (Exception e){
            Log.e(ContentValues.TAG, "PicassoFunction: " + e.getMessage() );
        }
    }

    private void logoutDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.logout_dialog);
        Button btnNot = dialog.findViewById(R.id.btncanDialog);
        btnNot.setOnClickListener(v -> dialog.dismiss());
        Button btnLogout = dialog.findViewById(R.id.btnlogOut);
        btnLogout.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                FirebaseAuth.getInstance().signOut();
                SharedPreferences.Editor editor = getSharedPreferences("Loged", MODE_PRIVATE).edit();
                editor.putBoolean("isloged",false);
                editor.apply();
                startActivity(new Intent(MainActivity.this, LoginRegister.class));
                finish();
            }
        });
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    private void initializer() {
        btnBookt = findViewById(R.id.btable);
        toggle = findViewById(R.id.toggle);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        navView = findViewById(R.id.nav);

        View headerView = navView.getHeaderView(0);

        tvUname = headerView.findViewById(R.id.tvUname);
        profImg = headerView.findViewById(R.id.mainPimg);



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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}
