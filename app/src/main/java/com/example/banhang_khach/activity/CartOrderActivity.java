package com.example.banhang_khach.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.banhang_khach.Adapter.CartOrderAdapter;
import com.example.banhang_khach.DTO.BillDTO;
import com.example.banhang_khach.DTO.CartOrderDTO;
import com.example.banhang_khach.DTO.OrderInformationDTO;
import com.example.banhang_khach.DTO.UserDTO;
import com.example.banhang_khach.R;
import com.example.banhang_khach.Zalopay.Api.CreateOrder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CartOrderActivity extends AppCompatActivity implements CartOrderAdapter.OnclickCheck{
    String TAG = "cartoderactivity";
    ListView rc_listcart;
    TextView tongcart;
    Button btnmuahang;
    ArrayList<CartOrderDTO> list;
    CartOrderAdapter adapter;
    ImageView imgback;
    ArrayList<String> idcart;
    int s = 0;
    int tongprice = 0;
    //String check thông tin
    String str_hoten = "1", str_sdt = "1",str_diachi = "1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_order);
        Anhxa();
        list = new ArrayList<>();
        adapter = new CartOrderAdapter(CartOrderActivity.this,list, this);
        rc_listcart.setAdapter(adapter);
        getdatacartorder();

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        onQuality(idcart);
    }


    public void Anhxa(){
        rc_listcart = findViewById(R.id.rc_view);
        tongcart = findViewById(R.id.tv_tonggia);
        btnmuahang = findViewById(R.id.btn_muahang);
        imgback = findViewById(R.id.id_back);
    }

    public void getdatacartorder(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefId = database.getReference("CartOrder");
        myRefId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CartOrderDTO categoryDTO = dataSnapshot.getValue(CartOrderDTO.class);
                    if (auth.getUid().equals(categoryDTO.getIduser()) && (categoryDTO.getIdBill()).equals("")){
                        list.add(categoryDTO);
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onQuality(ArrayList<String> idcart) {
        btnmuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (s != 0){
                    dialog(idcart);
                }else {
                    Toast.makeText(CartOrderActivity.this, "Bạn chưa chọn sản phẩm nào!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onCheckboxTrue(int tongtien) {
        s++;
        tongprice = tongtien;
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongcart.setText(decimalFormat.format(tongtien) + " VND");
    }

    @Override
    public void onCheckboxFalse(int tongtien) {
        s--;
        tongprice = tongtien;
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongcart.setText(decimalFormat.format(tongtien) + " VND");
    }
    public void dialog(ArrayList<String> idcart) {
        Dialog dialog = new Dialog(CartOrderActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_muahang);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        //hiểm thị tổng tiền
        TextView tvthanhtien = dialog.findViewById(R.id.tv_thanhtien);
        tvthanhtien.setText("Thành tiền: "+tongprice +" VND");

        //hiển thị thông tin nhận hàng
        TextView tv_sdt, tv_diachi, tv_fullname, tv_thongbao;
        tv_thongbao = dialog.findViewById(R.id.tv_thongbao);
        tv_sdt = dialog.findViewById(R.id.tv_sdt);
        tv_diachi = dialog.findViewById(R.id.tv_diachi);
        tv_fullname = dialog.findViewById(R.id.tv_hoten);
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
                                String phoneText = String.valueOf(phone);
                                tv_sdt.setText(phoneText);
                            } else {
                                tv_sdt.setVisibility(View.GONE);
                                str_sdt = "0";
                            }
                        } else {
                            // Người dùng không tồn tại hoặc lỗi xảy ra
                        }
                        if (user.getFullname() != null) {
                            String fullname = user.getFullname();
                            String fullnameText = fullname;
                            tv_fullname.setText(fullnameText);
                            tv_fullname.setVisibility(View.VISIBLE); // Hiển thị TextView
                        } else {
                            // Dữ liệu full name chưa điền, hiển thị "chưa điền" và giữ TextView không bị ẩn
                            tv_fullname.setText(" chưa điền");
                            tv_fullname.setVisibility(View.VISIBLE);
                            str_hoten = "0";
                        }
                        if (user.getAdress() != null) {
                            String diachi = user.getAdress();
                            String diachiText = diachi;
                            tv_diachi.setText(diachiText);
                            tv_diachi.setVisibility(View.VISIBLE); // Hiển thị TextView
                        } else {
                            // Dữ liệu Address chưa điền, hiển thị "chưa điền" và giữ TextView không bị ẩn
                            tv_diachi.setText(" chưa điền");
                            tv_diachi.setVisibility(View.VISIBLE); // Hiển thị TextView
                            str_diachi = "0";
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

        TextView tvsua = dialog.findViewById(R.id.tv_sua);
        tvsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(CartOrderActivity.this, InformationActivity.class);
                startActivity(intent1);
            }
        });

        Button btndialogmua = dialog.findViewById(R.id.btn_addcart);
        btndialogmua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (str_sdt.equals("0") || str_hoten.equals("0") || str_diachi.equals("0")) {
                    tv_thongbao.setText("Bạn phải nhập đủ thông tin nhận hàng !");
                } else {
                    UUID uuid = UUID.randomUUID();
                    String udi = uuid.toString().trim();
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                    String date = df.format(Calendar.getInstance().getTime());
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef_Bill = database.getReference("BillProduct/" + udi);
                    BillDTO billDTO = new BillDTO(udi, auth.getUid(), tongprice, date, 1);
                    myRef_Bill.setValue(billDTO, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            Toast.makeText(CartOrderActivity.this, "Cảm ơn bạn đã đặt hàng", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //thêm idbill vào bảng cartorder
                    for (int i = 0; i < idcart.size(); i++) {
                        DatabaseReference myRef = database.getReference("CartOrder/" + idcart.get(i));
                        Map<String, Object> mapcartoder = new HashMap<>();
                        mapcartoder.put("idBill", udi);
                        myRef.updateChildren(mapcartoder);
                    }
                    //thêm vào bảng thông tin nhận hàng
                    String str_fullname = tv_fullname.getText().toString().trim();
                    String str_sdt = tv_sdt.getText().toString().trim();
                    String str_diachi = tv_diachi.getText().toString().trim();
                    UUID infouuid = UUID.randomUUID();
                    String str_infouuid = infouuid.toString().trim();
                    DatabaseReference myRefinfo = database.getReference("OrderInformation/" + str_infouuid);
                    OrderInformationDTO orderInformationDTO = new OrderInformationDTO(udi, str_fullname, str_sdt, str_diachi);
                    myRefinfo.setValue(orderInformationDTO);
                }
            }
        });

        //Thanh toán zalopay
        Button btnpay = dialog.findViewById(R.id.btntesst);
        ThanhtoanZaloPay(btnpay);
        // Chiều rộng full màn hình
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        // Chiều cao theo dialog màn hình
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        // Đặt vị trí của dialog ở phía dưới cùng của màn hình
        layoutParams.gravity = Gravity.BOTTOM;

        window.setAttributes(layoutParams);
        window.setBackgroundDrawableResource(android.R.color.transparent);

        dialog.show();
    }
    public void ThanhtoanZaloPay(Button btnpay){
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        btnpay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String amou = String.valueOf(tongprice);
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
                ZaloPaySDK.getInstance().payOrder(CartOrderActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(CartOrderActivity.this)
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
                        new AlertDialog.Builder(CartOrderActivity.this)
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
                        new AlertDialog.Builder(CartOrderActivity.this)
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