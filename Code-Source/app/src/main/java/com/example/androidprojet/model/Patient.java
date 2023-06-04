package com.example.androidprojet.model;

import java.io.Serializable;

public class Patient implements Serializable {
    private String firstName;
    private String lastName;
    private String gender;
    private String cin;
    private String birthdate;
    private String address;
    private String phoneNumber;
    private String profile;
    private String password;
    private String email;

    public Patient() {
    }

    public Patient(String firstName,
                   String lastName,
                   String phoneNumber,
                   String profile,
                   String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.profile = profile;
        this.password = password;
    }

    public Patient(String firstName,
                   String lastName,
                   String gender,
                   String cin,
                   String birthdate,
                   String address,
                   String phoneNumber,
                   String profile,
                   String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.cin = cin;
        this.birthdate = birthdate;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.profile = profile;
        this.password = password;
    }

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}