package com.example.androidprojet.model;

public class DossierMedical {
    private String malade;
    private String docteur;
    private String date;
    private String Description;
    private int logo;

    public DossierMedical(String malade, String docteur, String date, String description, int logo) {
        this.malade = malade;
        this.docteur = docteur;
        this.date = date;
        Description = description;
        this.logo = logo;
    }

    public String getMalade() {
        return malade;
    }

    public void setMalade(String malade) {
        this.malade = malade;
    }

    public String getDocteur() {
        return docteur;
    }

    public void setDocteur(String docteur) {
        this.docteur = docteur;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }
}
