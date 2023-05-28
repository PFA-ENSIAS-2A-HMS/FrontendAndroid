package com.example.androidprojet.model;

public class User {
    private String login;
    private String password;
    private String role;
    private String token;
    public User(String login, String password, String role,String token) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.token = token;
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
}
