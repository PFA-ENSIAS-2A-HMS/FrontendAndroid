package com.example.androidprojet.model;

import java.io.Serializable;

public class DataPrescribedTreatment implements Serializable {
    private String date;
    private String diagnostic;
    private String medicaments;
    private String posologie;
    private String duree;
    private String instructionSpeciales;

    public DataPrescribedTreatment() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public String getMedicaments() {
        return medicaments;
    }

    public void setMedicaments(String medicaments) {
        this.medicaments = medicaments;
    }

    public String getPosologie() {
        return posologie;
    }

    public void setPosologie(String posologie) {
        this.posologie = posologie;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public String getInstructionSpeciales() {
        return instructionSpeciales;
    }

    public void setInstructionSpeciales(String instructionSpeciales) {
        this.instructionSpeciales = instructionSpeciales;
    }
}
