package com.example.banhang_khach.DTO;

public class CommentDTO {
    String idproduct;
    String iduser;
    String content;
    String date;

    public CommentDTO() {
    }

    public CommentDTO(String idproduct, String iduser, String content, String date) {
        this.idproduct = idproduct;
        this.iduser = iduser;
        this.content = content;
        this.date = date;
    }

    public String getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(String idproduct) {
        this.idproduct = idproduct;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
