package com.example.banhang_khach.Package_Bill.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.banhang_khach.DTO.CartOrderDTO;
import com.example.banhang_khach.Package_Bill.Adapter.Chitietdonhang_Adapter;
import com.example.banhang_khach.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Chitietdonhang_Activity extends AppCompatActivity {
    String TAG = "ChitietdonhangActivity";
    ArrayList<CartOrderDTO> list;
    Chitietdonhang_Adapter adapter;
    ImageView id_back;
    ListView lvhoadon;
    String idbill_hoadon, id_userbill;
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
        id_userbill = intent.getStringExtra("");
        list = new ArrayList<>();
        adapter = new Chitietdonhang_Adapter(Chitietdonhang_Activity.this, list);
        lvhoadon.setAdapter(adapter);
        if (status == 3){
            btnhuydonhang.setVisibility(View.GONE);
        }else if (status == 4){
            btnhuydonhang.setVisibility(View.GONE);
        }
        btnhuydonhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                huyBill();
            }
        });
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

    public void huyBill(){
        new AlertDialog.Builder(this)
                .setTitle("Xóa hóa đơn")
                .setMessage("Bản có chắc chắc muốn xóa đơn hàng này không ?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRefId = database.getReference("BillProduct/" + idbill_hoadon);
                        myRefId.removeValue();
                        for (int j = 0; j < list.size(); j++) {
                            DatabaseReference myRef = database.getReference("CartOrder/" + list.get(j));
                            myRef.removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    Toast.makeText(Chitietdonhang_Activity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }

                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}