package com.example.banhang_khach.DTO;

import java.util.ArrayList;

public class GlobalData_instance {
    private static GlobalData_instance instance;
    private int tongprice, soluong;
    private String sdt, diachi, fullname;
    private String idproduct, nameproduct, imageproduct;

    private ArrayList<String> arrayidcart;

    public GlobalData_instance() {
    }
    public static synchronized GlobalData_instance getInstance() {
        if (instance == null) {
            instance = new GlobalData_instance();
        }
        return instance;
    }

    public GlobalData_instance(int tongprice, int soluong, String sdt, String diachi, String fullname, String idproduct, String nameproduct, String imageproduct, ArrayList<String> arrayidcart) {
        this.tongprice = tongprice;
        this.soluong = soluong;
        this.sdt = sdt;
        this.diachi = diachi;
        this.fullname = fullname;
        this.idproduct = idproduct;
        this.nameproduct = nameproduct;
        this.imageproduct = imageproduct;
        this.arrayidcart = arrayidcart;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public String getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(String idproduct) {
        this.idproduct = idproduct;
    }

    public String getNameproduct() {
        return nameproduct;
    }

    public void setNameproduct(String nameproduct) {
        this.nameproduct = nameproduct;
    }

    public String getImageproduct() {
        return imageproduct;
    }

    public void setImageproduct(String imageproduct) {
        this.imageproduct = imageproduct;
    }

    public static void setInstance(GlobalData_instance instance) {
        GlobalData_instance.instance = instance;
    }

    public int getTongprice() {
        return tongprice;
    }

    public void setTongprice(int tongprice) {
        this.tongprice = tongprice;
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

    public ArrayList<String> getArrayidcart() {
        return arrayidcart;
    }

    public void setArrayidcart(ArrayList<String> arrayidcart) {
        this.arrayidcart = arrayidcart;
    }
}
