package com.example.banhang_khach.Interface;

import com.example.banhang_khach.DTO.FcmMessage;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FcmApiService {
    @Headers({
            "Content-Type: application/json",
            "Authorization: key=AAAA9Z3Eyao:APA91bF40R7Axu_lF6SQLChAfTU_zKxFFV0-wpA34I5Jopr7t3V6L-4YfROEjvwZCZJ1o_qGdbfL2_irXLG9z_vbf6TCvxD5s7Ld5CqOoj4sSkYwFchGEG2LgBhf5HdCB4sE80y7ZE1t"
    })
    @POST("fcm/send")
    Call<ResponseBody> sendFcmMessage(@Body FcmMessage message);
}
