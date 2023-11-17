package com.example.banhang_khach.VNpay;

public class DTO_postdata {
    String message, code, data;

    public DTO_postdata() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public DTO_postdata(String message, String code, String data) {
        this.message = message;
        this.code = code;
        this.data = data;
    }
}
