package com.example.banhang_khach.DTO;

public class UserDTO {
    String id;
    String passwd;
    String fullname;
    int phone;
    String email;
    String role;
    String lastMess;
    String age;
    String adress;

    public UserDTO() {
    }

    public UserDTO(String id, String passwd, String fullname, int phone, String email, String role, String lastMess, String age, String adress) {
        this.id = id;
        this.passwd = passwd;
        this.fullname = fullname;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.lastMess = lastMess;
        this.age = age;
        this.adress = adress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLastMess() {
        return lastMess;
    }

    public void setLastMess(String lastMess) {
        this.lastMess = lastMess;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}
