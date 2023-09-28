package com.example.banhang_khach.DTO;

public class CartOrderDTO {
    String idBill;
    String id_product;
    String iduser;
    String namesp;
    int soluong;
    double price;
    String image;

    public CartOrderDTO() {
    }

    public CartOrderDTO(String idBill, String id_product, String iduser, String namesp, int soluong, double price, String image) {
        this.idBill = idBill;
        this.id_product = id_product;
        this.iduser = iduser;
        this.namesp = namesp;
        this.soluong = soluong;
        this.price = price;
        this.image = image;
    }

    public String getIdBill() {
        return idBill;
    }

    public void setIdBill(String idBill) {
        this.idBill = idBill;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getNamesp() {
        return namesp;
    }

    public void setNamesp(String namesp) {
        this.namesp = namesp;
    }
}
