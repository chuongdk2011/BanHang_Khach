package com.example.banhang_khach.activity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.banhang_khach.DTO.BillDTO;
import com.example.banhang_khach.DTO.CartOrderDTO;
import com.example.banhang_khach.DTO.DTO_QlySanPham;
import com.example.banhang_khach.DTO.GlobalData_instance;
import com.example.banhang_khach.DTO.OrderInformationDTO;
import com.example.banhang_khach.DTO.UserDTO;
import com.example.banhang_khach.R;
import com.example.banhang_khach.VNpay.API;
import com.example.banhang_khach.VNpay.DTO_vnpay;
import com.example.banhang_khach.VNpay.Vnpay_Retrofit;
import com.example.banhang_khach.VNpay.WebViewThanhtoan;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class BuyNow_Activity extends AppCompatActivity {
    String TAG  = "buynow";
    TextView tv_dialogname, tv_dialogprice, tv_dialogsoluong;
    ImageView btn_close, imgpro, imgtru, imgcong;
    Button btnthanhtoan, btnPay;
    String idproduct, nameproduct,priceproduct, informationproduct,imageproduct;
    //điịa chỉ
    TextView tv_sdt, tv_fullname, tv_diachi, tv_thongbao, tv_sua, tv_thanhtien;
    //String check thông tin
    String str_hoten = "1", str_sdt = "1",str_diachi = "1";
    int sl, tongprice;
    static String url = API.URL;
    static final String BASE_URL = url +"/payment/";
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_now);
        Anhxa();

        Intent intent = getIntent();
        idproduct = intent.getStringExtra("id_product");

        getchitietsanpham();
        Muahang();
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
        btn_close = findViewById(R.id.id_back);
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
        btnthanhtoan = findViewById(R.id.btnthanhtoan);
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

                    // Kiểm tra xem có dữ liệu số điện thoại,địa chỉ không
                    if (user != null) {
                        if (user != null) {
                            int phone = user.getPhone();
                            int defaultValue = -1; // Sử dụng -1 làm giá trị mặc định

                            if (phone != defaultValue) {
                                tv_sdt.setText(""+user.getPhone());
                            } else {
                                tv_sdt.setVisibility(View.GONE);
                                str_sdt = "0";
                            }
                        } else {
                            // Người dùng không tồn tại hoặc lỗi xảy ra
                        }
                        if (user.getFullname() != null) {
                            tv_fullname.setText(user.getFullname());
                            tv_fullname.setVisibility(View.VISIBLE); // Hiển thị TextView
                        } else {
                            // Dữ liệu full name chưa điền, hiển thị "chưa điền" và giữ TextView không bị ẩn
                            tv_fullname.setText(" chưa điền");
                            tv_fullname.setVisibility(View.VISIBLE);
                            str_hoten = "0";
                        }
                        if (user.getAdress() != null) {
                            tv_diachi.setText(user.getAdress());
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
    }
    public void getchitietsanpham(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

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

        btnthanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_thanhtoan();
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public void dialog_thanhtoan(){
        dialog = new Dialog(BuyNow_Activity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_chonthanhtoan);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        btnPay = dialog.findViewById(R.id.btnzalopay);
        ThanhtoanZaloPay(btnPay);

        // Thêm vào bảng thông tin nhận hàng
        String string_fullname = tv_fullname.getText().toString().trim();
        String string_sdt = tv_sdt.getText().toString().trim();
        String string_diachi = tv_diachi.getText().toString().trim();
        sl = Integer.parseInt(tv_dialogsoluong.getText().toString().trim());
        tongprice =(Integer.parseInt(priceproduct) * sl);
        // Sử dụng class GlobalData để lưu trữ thông tin
        GlobalData_instance globalData = GlobalData_instance.getInstance();
        globalData.setDiachi(string_diachi);
        globalData.setFullname(string_fullname);
        globalData.setSdt(string_sdt);
        globalData.setTongprice(tongprice);
        globalData.setSoluong(sl);
        globalData.setIdproduct(idproduct);
        globalData.setNameproduct(nameproduct);
        globalData.setImageproduct(imageproduct);

        Button btnvnpay = dialog.findViewById(R.id.btn_VnPay);
        btnvnpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tongprice <= 20000){
                    Toast.makeText(BuyNow_Activity.this, "Đơn giá phải trên 20,000 VND mới thanh toán được VNPay", Toast.LENGTH_SHORT).show();
                }else {
                    DTO_vnpay dtovnpay = new DTO_vnpay();
                    dtovnpay.setAmount(tongprice);
                    dtovnpay.setBankCode("NCB");
                    postthamso(dtovnpay);
                    dialog.dismiss();
                }
            }
        });

        Button btn_nhanhang = dialog.findViewById(R.id.btn_nhanhang);
        btn_nhanhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (str_sdt.equals("0") || str_hoten.equals("0") || str_diachi.equals("0")){
                    tv_thongbao.setText("Bạn phải nhập đủ thông tin nhận hàng !");
                }else {

                    UUID uuid = UUID.randomUUID();
                    String idthanhtoan = uuid.toString().trim();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef_thanhtoan = database.getReference("Thanhtoan/" + idthanhtoan);
                    Map<String, Object> mapthanhtoan = new HashMap<>();
                    mapthanhtoan.put("idthanhtoan", idthanhtoan);
                    mapthanhtoan.put("vnp_Amount", ""+tongprice);
                    mapthanhtoan.put("vnp_CardType", "Thanh toán khi nhận hàng");
                    myRef_thanhtoan.setValue(mapthanhtoan);

                    btnmuahang(idthanhtoan, string_fullname,string_sdt, string_diachi, sl,
                            tongprice, idproduct, nameproduct, imageproduct);
                    dialog.dismiss();
                    tv_thongbao.setText("Đặt hàng thành công");
                }
            }
        });


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
    public void btnmuahang(String idthanhtoan, String string_fullname,String string_sdt, String string_diachi, int soluong,
                           int tongprice, String idproduct, String nameproduct, String imageproduct){

        UUID uuid = UUID.randomUUID();
        String udi = uuid.toString().trim();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef_Bill = database.getReference("BillProduct/" + udi);
        BillDTO billDTO = new BillDTO(udi, auth.getUid(),idthanhtoan, tongprice,date, 1);
        myRef_Bill.setValue(billDTO);

        //thêm idbill vào bảng giỏ hàng
        UUID uuid1 = UUID.randomUUID();
        String idu = uuid1.toString().trim();
        DatabaseReference myRef = database.getReference("CartOrder/" + idu);
        CartOrderDTO cartOrderDTO = new CartOrderDTO(idu, udi, idproduct, auth.getUid(), nameproduct, soluong, tongprice, imageproduct);
        myRef.setValue(cartOrderDTO);

        //thêm vào bảng thông tin nhận hàng
        UUID infouuid = UUID.randomUUID();
        String str_infouuid = infouuid.toString().trim();
        DatabaseReference myRefinfo = database.getReference("OrderInformation/" + str_infouuid);
        OrderInformationDTO orderInformationDTO = new OrderInformationDTO(udi, string_fullname,string_sdt, string_diachi);
        myRefinfo.setValue(orderInformationDTO);
    }
    public void ThanhtoanZaloPay(Button btnPay){
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
    void postthamso(DTO_vnpay objUser){
        //tạo dđối towngj chuyển đổi kiểu dữ liệu
        Gson gson = new GsonBuilder().setLenient().create();
        //Tạo Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( BASE_URL )
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        //Khởi tạo Interface
        Vnpay_Retrofit userInterface = retrofit.create(Vnpay_Retrofit.class);
        //Tạo Call
        Call<DTO_vnpay> objCall = userInterface.createpaymenturl(objUser);

        //Thực hiệnửi dữ liệu lên server
        objCall.enqueue(new Callback<DTO_vnpay>() {
            @Override
            public void onResponse(Call<DTO_vnpay> call, Response<DTO_vnpay> response) {
                //Kết quẳ server trả về ở đây
                if(response.isSuccessful()){
                    DTO_vnpay dtoVnpay = response.body();
                    Log.d(TAG, "responseData.getData(): "+ dtoVnpay.getDataurl());
                    Intent intent = new Intent(getApplicationContext(), WebViewThanhtoan.class);
                    intent.putExtra("paymentUrl", dtoVnpay.getDataurl());
                    intent.putExtra("locactivity", "buynowactivity");
                    startActivity(intent);
                }else {
                    Log.d(TAG, "nguyen1: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DTO_vnpay> call, Throwable t) {
                //Nếu say ra lỗi sẽ thông báo ở đây
                Log.d(TAG, "nguyen2: " + t.getLocalizedMessage());
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        diachi();
    }
}
