package com.example.banhang_khach.Package_Bill.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.banhang_khach.DTO.CartOrderDTO;
import com.example.banhang_khach.Package_Bill.Adapter.Chitietdonhang_Adapter;
import com.example.banhang_khach.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chitietdonhang_Activity extends AppCompatActivity {
    String TAG = "ChitietdonhangActivity";
    ArrayList<CartOrderDTO> list;
    Chitietdonhang_Adapter adapter;
    ImageView id_back;
    ListView lvhoadon;
    String idbill_hoadon;
    int status;
    Button btnhuydonhang;
    public void Anhxa(){
        lvhoadon = findViewById(R.id.lv_hoadon);
        id_back = findViewById(R.id.id_back);
        btnhuydonhang = findViewById(R.id.btn_huydonhang);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietdonhang);
        Anhxa();
        id_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        
        Intent intent = getIntent();
        idbill_hoadon = intent.getStringExtra("id_bill");
        status = intent.getIntExtra("status", 1);
        Log.d(TAG, "id_bill: " + status);
        list = new ArrayList<>();
        adapter = new Chitietdonhang_Adapter(Chitietdonhang_Activity.this, list);
        lvhoadon.setAdapter(adapter);
        if (status == 3){
            btnhuydonhang.setEnabled(false);
            btnhuydonhang.setText("Không thể hủy đơn");
        }else if (status == 4){
            btnhuydonhang.setEnabled(false);
            btnhuydonhang.setText("Cảm ơn quý khách");
        }
        getdata();
    }
    public void getdata(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefId = database.getReference("CartOrder");
        myRefId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CartOrderDTO cartOrderDTO = dataSnapshot.getValue(CartOrderDTO.class);
                    if (idbill_hoadon.equals(cartOrderDTO.getIdBill())){
                        list.add(cartOrderDTO);
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "err: " + error);
            }
        });
    }

}