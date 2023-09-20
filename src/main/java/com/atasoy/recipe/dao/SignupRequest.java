package com.atasoy.recipe.dao;

import jakarta.validation.constraints.Size;

public class SignupRequest {
    @Size(max = 20, message = "Kullanıcı adı en fazla 20 karakter olmalı.")
    private String firstName;
    @Size(max = 20, message = "Ad en fazla 20 karakter olmalı.")
    private String lastName;
    @Size(max = 20, message = "Soyad en fazla 20 karakter olmalı.")
    private String userName;
    private String password;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
