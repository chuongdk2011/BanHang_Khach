package com.example.banhang_khach.DTO;

import java.util.Map;

public class FcmMessage {
    private String to;
    private Map<String, String> data;

    public FcmMessage() {
        // Constructor mặc định không đối số
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
