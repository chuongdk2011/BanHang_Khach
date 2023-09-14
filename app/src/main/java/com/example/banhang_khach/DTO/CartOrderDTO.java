package com.example.banhang_khach.DTO;

public class CartOrderDTO {
    String idCart;
    String iduser;
    String name;
    String price;
    String image;
    int quantity;

    public CartOrderDTO() {
    }

    public CartOrderDTO(String idCart, String iduser, String name, String price, String image, int quantity) {
        this.idCart = idCart;
        this.iduser = iduser;
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }

    public String getIdCart() {
        return idCart;
    }

    public void setIdCart(String idCart) {
        this.idCart = idCart;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
