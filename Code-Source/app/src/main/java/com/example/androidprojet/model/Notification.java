package com.example.androidprojet.model;

import android.widget.ImageView;
import android.widget.TextView;

public class Notification {

    public  int senderImage;
    public String sender;
    private String messageNotification;
    private String heureRecu;

    public Notification(String sender, String messageNotification, String heureRecu, int senderImage) {
        this.senderImage = senderImage;
        this.sender = sender;
        this.messageNotification = messageNotification;
        this.heureRecu = heureRecu;
    }

    public int getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(int senderImage) {
        this.senderImage = senderImage;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessageNotification() {
        return messageNotification;
    }

    public void setMessageNotification(String messageNotification) {
        this.messageNotification = messageNotification;
    }

    public String getHeureRecu() {
        return heureRecu;
    }

    public void setHeureRecu(String heureRecu) {
        this.heureRecu = heureRecu;
    }

}
