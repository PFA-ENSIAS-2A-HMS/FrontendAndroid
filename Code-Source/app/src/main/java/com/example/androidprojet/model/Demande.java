package com.example.androidprojet.model;

public class Demande {
    private String login;
    private String contenu;
    private String status;
    private String date;

    public Demande() {

    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "{" +
                "login='" + login + '\'' +
                ", contenu='" + contenu + '\'' +
                '}';
    }
}