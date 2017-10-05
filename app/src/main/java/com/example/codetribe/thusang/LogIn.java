package com.example.codetribe.thusang;

/**
 * Created by codetribe on 2017/09/28.
 */

public class LogIn {
    String name,email,password,address;

    public LogIn() {

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LogIn(String s) {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotes() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
