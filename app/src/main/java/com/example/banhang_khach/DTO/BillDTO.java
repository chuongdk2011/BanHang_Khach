package com.example.banhang_khach.DTO;

public class BillDTO {
    String idBill;
    String iduser, idthanhtoan;

    int TotalPrice;
    String dateBuy;
    int status;

    public BillDTO() {
    }

    public BillDTO(String idBill, String iduser, String idthanhtoan, int totalPrice, String dateBuy, int status) {
        this.idBill = idBill;
        this.iduser = iduser;
        this.idthanhtoan = idthanhtoan;
        TotalPrice = totalPrice;
        this.dateBuy = dateBuy;
        this.status = status;
    }

    public String getIdthanhtoan() {
        return idthanhtoan;
    }

    public void setIdthanhtoan(String idthanhtoan) {
        this.idthanhtoan = idthanhtoan;
    }

    public String getIdBill() {
        return idBill;
    }

    public void setIdBill(String idBill) {
        this.idBill = idBill;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getDateBuy() {
        return dateBuy;
    }

    public void setDateBuy(String dateBuy) {
        this.dateBuy = dateBuy;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
