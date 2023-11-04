package com.example.banhang_khach.DTO;

public class OrderInformationDTO {
    String id_bill;
    String fullname;
    String sdt;
    String diachi;

    public OrderInformationDTO() {
    }

    public OrderInformationDTO(String id_bill, String fullname, String sdt, String diachi) {
        this.id_bill = id_bill;
        this.fullname = fullname;
        this.sdt = sdt;
        this.diachi = diachi;
    }

    public String getId_bill() {
        return id_bill;
    }

    public void setId_bill(String id_bill) {
        this.id_bill = id_bill;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }
}
