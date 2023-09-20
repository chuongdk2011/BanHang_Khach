package com.example.banhang_khach.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.banhang_khach.Adapter.ProAdapter;
import com.example.banhang_khach.DTO.DTO_QlySanPham;
import com.example.banhang_khach.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chitietsanpham extends AppCompatActivity {
    String TAG = "chitietsp";
    ImageView img_backsp, img_xemthem;
    TextView tv_motasp, tv_xemthem;
    LinearLayout layout_xemthem;
    ArrayList<DTO_QlySanPham> list;
    ProAdapter adapter;
    RecyclerView rcv_pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietsanpham);
        Log.d(TAG, "onCreate: Đã vào chi tiết sp");


        img_xemthem = findViewById(R.id.img_xemthem);
        tv_xemthem = findViewById(R.id.tv_xemthem);
        layout_xemthem = findViewById(R.id.layout_xemthem);
        final int[] count = {0};
        layout_xemthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count[0] == 0){
                    tv_motasp.setMaxLines(1000);
                    tv_xemthem.setText("Thu gọn");
                    img_xemthem.setImageResource(R.drawable.ic_xemthem1);
                    count[0] = 1;
                }else{
                    tv_motasp.setMaxLines(1);
                    tv_xemthem.setText("Xem thêm");
                    img_xemthem.setImageResource(R.drawable.ic_xemthem);
                    count[0] = 0;
                }
            }
        });
        tv_motasp = findViewById(R.id.tv_motasp);

        img_backsp = findViewById(R.id.img_backsp);
        img_backsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        rcv_pro =findViewById(R.id.rcv_pro);

        list= new ArrayList<>();
        getDataPro();
        adapter = new ProAdapter(Chitietsanpham.this,list);
        rcv_pro.setAdapter(adapter);
    }
    private void getDataPro() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Products");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot :
                        snapshot.getChildren()) {

                    DTO_QlySanPham sanPham = dataSnapshot.getValue(DTO_QlySanPham.class);
                    list.add(sanPham);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}