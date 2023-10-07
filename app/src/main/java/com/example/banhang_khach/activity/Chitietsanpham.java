package com.example.banhang_khach.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.banhang_khach.Adapter.ProAdapter;
import com.example.banhang_khach.DTO.CartOrderDTO;
import com.example.banhang_khach.DTO.DTO_QlySanPham;
import com.example.banhang_khach.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class Chitietsanpham extends AppCompatActivity {
    String TAG = "chitietsp";
    ImageView img_backsp, img_xemthem, img_pro ,img_favo , img_bl;
    TextView tv_motasp, tv_xemthem, tv_price, tv_name, tv_dialogname, tv_dialogprice, tv_dialogsoluong;
    LinearLayout layout_xemthem, IMGaddCartOrder;
    ArrayList<DTO_QlySanPham> list;
    ProAdapter adapter;
    RecyclerView rcv_pro;
    int soluong;
    int checkaddnull = 0, checkadd = 0;
    String idproduct, nameproduct, priceproduct, informationproduct, imageproduct, soluongkho;

    boolean isMyFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietsanpham);
        Log.d(TAG, "onCreate: Đã vào chi tiết sp");
        Anhxa();

        Intent intent = getIntent();
        idproduct = intent.getStringExtra("id_product");
        nameproduct = intent.getStringExtra("name");
        priceproduct = intent.getStringExtra("price");
        informationproduct = intent.getStringExtra("information");
        imageproduct = intent.getStringExtra("image");
        soluongkho = intent.getStringExtra("soluongkho");
        Log.d(TAG, "idproduct intent: " + idproduct);
        Log.d(TAG, "nameproduct intent: " + nameproduct);
        Log.d(TAG, "priceproduct intent: " + priceproduct);
        Log.d(TAG, "informationproduct intent: " + informationproduct);
        Log.d(TAG, "imageproduct intent: " + imageproduct);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
//            Toast.makeText(context, "You're not logged in", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(firebaseAuth.getUid()).child("Favorites").child(idproduct)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            isMyFavorite = snapshot.exists();
                            if (isMyFavorite) {
                                img_favo.setImageResource(R.drawable.favorite_24);
                            } else {
                                img_favo.setImageResource(R.drawable.baseline_favorite_border_24);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }


            Glide.with(Chitietsanpham.this).load(imageproduct).centerCrop().into(img_pro);
            tv_name.setText("Tên: " + nameproduct);
            tv_price.setText("Giá: " + priceproduct + "đ");
            tv_motasp.setText(informationproduct);

            final int[] count = {0};
            layout_xemthem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (count[0] == 0) {
                        tv_motasp.setMaxLines(1000);
                        tv_xemthem.setText("Thu gọn");
                        img_xemthem.setImageResource(R.drawable.ic_xemthem1);
                        count[0] = 1;
                    } else {
                        tv_motasp.setMaxLines(1);
                        tv_xemthem.setText("Xem thêm");
                        img_xemthem.setImageResource(R.drawable.ic_xemthem);
                        count[0] = 0;
                    }
                }
            });

            img_backsp = findViewById(R.id.img_backsp);
            img_backsp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            rcv_pro = findViewById(R.id.rcv_pro);
            list = new ArrayList<>();
            getDataPro();
            adapter = new ProAdapter(Chitietsanpham.this, list);
            rcv_pro.setNestedScrollingEnabled(false);
            rcv_pro.setAdapter(adapter);

            IMGaddCartOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckCart();
                }
            });
        }
        public void Anhxa () {
            IMGaddCartOrder = findViewById(R.id.addCartOrder);
            img_pro = findViewById(R.id.img_pro);
            tv_price = findViewById(R.id.tv_price);
            tv_name = findViewById(R.id.tv_name);
            tv_motasp = findViewById(R.id.tv_motasp);
            img_xemthem = findViewById(R.id.img_xemthem);
            tv_xemthem = findViewById(R.id.tv_xemthem);
            layout_xemthem = findViewById(R.id.layout_xemthem);
            img_favo = findViewById(R.id.img_favo_chi_tiet);
            img_bl = findViewById(R.id.img_bl);
        }

        public void CheckCart () {
            final Dialog dialog1 = new Dialog(Chitietsanpham.this);
            dialog1.setContentView(R.layout.dialog_addcartorder);
            dialog1.setCancelable(false);

            Window window = dialog1.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (dialog1 != null && dialog1.getWindow() != null) {
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            }

            ImageView btn_close, imgpro, imgtru, imgcong;
            btn_close = dialog1.findViewById(R.id.btn_close);
            Button btn_addcart = dialog1.findViewById(R.id.btn_addcart);
            tv_dialogsoluong = dialog1.findViewById(R.id.tv_soluong);
            imgpro = dialog1.findViewById(R.id.img_pro);
            tv_dialogname = dialog1.findViewById(R.id.tv_name);
            tv_dialogprice = dialog1.findViewById(R.id.tv_price);
            imgtru = dialog1.findViewById(R.id.imgtru);
            imgcong = dialog1.findViewById(R.id.imgcong);

            Glide.with(Chitietsanpham.this).load(imageproduct).centerCrop().into(imgpro);
            tv_dialogname.setText("Tên: " + nameproduct);
            tv_dialogprice.setText("Giá: " + priceproduct + "đ");
            soluong = Integer.parseInt(tv_dialogsoluong.getText().toString().trim());
            Log.d(TAG, "soluong: " + soluong);
            imgcong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int soluong = Integer.parseInt(tv_dialogsoluong.getText().toString().trim()) + 1;
                    if (soluong < 101) {
                        String slmoi = String.valueOf(soluong);
                        tv_dialogsoluong.setText(slmoi);
                    }
                }
            });
            imgtru.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int soluong = Integer.parseInt(tv_dialogsoluong.getText().toString().trim()) - 1;
                    if (soluong > 0) {
                        String slmoi = String.valueOf(soluong);
                        tv_dialogsoluong.setText(slmoi);
                    }
                }
            });
            btn_addcart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddCart();
                    dialog1.dismiss();
                }
            });
            btn_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog1.dismiss();
                }
            });
            dialog1.show();
        }

        private void AddCart () {
            int soluong = Integer.parseInt(tv_dialogsoluong.getText().toString().trim());
            double priceB = Double.parseDouble(priceproduct) * soluong;
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("CartOrder/" + idproduct);
            CartOrderDTO cartOrderDTO = new CartOrderDTO("", idproduct, auth.getUid(), nameproduct, soluong, priceB, imageproduct);
            myRef.setValue(cartOrderDTO, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    Toast.makeText(Chitietsanpham.this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                }
            });
        }
        private void getDataPro () {
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