package com.example.androidprojet.model;

import java.io.Serializable;

public class FireBaseUser implements Serializable {
    public String name, email, password, token;
    public int image;

    public FireBaseUser(String name, String email, String password, String token, int image) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.token = token;
        this.image = image;
    }
}
