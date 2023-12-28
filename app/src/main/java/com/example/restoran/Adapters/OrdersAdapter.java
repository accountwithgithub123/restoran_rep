package com.example.restoran.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restoran.BookTable;
import com.example.restoran.Fragements.SigninFragment;
import com.example.restoran.OrderFirebase;
import com.example.restoran.ProgDialog;
import com.example.restoran.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    Context context;
    ArrayList<OrderFirebase> ordersList;

    @SuppressLint("NotifyDataSetChanged")
    public void notifyDataChanged(ArrayList<OrderFirebase> list){
        ordersList = list;
        notifyDataSetChanged();
    }

    public OrdersAdapter(Context context, ArrayList<OrderFirebase> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_detail_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.ViewHolder holder, int position) {
        OrderFirebase order = ordersList.get(position);

        holder.tvname.setText(order.getName());
        holder.tvemail.setText(order.getEmail());
        holder.tvdate.setText(order.getDate());
        holder.tvtime.setText(order.getTime());
        holder.tvguest.setText(order.getNumberOfGuests());
        holder.tvreq.setText(order.getRequest());
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookTable.class);
            intent.putExtra("obj", (Parcelable) order);
            context.startActivity(intent);
//            Toast.makeText(context, "Edit this", Toast.LENGTH_SHORT).show();
        });
        holder.btnDelete.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Delete this order")
                    .setMessage("Are you sure?")
                    .setIcon(R.drawable.baseline_info_24)
                    .setPositiveButton("Yes Delete", (dialog1, which) -> {
                        if (SigninFragment.connectionAvailable(context)){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                Log.e("Deleting Table ", "DelteOrder: User is not null with UID = "+ user.getUid() );
                                ProgDialog prog = new ProgDialog(context);
                                prog.show();

                                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference()
                                        .child("ordersDetail").child(user.getUid());
                                myRef.child(order.getFbKey()).removeValue().addOnSuccessListener(unused -> {
                                        prog.dismiss();
                                        Toast.makeText(context, "Order deleted successfully.", Toast.LENGTH_SHORT).show();
                                        Log.e("Deleting Table ", "Order deleted Successfully! ");
                                        })
                                        .addOnFailureListener(e -> {
                                            prog.dismiss();
                                            Toast.makeText(context, "Error while deleting order!", Toast.LENGTH_SHORT).show();
                                            Log.e("Deleting Table ", "Error while deleting order! " + e.getMessage());
                                        });
                            }
                            else{
                                Log.e("Deleting Table ", "User is null" );
                            }
                        }
                        else{
                            Toast.makeText(context, "No internet connection!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNeutralButton("Cancel", (dialog12, which) -> {
                    });
            dialog.show();
//            Toast.makeText(context, "Delete this", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvsrno,tvname,tvemail,tvdate,tvtime,tvguest,tvreq;
        ImageButton btnEdit,btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            tvsrno = itemView.findViewById(R.id.srno);
            tvname = itemView.findViewById(R.id.tvoname);
            tvemail = itemView.findViewById(R.id.tvoemail);
            tvdate = itemView.findViewById(R.id.tvoDate);
            tvtime = itemView.findViewById(R.id.tvoTime);
            tvguest = itemView.findViewById(R.id.tvoguest);
            tvreq = itemView.findViewById(R.id.tvoreq);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btndelete);
        }
    }
}
