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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.banhang_khach.DTO.CartOrderDTO;
import com.example.banhang_khach.DTO.DTO_QlySanPham;
import com.example.banhang_khach.DTO.OrderInformationDTO;
import com.example.banhang_khach.DTO.UserDTO;
import com.example.banhang_khach.Package_Bill.Adapter.Chitietdonhang_Adapter;
import com.example.banhang_khach.R;
import com.example.banhang_khach.VNpay.API;
import com.example.banhang_khach.VNpay.DTO_hoantien;
import com.example.banhang_khach.VNpay.DTO_thanhtoan;
import com.example.banhang_khach.VNpay.DTO_vnpay;
import com.example.banhang_khach.VNpay.Vnpay_Retrofit;
import com.example.banhang_khach.VNpay.WebViewThanhtoan;
import com.example.banhang_khach.activity.Chitietsanpham;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Chitietdonhang_Activity extends AppCompatActivity {
    String TAG = "ChitietdonhangActivity";
    ArrayList<CartOrderDTO> list;
    Chitietdonhang_Adapter adapter;
    ImageView id_back;
    ListView lvhoadon;
    String idbill_hoadon, id_userbill, idthanhtoan;
    int status;
    Button btnhuydonhang;
    TextView tv_sdt, tv_fullname, tv_diachi, tv_hinhthucthanhtoan, tv_giathanhtaon;
    static String url = API.URL;
    static final String BASE_URL = url +"/payment/";
    String Vnp_Amount, Vnp_TxnRef;

    public void Anhxa(){
        lvhoadon = findViewById(R.id.lv_hoadon);
        id_back = findViewById(R.id.id_back);
        btnhuydonhang = findViewById(R.id.btn_huydonhang);
        tv_fullname = findViewById(R.id.tv_hoten);
        tv_sdt = findViewById(R.id.tv_sdt);
        tv_diachi = findViewById(R.id.tv_diachi);
        tv_giathanhtaon = findViewById(R.id.tv_giathanhtaon);
        tv_hinhthucthanhtoan = findViewById(R.id.tv_hinhthucthanhtoan);
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
        id_userbill = intent.getStringExtra("id_user");
        idthanhtoan = intent.getStringExtra("idthanhtoan");

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

                FirebaseAuth auth = FirebaseAuth.getInstance();
                String user = auth.getUid();
                DTO_hoantien dtoHoantien = new DTO_hoantien();
                dtoHoantien.setVnp_CreateBy(user);
                dtoHoantien.setVnp_Amount(Vnp_Amount);
                dtoHoantien.setVnp_TxnRef(Vnp_TxnRef);
                dtoHoantien.setVnp_TransactionType("02");
                Hoantien(dtoHoantien);
            }
        });
        getdata();
        getdiachi();
        billthanhtoan();
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
                .setTitle("Hủy hóa đơn")
                .setMessage("Bản có chắc chắc muốn hủy đơn hàng này không ?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRefId = database.getReference("BillProduct/" + idbill_hoadon);
                            Map<String, Object> map = new HashMap<>();
                            map.put("status", 5);

                            myRefId.updateChildren(map, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    Toast.makeText(Chitietdonhang_Activity.this, "Đã xác nhận", Toast.LENGTH_SHORT).show();
                                }
                            });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    public void getdiachi(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("OrderInformation").orderByChild("id_bill").equalTo(idbill_hoadon);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        OrderInformationDTO orderInformation = issue.getValue(OrderInformationDTO.class);
                        String sdt = orderInformation.getSdt();
                        String fullname = orderInformation.getFullname();
                        String diachi = orderInformation.getDiachi();
                        tv_fullname.setText(sdt);
                        tv_diachi.setText(diachi);
                        tv_fullname.setText(fullname);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void billthanhtoan(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("Thanhtoan").orderByChild("idthanhtoan").equalTo(idthanhtoan);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        DTO_thanhtoan thanhtoan = issue.getValue(DTO_thanhtoan.class);
                        String giathanhtoan = thanhtoan.getVnp_Amount();
                        String hinhthucthanhtoan = thanhtoan.getVnp_CardType();
                        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                        tv_giathanhtaon.setText(decimalFormat.format(Integer.parseInt(giathanhtoan)) + " VND");
                        tv_hinhthucthanhtoan.setText(""+hinhthucthanhtoan);
                        Vnp_Amount = thanhtoan.getVnp_Amount();
                        Vnp_TxnRef = thanhtoan.getVnp_TxnRef();
                        if (thanhtoan.getVnp_Amount().length() == 0 && thanhtoan.getVnp_TxnRef().length() == 0){
                            Log.d(TAG, "1: " + Vnp_Amount);
                            Log.d(TAG, "1: " + Vnp_TxnRef);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void Hoantien(DTO_hoantien dtoHoantien){
        //tạo dđối towngj chuyển đổi kiểu dữ liệu
        Gson gson = new GsonBuilder().setLenient().create();
        //Tạo Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( BASE_URL )
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        //Khởi tạo Interface
        Vnpay_Retrofit vnpayRetrofit = retrofit.create(Vnpay_Retrofit.class);
        //Tạo Call
        Call<DTO_hoantien> objCall = vnpayRetrofit.apirefund(dtoHoantien);

        //Thực hiệnửi dữ liệu lên server
        objCall.enqueue(new Callback<DTO_hoantien>() {
            @Override
            public void onResponse(Call<DTO_hoantien> call, Response<DTO_hoantien> response) {
                //Kết quẳ server trả về ở đây
                if(response.isSuccessful()){
                    DTO_hoantien dtoVnpay = response.body();
                }else {
                    Log.d(TAG, "nguyen1: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DTO_hoantien> call, Throwable t) {
                //Nếu say ra lỗi sẽ thông báo ở đây
                Log.d(TAG, "nguyen2: " + t.getLocalizedMessage());
            }
        });
    }
}