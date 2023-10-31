package com.example.banhang_khach.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.banhang_khach.DTO.BillDTO;
import com.example.banhang_khach.DTO.CartOrderDTO;
import com.example.banhang_khach.DTO.DTO_QlySanPham;
import com.example.banhang_khach.DTO.UserDTO;
import com.example.banhang_khach.R;
import com.example.banhang_khach.Zalopay.Api.CreateOrder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class BuyNow_Activity extends AppCompatActivity {
    String TAG  = "buynow";
    TextView tv_dialogname, tv_dialogprice, tv_dialogsoluong;
    ImageView btn_close, imgpro, imgtru, imgcong;
    Button btnPay, btn_addcart;
    int soluong;
    String idproduct, nameproduct, priceproduct, informationproduct, imageproduct;
    //điịa chỉ
    TextView tv_sdt, tv_fullname, tv_diachi, tv_thongbao, tv_sua, tv_thanhtien;
    //String check thông tin
    String str_hoten = "1", str_sdt = "1",str_diachi = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_now);
        Anhxa();

        Intent intent = getIntent();
        idproduct = intent.getStringExtra("id_product");

        getchitietsanpham();
        Muahang();
        ThanhtoanZaloPay();
        diachi();
        tv_sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(BuyNow_Activity.this, InformationActivity.class);
                startActivity(intent1);
            }
        });
    }
    public void Anhxa(){
        btnPay = findViewById(R.id.btntesst);
        btn_close = findViewById(R.id.id_back);
        btn_addcart = findViewById(R.id.btn_addcart);
        tv_dialogsoluong = findViewById(R.id.tv_soluong);
        imgpro = findViewById(R.id.img_pro);
        tv_dialogname = findViewById(R.id.tv_name);
        tv_dialogprice = findViewById(R.id.tv_price);
        imgtru = findViewById(R.id.imgtru);
        imgcong = findViewById(R.id.imgcong);
        tv_fullname = findViewById(R.id.tv_hoten);
        tv_sdt = findViewById(R.id.tv_sdt);
        tv_diachi = findViewById(R.id.tv_diachi);
        tv_thongbao = findViewById(R.id.tv_thongbao);
        tv_sua = findViewById(R.id.tv_sua);
        tv_thanhtien = findViewById(R.id.tv_thanhtien);
    }
    public void diachi(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Đặt ID của người dùng bạn muốn lấy thông tin
        DatabaseReference userRef = databaseReference.child("Users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Dữ liệu người dùng được tìm thấy
                    UserDTO user = dataSnapshot.getValue(UserDTO.class);

                    // Kiểm tra xem có dữ liệu số điện thoại, tuổi và địa chỉ không
                    if (user != null) {
                        if (user != null) {
                            int phone = user.getPhone();
                            int defaultValue = -1; // Sử dụng -1 làm giá trị mặc định

                            if (phone != defaultValue) {
                                String phoneText = "Số điện thoại:(+84) " + String.valueOf(phone);
                                tv_sdt.setText(phoneText);
                            } else {
                                tv_sdt.setVisibility(View.GONE);
                                str_sdt = "0";
                                Log.d(TAG, "str_sdt: " + str_sdt);
                            }
                        } else {
                            // Người dùng không tồn tại hoặc lỗi xảy ra
                        }
                        if (user.getFullname() != null) {
                            String fullname = user.getFullname();
                            String fullnameText = "Họ tên: " + fullname;
                            tv_fullname.setText(fullnameText);
                            tv_fullname.setVisibility(View.VISIBLE); // Hiển thị TextView
                        } else {
                            // Dữ liệu full name chưa điền, hiển thị "chưa điền" và giữ TextView không bị ẩn
                            tv_fullname.setText("FullName: chưa điền");
                            tv_fullname.setVisibility(View.VISIBLE);
                            str_hoten = "0";
                            Log.d(TAG, "str_hoten: " + str_hoten);
                        }
                        if (user.getAdress() != null) {
                            String diachi = user.getAdress();
                            String diachiText = "Diachi: " + diachi;
                            tv_diachi.setText(diachiText);
                            tv_diachi.setVisibility(View.VISIBLE); // Hiển thị TextView
                        } else {
                            // Dữ liệu Address chưa điền, hiển thị "chưa điền" và giữ TextView không bị ẩn
                            tv_diachi.setText("Diachi: chưa điền");
                            tv_diachi.setVisibility(View.VISIBLE); // Hiển thị TextView
                            str_diachi = "0";
                            Log.d(TAG, "str_diachi: " + str_diachi);
                        }
                    } else {
                        // Người dùng không tồn tại hoặc lỗi xảy ra
                    }
                } else {
                    // Người dùng không
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }
    public void getchitietsanpham(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Log.d(TAG, "getchitietsanpham: " + idproduct);

        Query query = reference.child("Products").orderByChild("id").equalTo(idproduct);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        DTO_QlySanPham dto_qlySanPham = issue.getValue(DTO_QlySanPham.class);
                        idproduct = dto_qlySanPham.getId();
                        priceproduct = dto_qlySanPham.getPrice();
                        nameproduct = dto_qlySanPham.getName();
                        imageproduct = dto_qlySanPham.getImage();
                        informationproduct = dto_qlySanPham.getInformation();
                        Glide.with(BuyNow_Activity.this).load(imageproduct).centerCrop().into(imgpro);
                        tv_dialogname.setText("Tên: " + nameproduct);
                        tv_dialogprice.setText("Giá: " + priceproduct + "đ");
                        tv_thanhtien.setText("Thành tiền: " +priceproduct + " VND");
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void Muahang(){
        soluong = Integer.parseInt(tv_dialogsoluong.getText().toString().trim());
        imgcong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int soluong = Integer.parseInt(tv_dialogsoluong.getText().toString().trim()) + 1;
                if (soluong < 101) {
                    String slmoi = String.valueOf(soluong);
                    double thanhtien = Double.parseDouble(priceproduct) * soluong;
                    tv_thanhtien.setText("Thành tiền: " +thanhtien + " VND");
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
                    double thanhtien = Double.parseDouble(priceproduct) * soluong;
                    tv_thanhtien.setText("Thành tiền: " +thanhtien + " VND");
                    tv_dialogsoluong.setText(slmoi);
                }
            }
        });

        btn_addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (str_sdt.equals("0") || str_hoten.equals("0") || str_diachi.equals("0")){
                    tv_thongbao.setText("Bạn phải nhập đủ thông tin nhận hàng !");
                }else {
                    btnmuahang();
                }
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public void btnmuahang(){
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
                Toast.makeText(BuyNow_Activity.this, "Cảm ơn bạn đã đặt hàng", Toast.LENGTH_SHORT).show();
            }
        });

        UUID uuid1 = UUID.randomUUID();
        String idu = uuid1.toString().trim();
        DatabaseReference myRef = database.getReference("CartOrder/" + idu);
        CartOrderDTO cartOrderDTO = new CartOrderDTO(idu, udi, idproduct, auth.getUid(), nameproduct, soluong, priceB, imageproduct);
        myRef.setValue(cartOrderDTO);
    }
    public void ThanhtoanZaloPay(){
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String amou = priceproduct.toString().trim();
                CreateOrder orderApi = new CreateOrder();
                JSONObject data ;
                String token ;
                try {
                    data = orderApi.createOrder(amou);
                    token = data.getString("zp_trans_token");
                    Log.d(TAG, "token: " + token);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                ZaloPaySDK.getInstance().payOrder(BuyNow_Activity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(BuyNow_Activity.this)
                                        .setTitle("Payment Success")
                                        .setMessage(String.format("TransactionId: %s - TransToken: %s", transactionId, transToken))
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .setNegativeButton("Cancel", null).show();
                            }

                        });
                    }

                    @Override
                    public void onPaymentCanceled(String zpTransToken, String appTransID) {
                        new AlertDialog.Builder(BuyNow_Activity.this)
                                .setTitle("User Cancel Payment")
                                .setMessage(String.format("zpTransToken: %s \n", zpTransToken))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNegativeButton("Cancel", null).show();
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                        new AlertDialog.Builder(BuyNow_Activity.this)
                                .setTitle("Payment Fail")
                                .setMessage(String.format("ZaloPayErrorCode: %s \nTransToken: %s", zaloPayError.toString(), zpTransToken))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNegativeButton("Cancel", null).show();
                    }
                });
            }
        });
    }
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}
