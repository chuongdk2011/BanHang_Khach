package com.example.banhang_khach.DTO;

public class DTO_QlySanPham {
    String image,name,price,information,category;
    int number;

    public DTO_QlySanPham() {
    }

    public DTO_QlySanPham(String image, String name, String price, String information, String category, int number) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.information = information;
        this.category = category;
        this.number = number;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
