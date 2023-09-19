package com.example.banhang_khach.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.banhang_khach.R;

public class Chitietsanpham extends AppCompatActivity {
    String TAG = "chitietsp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietsanpham);
        Log.d(TAG, "onCreate: Đã vào chi tiết sp");
    }
}