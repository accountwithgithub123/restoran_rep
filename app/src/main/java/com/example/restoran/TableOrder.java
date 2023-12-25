package com.example.restoran;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restoran.Adapters.OrdersAdapter;
import com.example.restoran.Fragements.SigninFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TableOrder extends AppCompatActivity {

    SwipeRefreshLayout swpeLay;
    RecyclerView rcview;
    ArrayList<OrderFirebase> orderList;
//    DBHelper dbHelper;
    OrdersAdapter adapter;
    LinearLayout layProg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_order);
        initializer();
        
    }
    @SuppressLint("NotifyDataSetChanged")
    private void initializer() {
        rcview = findViewById(R.id.rcView);
        swpeLay = findViewById(R.id.swpLayout);
        layProg = findViewById(R.id.lprogto);
        
        if (!SigninFragment.connectionAvailable(this)){
            layProg.setVisibility(View.GONE);
            Toast.makeText(TableOrder.this, "No internet connection!", Toast.LENGTH_SHORT).show();
        }else {
            LinearLayout layCloud = findViewById(R.id.layoutCloud);
            layCloud.setVisibility(View.GONE);
        }
        
        ActionBar abar = getSupportActionBar();
        abar.setTitle("Orders Placed");
        abar.setDisplayHomeAsUpEnabled(true);
        
        orderList = new ArrayList<>();

//        dbHelper = new DBHelper(this);
//        orderList = dbHelper.getAllData();
        rcview.setLayoutManager(new LinearLayoutManager(TableOrder.this));
        adapter = new OrdersAdapter(TableOrder.this,orderList);
        rcview.setAdapter(adapter);

        fetchData();

        swpeLay.setOnRefreshListener(() -> {
//            orderList = dbHelper.getAllData();
            fetchData();
//            adapter.notifyDataChanged(orderList);
        });
        
    }

    private void fetchData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.e("Table Order ", "fetchData: User is not null with UID = " + user.getUid());
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference()
                    .child("ordersDetail").child(user.getUid());
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    orderList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        OrderFirebase order = dataSnapshot.getValue(OrderFirebase.class);
                        if (order!=null){
                            Log.e("Order Detail", "OName " + order.getName());
                            orderList.add(order);
                        }
                        else
                            Log.d(TAG, "Order Obj is null in reading dataFromFirebase");
                    }
                    adapter.notifyDataChanged(orderList);
                    layProg.setVisibility(View.GONE);
                    swpeLay.setRefreshing(false);

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    layProg.setVisibility(View.GONE);
                    swpeLay.setRefreshing(false);
                    Toast.makeText(TableOrder.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
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