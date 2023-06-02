package com.example.androidprojet.model;

import java.io.Serializable;

public class DataBiometric implements Serializable {
    private Double poids;
    private Double taille;
    private Double temperature;
    private String tension;
    private Double pouls;
    private String glycemie;

    public DataBiometric() {
    }

    public DataBiometric(Double poids,
                         Double taille,
                         Double temperature,
                         String tension,
                         Double pouls,
                         String glycemie) {
        this.poids = poids;
        this.taille = taille;
        this.temperature = temperature;
        this.tension = tension;
        this.pouls = pouls;
        this.glycemie = glycemie;
    }

    public Double getPoids() {
        return poids;
    }

    public void setPoids(Double poids) {
        this.poids = poids;
    }

    public Double getTaille() {
        return taille;
    }

    public void setTaille(Double taille) {
        this.taille = taille;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getTension() {
        return tension;
    }

    public void setTension(String tension) {
        this.tension = tension;
    }

    public Double getPouls() {
        return pouls;
    }

    public void setPouls(Double pouls) {
        this.pouls = pouls;
    }

    public String getGlycemie() {
        return glycemie;
    }

    public void setGlycemie(String glycemie) {
        this.glycemie = glycemie;
    }

    @Override
    public String toString() {
        return "DataBiometric{" +
                "poids=" + poids +
                ", taille=" + taille +
                ", temperature=" + temperature +
                ", tension='" + tension + '\'' +
                ", pouls=" + pouls +
                ", glycemie='" + glycemie + '\'' +
                '}';
    }
}
