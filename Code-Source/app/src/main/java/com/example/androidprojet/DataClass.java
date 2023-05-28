package com.example.androidprojet;

public class DataClass {
    private int dataImage;
    private String dataTitle;
    private int dataDesc;

    public DataClass(int dataImage, String dataTitle, int dataDesc) {
        this.dataImage = dataImage;
        this.dataTitle = dataTitle;
        this.dataDesc = dataDesc;
    }

    public int getDataImage() {
        return dataImage;
    }

    public String getDataTitle() {
        return dataTitle;
    }

    public int getDataDesc() {
        return dataDesc;
    }
}
