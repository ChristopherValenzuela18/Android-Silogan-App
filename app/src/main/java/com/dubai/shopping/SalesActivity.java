package com.dubai.shopping;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dubai.shopping.Model.AdminOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class SalesActivity extends AppCompatActivity {
    private RecyclerView ordersList;
    private DatabaseReference ordersRef;
    private int overTotalPrice=0;
    private TextView txtTotalAmount;
    private TextView monthh,yr;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make this activity, full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Hide the Title bar of this activity screen
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.activity_sales);
        txtTotalAmount = (TextView)findViewById(R.id.textView2);
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("Admin view");
        ordersList = findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
        monthh = findViewById(R.id.month);
        yr = findViewById(R.id.year);




        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        Log.d("Month",dateFormat.format(date));



Date currentime = Calendar.getInstance().getTime();
String Formatted = DateFormat.getDateInstance(DateFormat.FULL).format(currentime);
String[]splitdate = Formatted.split(",");
Log.d("myLOG",currentime.toString());

monthh.setText(splitdate[1]);
        yr.setText(splitdate[2]);
Log.d("myLOG",splitdate[1].trim());
        Log.d("myLOG",splitdate[2].trim());

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options=
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                        .setQuery(ordersRef, AdminOrders.class)
                        .build();
        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model) {

                        holder.userTotalPrice.setText("Total Ammount = ₱"+model.getTotalAmount());
                        int oneTyprProductTPrice = Integer.valueOf(model.getTotalAmount());
                        overTotalPrice = overTotalPrice + oneTyprProductTPrice;
                        txtTotalAmount.setText("Total Sales In this Month \n= ₱"+String.valueOf(overTotalPrice));

                        long cutoff = new Date().getTime() - TimeUnit.MILLISECONDS.convert(30, TimeUnit.DAYS);
                        Query oldItems = ordersRef.orderByChild("totalAmount").endAt(cutoff);
                        oldItems.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                for (DataSnapshot itemSnapshot: snapshot. getChildren()) {
                                    itemSnapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                throw databaseError.toException();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.total,parent,false);
                        return new AdminOrdersViewHolder(view);
                    }
                };
        ordersList.setAdapter(adapter);
        adapter.startListening();

    }


    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder{

        public TextView userTotalPrice;


        public AdminOrdersViewHolder(View itemView) {
            super(itemView);




            userTotalPrice = itemView.findViewById(R.id.order_total_price);

        }
    }
    private void RemoverOrder(String uID)
    {
        ordersRef.child(uID).removeValue();
    }
}
