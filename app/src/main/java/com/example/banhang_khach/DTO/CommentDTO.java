package com.example.banhang_khach.DTO;

public class CommentDTO {
    String idproduct;

    String content;
    String date;
    String id;
    UserDTO userDTO;

    public CommentDTO() {
    }

    public CommentDTO(String idproduct, String id, String content, String date, UserDTO userDTO) {
        this.idproduct = idproduct;
        this.id = id;
        this.content = content;
        this.date = date;
        this.userDTO = userDTO;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public String getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(String idproduct) {
        this.idproduct = idproduct;
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
