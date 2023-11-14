package com.example.banhang_khach.VNpay;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Vnpay_Retrofit {
    @GET("vnpay_return")
    Call<List<DTO_vnpay>> listvnpay_return();
    //Thêm mới user:
    @POST("create_payment_url")
    Call<DTO_vnpay> createpaymenturl(@Body DTO_vnpay objU);
    @GET("vnpay_ipn")
    Call<List<DTO_vnpay>> listvnpay_ipn();
}
