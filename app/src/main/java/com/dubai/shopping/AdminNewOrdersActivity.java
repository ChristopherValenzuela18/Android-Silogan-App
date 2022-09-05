package com.dubai.shopping;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminNewOrdersActivity extends AppCompatActivity {
    private RecyclerView ordersList;
    private DatabaseReference ordersRef;
    private int overTotalPrice=0;
    private TextView txtTotalAmount;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);
        txtTotalAmount = (TextView)findViewById(R.id.tots);
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("User view");
        ordersList = findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
   txtTotalAmount.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           txtTotalAmount.setText("Total Price = ₱"+String.valueOf(overTotalPrice));

       }
   });

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
                         Picasso.get().load(model.getImages()).into(holder.userPayimg);
                        holder.userName.setText("Name: "+model.getName());
                        holder.userPhoneNumber.setText("Number: "+model.getPhone());
                        holder.userTotalPrice.setText("Amount = ₱"+model.getTotalAmount());
                        holder.userDateTime.setText("Date: "+model.getDate()+" ");
                        int oneTyprProductTPrice = Integer.valueOf(model.getTotalAmount());
                        overTotalPrice = overTotalPrice + oneTyprProductTPrice;


                        holder.showOrdersBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String uID = getRef(position).getKey();
                                Intent intent = new Intent(AdminNewOrdersActivity.this,AdminUserProductsActivity.class);
                                intent.putExtra("uid",uID);
                                startActivity(intent);
                            }
                        });


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CharSequence options[] =new CharSequence[]{
                                        "Yes",
                                        "No"

                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                                builder.setTitle("Have you shipped this Food Order?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (i==0){


                                            String uID = getRef(position).getKey();
                                            RemoverOrder(uID);



                                        }
                                        else {
                                            finish();
                                        }

                                    }
                                });
                                builder.show();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                        return new AdminOrdersViewHolder(view);
                    }
                };
        ordersList.setAdapter(adapter);
        adapter.startListening();

    }


    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder{

        public TextView userName, userPhoneNumber,userTotalPrice,userDateTime,userShippingAddress,userImg;
        public Button showOrdersBtn,showpayment;
        public ImageView userPayimg;

        public AdminOrdersViewHolder(View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);

            showOrdersBtn = itemView.findViewById(R.id.show_all_product_btn);
            userPayimg = itemView.findViewById(R.id.gcashbtn);

        }
    }
    private void RemoverOrder(String uID)
    {
        ordersRef.child(uID).removeValue();
    }
}
