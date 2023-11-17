package com.example.banhang_khach.VNpay;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.banhang_khach.DTO.GlobalData_instance;
import com.example.banhang_khach.R;
import com.example.banhang_khach.activity.BuyNow_Activity;
import com.example.banhang_khach.activity.CartOrderActivity;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.UUID;

public class WebViewThanhtoan extends AppCompatActivity {
    private WebView webView;
    ImageView ic_back;
    static String url = API.URL;
    static final String BASE_URL = url +"/payment/vnpay_return";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_vnpay_main);
        webView = findViewById(R.id.webView);
        ic_back = findViewById(R.id.id_back);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("WebViewInfo", "Page finished loading: " + url);
            }
        });

        String url = getIntent().getStringExtra("paymentUrl");
        String loc_activity = getIntent().getStringExtra("locactivity");
        webView.loadUrl(url);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("WebViewInfo", "Page finished loading: " + url);

                // Lấy URL hiện tại của WebView
                String currentUrl = view.getUrl();
                Log.d("WebViewInfo", "Current URL: " + currentUrl);

                // Kiểm tra xem URL có phải là URL cần xử lý không
                if (currentUrl.startsWith(BASE_URL)) {
                    // Phân tích URL để lấy các tham số
                    Uri uri = Uri.parse(currentUrl);

                    // Lấy giá trị của tham số vnp_Amount
                    String vnp_Amount = uri.getQueryParameter("vnp_Amount");
                    String vnp_BankCode = uri.getQueryParameter("vnp_BankCode");
                    String vnp_BankTranNo = uri.getQueryParameter("vnp_BankTranNo");
                    String vnp_CardType = uri.getQueryParameter("vnp_CardType");
                    String vnp_OrderInfo = uri.getQueryParameter("vnp_OrderInfo");
                    String vnp_PayDate = uri.getQueryParameter("vnp_PayDate");
                    String vnp_ResponseCode = uri.getQueryParameter("vnp_ResponseCode");
                    String vnp_TmnCode = uri.getQueryParameter("vnp_TmnCode");
                    String vnp_TransactionNo = uri.getQueryParameter("vnp_TransactionNo");
                    String vnp_TransactionStatus = uri.getQueryParameter("vnp_TransactionStatus");
                    String vnp_TxnRef = uri.getQueryParameter("vnp_TxnRef");
                    String vnp_SecureHash = uri.getQueryParameter("vnp_SecureHash");
                    // Gọi phương thức để xử lý dữ liệu từ URL
                    if (loc_activity.equals("buynowactivity")){
                        Log.d("log", "buynowactivity: " + loc_activity);
                        Log.d("log", "buynowactivity: " + (loc_activity.equals("buynowactivity")));
                        DataFromUrl_buynowactivity(vnp_Amount, vnp_BankCode,vnp_BankTranNo, vnp_CardType, vnp_OrderInfo,
                                vnp_PayDate, vnp_ResponseCode, vnp_TmnCode, vnp_TransactionNo, vnp_TransactionStatus,
                                vnp_TxnRef, vnp_SecureHash);
                    }else if (loc_activity.equals("cartorderactivity")){
                        DataFromUrl_cartorderactivity(vnp_Amount, vnp_BankCode,vnp_BankTranNo, vnp_CardType, vnp_OrderInfo,
                                vnp_PayDate, vnp_ResponseCode, vnp_TmnCode, vnp_TransactionNo, vnp_TransactionStatus,
                                vnp_TxnRef, vnp_SecureHash);
                    }

                }
            }
        });
    }
    private void DataFromUrl_buynowactivity(String vnp_Amount, String vnp_BankCode, String vnp_BankTranNo, String vnp_CardType,
                                              String vnp_OrderInfo, String vnp_PayDate, String vnp_ResponseCode, String vnp_TmnCode,
                                              String vnp_TransactionNo, String vnp_TransactionStatus, String vnp_TxnRef, String vnp_SecureHash) {
            UUID uuid = UUID.randomUUID();
            String idthanhtoan = uuid.toString().trim();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef_thanhtoan = database.getReference("Thanhtoan/" + idthanhtoan);
            String vnp_Amount100 = String.valueOf(Integer.parseInt(vnp_Amount) / 100);
            DTO_thanhtoan dtoThanhtoan = new DTO_thanhtoan(idthanhtoan,vnp_Amount100, vnp_BankCode,vnp_BankTranNo, vnp_CardType, vnp_OrderInfo,
                    vnp_PayDate, vnp_ResponseCode, vnp_TmnCode, vnp_TransactionNo, vnp_TransactionStatus,
                    vnp_TxnRef, vnp_SecureHash);
            myRef_thanhtoan.setValue(dtoThanhtoan, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    Toast.makeText(WebViewThanhtoan.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Tạo đối tượng của BuyNow_Activity
                            BuyNow_Activity buyNowActivity = new BuyNow_Activity();

                            // Sử dụng class GlobalData để lấy thông tin
                            GlobalData_instance globalData = GlobalData_instance.getInstance();
                            String string_diachi = globalData.getDiachi();
                            String string_sdt = globalData.getSdt();
                            String string_fullname = globalData.getFullname();
                            Log.d("string_fullname", "run: " + string_fullname);
                            int tongprice = globalData.getTongprice();
                            int soluong = globalData.getSoluong();
                            String idproduct = globalData.getIdproduct();
                            String imageproduct = globalData.getImageproduct();
                            String nameproduct = globalData.getNameproduct();

                            // Gọi phương thức từ BuyNow_Activity
                            buyNowActivity.btnmuahang(idthanhtoan, string_fullname,string_sdt, string_diachi, soluong,
                                    tongprice, idproduct, nameproduct, imageproduct);
                            onBackPressed();
                        }
                    }, 10000);
                }
            });

    }

    private void DataFromUrl_cartorderactivity(String vnp_Amount, String vnp_BankCode, String vnp_BankTranNo, String vnp_CardType,
                                              String vnp_OrderInfo, String vnp_PayDate, String vnp_ResponseCode, String vnp_TmnCode,
                                              String vnp_TransactionNo, String vnp_TransactionStatus, String vnp_TxnRef, String vnp_SecureHash) {
            UUID uuid = UUID.randomUUID();
            String idthanhtoan = uuid.toString().trim();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef_thanhtoan = database.getReference("Thanhtoan/" + idthanhtoan);
            String vnp_Amount100 = String.valueOf(Integer.parseInt(vnp_Amount) / 100);
            DTO_thanhtoan dtoThanhtoan = new DTO_thanhtoan(idthanhtoan,vnp_Amount100, vnp_BankCode,vnp_BankTranNo, vnp_CardType, vnp_OrderInfo,
                    vnp_PayDate, vnp_ResponseCode, vnp_TmnCode, vnp_TransactionNo, vnp_TransactionStatus,
                    vnp_TxnRef, vnp_SecureHash);
            myRef_thanhtoan.setValue(dtoThanhtoan, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    Toast.makeText(WebViewThanhtoan.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Tạo đối tượng của BuyNow_Activity
                            CartOrderActivity cartOrderActivity = new CartOrderActivity();

                            // Sử dụng class GlobalData để lấy thông tin
                            GlobalData_instance globalData = GlobalData_instance.getInstance();
                            ArrayList<String> listidcart = globalData.getArrayidcart();
                            Log.d("listidcart", "run: " +listidcart);
                            String string_diachi = globalData.getDiachi();
                            String string_sdt = globalData.getSdt();
                            int tongprice = globalData.getTongprice();
                            String string_fullname = globalData.getFullname();

                            // Gọi phương thức từ BuyNow_Activity
                            cartOrderActivity.muahangfirebase(idthanhtoan, string_fullname, string_sdt, string_diachi, tongprice, listidcart);
                            onBackPressed();
                        }
                    }, 10000);
                }
            });

    }
}
