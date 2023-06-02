package com.example.androidprojet.model;

import com.example.androidprojet.enums.StatusDataBiometric;

public class User {
    private String login;
    private String password;
    private String role;
    private String token;
    private StatusDataBiometric dataBiometric;

    public User(String login,
                String password,
                String role,
                String token,
                StatusDataBiometric dataBiometric) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.token = token;
        this.dataBiometric = dataBiometric;
    }

    public String getLogin() {
        return login;
    }

    public String getToken() {
        return token;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public StatusDataBiometric getDataBiometric() {
        return dataBiometric;
    }

    public void setDataBiometric(StatusDataBiometric dataBiometric) {
        this.dataBiometric = dataBiometric;
    }
}
