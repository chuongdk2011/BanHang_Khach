package com.example.banhang_khach.VNpay;

public class DTO_hoantien {
    String vnp_TxnRef, vnp_TransactionDate, vnp_Amount, vnp_TransactionType, vnp_CreateBy;

    public DTO_hoantien() {
    }

    public DTO_hoantien(String vnp_TxnRef, String vnp_TransactionDate, String vnp_Amount, String vnp_TransactionType, String vnp_CreateBy) {
        this.vnp_TxnRef = vnp_TxnRef;
        this.vnp_TransactionDate = vnp_TransactionDate;
        this.vnp_Amount = vnp_Amount;
        this.vnp_TransactionType = vnp_TransactionType;
        this.vnp_CreateBy = vnp_CreateBy;
    }

    public String getVnp_TxnRef() {
        return vnp_TxnRef;
    }

    public void setVnp_TxnRef(String vnp_TxnRef) {
        this.vnp_TxnRef = vnp_TxnRef;
    }

    public String getVnp_TransactionDate() {
        return vnp_TransactionDate;
    }

    public void setVnp_TransactionDate(String vnp_TransactionDate) {
        this.vnp_TransactionDate = vnp_TransactionDate;
    }

    public String getVnp_Amount() {
        return vnp_Amount;
    }

    public void setVnp_Amount(String vnp_Amount) {
        this.vnp_Amount = vnp_Amount;
    }

    public String getVnp_TransactionType() {
        return vnp_TransactionType;
    }

    public void setVnp_TransactionType(String vnp_TransactionType) {
        this.vnp_TransactionType = vnp_TransactionType;
    }

    public String getVnp_CreateBy() {
        return vnp_CreateBy;
    }

    public void setVnp_CreateBy(String vnp_CreateBy) {
        this.vnp_CreateBy = vnp_CreateBy;
    }
}
