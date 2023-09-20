package com.atasoy.recipe.dao;

import com.atasoy.recipe.model.UserModel;

public class JwtAuthenticationResponse {
    private boolean success;
    private String token;
    private String message;
    private UserModel userModel;

    public JwtAuthenticationResponse(boolean b, String jwtToken, String s, UserModel userModel) {
        this.success = b;
        this.token = jwtToken;
        this.message = s;
        this.userModel = userModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }


}
