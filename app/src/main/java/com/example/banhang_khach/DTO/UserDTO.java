package com.example.banhang_khach.DTO;

public class UserDTO {
    String iduser;
    String passwd;
    String fullname;
    int phone;
    String email;
    String vaitro;

    public UserDTO() {
    }

    public UserDTO(String iduser, String passwd, String fullname, int phone, String email, String vaitro) {
        this.iduser = iduser;
        this.passwd = passwd;
        this.fullname = fullname;
        this.phone = phone;
        this.email = email;
        this.vaitro = vaitro;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVaitro() {
        return vaitro;
    }

    public void setVaitro(String vaitro) {
        this.vaitro = vaitro;
    }
}
