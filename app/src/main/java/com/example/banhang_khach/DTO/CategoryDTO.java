package com.example.banhang_khach.DTO;

public class CategoryDTO {
    String idCate;
    String name;

    public CategoryDTO() {
    }

    public CategoryDTO(String idCate, String name) {
        this.idCate = idCate;
        this.name = name;
    }

    public String getIdCate() {
        return idCate;
    }

    public void setIdCate(String idCate) {
        this.idCate = idCate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
