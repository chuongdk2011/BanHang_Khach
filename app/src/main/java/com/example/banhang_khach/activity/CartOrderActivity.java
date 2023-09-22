package com.example.banhang_khach.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.banhang_khach.Adapter.CartOrderAdapter;
import com.example.banhang_khach.DTO.CartOrderDTO;
import com.example.banhang_khach.DTO.DTO_QlySanPham;
import com.example.banhang_khach.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartOrderActivity extends AppCompatActivity implements CartOrderAdapter.OnclickCheck{
    String TAG = "cartoderactivity";
    RecyclerView rc_listcart;
    TextView tongtien;
    Button muahang;
    ArrayList<CartOrderDTO> list;
    CartOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_order);
        Anhxa();
        list = new ArrayList<>();
        adapter = new CartOrderAdapter(CartOrderActivity.this, list, this);
        rc_listcart.setAdapter(adapter);
        getdatacartorder();
    }

    public void Anhxa(){
        rc_listcart = findViewById(R.id.rc_view);
        tongtien = findViewById(R.id.tv_tonggia);
        muahang = findViewById(R.id.btn_muahang);
    }

    public void getdatacartorder(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefId = database.getReference("CartOrder");
        myRefId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CartOrderDTO categoryDTO = dataSnapshot.getValue(CartOrderDTO.class);
                    Log.d(TAG, "list : " + categoryDTO.getPrice());
                    list.add(categoryDTO);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onCheckbox(CartOrderDTO cartOrderDTO) {

        Log.d(TAG, "onCheckbox: " + cartOrderDTO.getPrice());
        tongtien.setText("Tổng tiền: " +String.valueOf(cartOrderDTO.getPrice()));
    }
}