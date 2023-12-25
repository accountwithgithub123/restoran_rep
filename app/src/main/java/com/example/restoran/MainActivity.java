package com.example.restoran;

import static com.airbnb.lottie.L.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    ImageView ivrotate;
    FloatingActionButton btnBookt;
    String imgUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        initializer();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user!=null){
            if (user.getEmail()!=null){
                tvUname.setText(user.getEmail());
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("ProfileImgLinks")
                        .child(getMyPath(user.getEmail()));
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        imgUrl = dataSnapshot.getValue(String.class);
                        Log.d("readingData from Database", "Value is: " + imgUrl);

                            Log.e("MainActivity", "onCreate: imgUrl === " + imgUrl );
                            picassoFunction(Uri.parse(imgUrl));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Log.e("MainActivity", "onCreate: user.getEmail() === " + user.getEmail() );
            }
            else{
                Log.e("MainActivity", "onCreate: user.getEmail() is  null in main activity" );
            }
            if (user.getPhotoUrl()!=null){
                Log.e("MainActivity", "onCreate: user.getPhotourl() === " + user.getPhotoUrl() );
                picassoFunction(user.getPhotoUrl());
            }else
                Log.e("MainActivity", "onCreate: Photo url is else running ");
        }
        else{
            Log.e("MainActivity", "onCreate: User is null in main activity" );
            Bundle bundle = getIntent().getExtras();
            if (bundle!=null){
                Log.e("MainActivity : ", "getIntent_Data: Email : " + bundle.getString("email") );
                Log.e("MainActivity : ", "getIntent_Data: photoUrl : " + bundle.getString("phurl") );
                tvUname.setText(bundle.getString("email"));
                picassoFunction(Uri.parse(bundle.getString("phurl")));
            }
            else
                Log.e("MainActivity : ", "GetIntent is also null in main Activity ");

        }
        ivrotate.setAnimation(AnimationUtils.loadAnimation(this,R.anim.rotate_img));
        toggle.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
        btnBookt.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,BookTable.class)));

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
                item.setChecked(true);
                return true;
            }
        });


    }
    private String getMyPath(String email) {
        return email.substring(0,email.indexOf('@'));
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
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void initializer() {
        ivrotate = findViewById(R.id.ivhero);
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
}