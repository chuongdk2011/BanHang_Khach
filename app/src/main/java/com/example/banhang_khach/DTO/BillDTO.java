package com.example.banhang_khach.DTO;

public class BillDTO {
    String idBill;
    String iduser;
    double TotalPrice;
    String dateBuy;
    String status;

    public BillDTO() {
    }

    public BillDTO(String idBill,String iduser, double totalPrice, String dateBuy, String status) {
        this.idBill = idBill;
        this.iduser = iduser;
        TotalPrice = totalPrice;
        this.dateBuy = dateBuy;
        this.status = status;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getIdBill() {
        return idBill;
    }

    public void setIdBill(String idBill) {
        this.idBill = idBill;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getDateBuy() {
        return dateBuy;
    }

    public void setDateBuy(String dateBuy) {
        this.dateBuy = dateBuy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
