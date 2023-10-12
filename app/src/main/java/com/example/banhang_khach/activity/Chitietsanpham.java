package com.example.banhang_khach.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.banhang_khach.Adapter.CmtAdapter;
import com.example.banhang_khach.Adapter.ProAdapter;
import com.example.banhang_khach.DTO.BillDTO;
import com.example.banhang_khach.DTO.CartOrderDTO;
import com.example.banhang_khach.DTO.CommentDTO;
import com.example.banhang_khach.DTO.DTO_QlySanPham;
import com.example.banhang_khach.DTO.UserDTO;
import com.example.banhang_khach.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class Chitietsanpham extends AppCompatActivity {
    String TAG = "chitietsp";
    ImageView img_backsp, img_xemthem, img_pro, img_favo, img_bl,img_chat;
    TextView tv_motasp, tv_xemthem, tv_price, tv_name, tv_dialogname, tv_dialogprice, tv_dialogsoluong;
    LinearLayout layout_xemthem, IMGaddCartOrder, llmuahang;
    ArrayList<DTO_QlySanPham> list;
    ProAdapter adapter;
    RecyclerView rcv_pro;
    ArrayList<CommentDTO> listCMT;
    CmtAdapter adapterCMT;
    RecyclerView rcv_cmt;
    int slbl;
    int soluong;
    int checkaddnull = 0;
    String idproduct, nameproduct, priceproduct, informationproduct, imageproduct, soluongkho;

    boolean isMyFavorite = false;
    EditText ed_cmt;
    UserDTO userDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietsanpham);

        Anhxa();

        Intent intent = getIntent();
        idproduct = intent.getStringExtra("id_product");
        nameproduct = intent.getStringExtra("name");
        priceproduct = intent.getStringExtra("price");
        informationproduct = intent.getStringExtra("information");
        imageproduct = intent.getStringExtra("image");
        soluongkho = intent.getStringExtra("soluongkho");

        listCMT = new ArrayList<>();

        userDTO = new UserDTO();
        getUserCur();
        getListBlByPro(idproduct);
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

        getCheck();
        IMGaddCartOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkaddnull == 0){
                    AddCart();
                }else {
                    Toast.makeText(Chitietsanpham.this,"Đã có sản phẩm trong giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }
        });
        llmuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BtnMuaHang();
            }
        });
        img_bl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showComment();
            }
        });
        img_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Chitietsanpham.this, ChatActivity.class));

            }
        });
    }
    private void getCheck() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("CartOrder").orderByChild("id_product").equalTo(idproduct);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        CartOrderDTO cartOrderDTO = issue.getValue(CartOrderDTO.class);
                        if(cartOrderDTO.getIdBill().equalsIgnoreCase("") == true){
                            Log.d(TAG, "onDataChange: " + (cartOrderDTO.getIdBill().equalsIgnoreCase("") == true));
                            checkaddnull = 1;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void Anhxa() {
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
        llmuahang = findViewById(R.id.ll_muahang);
        img_chat = findViewById(R.id.img_chat);
    }

    private void showComment() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Chitietsanpham.this);

        View view = getLayoutInflater().inflate(R.layout.dialog_comment, null);
        bottomSheetDialog.setContentView(view);


        float screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        float halfScreenHeight = screenHeight / 1.5f;
        View bottomSheetInternal = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheetInternal != null) {
            ViewGroup.LayoutParams layoutParams = bottomSheetInternal.getLayoutParams();

            if (layoutParams != null) {
                layoutParams.height = (int) halfScreenHeight;
                bottomSheetInternal.setLayoutParams(layoutParams);

            }
        }


        ImageView img_close = view.findViewById(R.id.img_close);
        TextView tv_slcmt = view.findViewById(R.id.tv_slcmt);
        rcv_cmt = view.findViewById(R.id.rcv_comment);
        ed_cmt = view.findViewById(R.id.ed_cmt);
        ImageView img_send = view.findViewById(R.id.img_send);
        TextView tv_nullCMT = view.findViewById(R.id.tv_nullBL);

        adapterCMT = new CmtAdapter(listCMT, Chitietsanpham.this);
        rcv_cmt.setAdapter(adapterCMT);
        adapterCMT.notifyDataSetChanged();
        if (listCMT.size()==0) {
            tv_nullCMT.setVisibility(View.VISIBLE);
            rcv_cmt.setVisibility(View.GONE);
        } else {
            tv_nullCMT.setVisibility(View.GONE);
            rcv_cmt.setVisibility(View.VISIBLE);
        }
        slbl = listCMT.size();
        Log.d(TAG, "showComment: "+listCMT.size());
        tv_slcmt.setText(slbl + " bình luận");

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        img_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddComment();
            }
        });

        bottomSheetDialog.show();
    }

    private void AddComment() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Comments");

        String idBL = myRef.push().getKey();
        CommentDTO commentDTO = new CommentDTO();

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Trong Java, tháng bắt đầu từ 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);


        String timeCur = hour + ":" + minute + " " + day + "/" + month + "/" + year;
        commentDTO.setContent(ed_cmt.getText().toString());
        commentDTO.setDate(timeCur);
        commentDTO.setIdproduct(idproduct);
        commentDTO.setUserDTO(userDTO);
        commentDTO.setId(idBL);

        myRef.child(idBL).setValue(commentDTO, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                ed_cmt.setText("");
                adapterCMT = new CmtAdapter(listCMT, Chitietsanpham.this);
                rcv_cmt.setAdapter(adapterCMT);
                adapterCMT.notifyDataSetChanged();

            }
        });
    }

    private void getUserCur() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String idU = user.getUid();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        DatabaseReference objectRef = mDatabase.child(idU);
        objectRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    userDTO = dataSnapshot.getValue(UserDTO.class);

                    Log.d("chuongdk", "onDataChange:  Name user = " + userDTO.getFullname());
                    Log.d("chuongdk", "onDataChange: " + userDTO.getId());
                } else {
                    Toast.makeText(Chitietsanpham.this, "Id người dùng không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi ở đây
                Log.w(TAG, "loadObject:onCancelled", databaseError.toException());
            }
        });
    }

    private void getListBlByPro(String idPro) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("Comments").orderByChild("idproduct").equalTo(idPro);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    listCMT.clear();
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        CommentDTO commentDTO = issue.getValue(CommentDTO.class);
                        listCMT.add(commentDTO);
                    }
                    Log.d("chuongdk", "onDataChange: "+listCMT.size());

                    slbl = listCMT.size();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void BtnMuaHang() {
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
                MuaHang();
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

    private void AddCart() {
        Double gia = Double.parseDouble(priceproduct);
        UUID uuid = UUID.randomUUID();
        String idu = uuid.toString().trim();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("CartOrder/" + idu);
        CartOrderDTO cartOrderDTO = new CartOrderDTO(idu, "", idproduct, auth.getUid(), nameproduct, 1, gia, imageproduct);
        myRef.setValue(cartOrderDTO, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(Chitietsanpham.this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
            }
        });
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
    public void MuaHang(){
        int soluong = Integer.parseInt(tv_dialogsoluong.getText().toString().trim());
        double priceB = Double.parseDouble(priceproduct) * soluong;
        UUID uuid = UUID.randomUUID();
        String udi = uuid.toString().trim();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef_Bill = database.getReference("BillProduct/" + udi);
        BillDTO billDTO = new BillDTO(udi, auth.getUid(), priceB,date, 1);
        myRef_Bill.setValue(billDTO, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(Chitietsanpham.this, "Cảm ơn bạn đã đặt hàng", Toast.LENGTH_SHORT).show();
            }
        });

        UUID uuid1 = UUID.randomUUID();
        String idu = uuid1.toString().trim();
        DatabaseReference myRef = database.getReference("CartOrder/" + idu);
        CartOrderDTO cartOrderDTO = new CartOrderDTO(idu, udi, idproduct, auth.getUid(), nameproduct, soluong, priceB, imageproduct);
        myRef.setValue(cartOrderDTO);
    }


}