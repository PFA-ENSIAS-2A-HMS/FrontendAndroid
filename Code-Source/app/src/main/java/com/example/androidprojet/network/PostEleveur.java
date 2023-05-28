package com.example.androidprojet.network;

import com.example.androidprojet.model.Eleveur;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;

public class PostEleveur {

    private OkHttpClient client;

    public PostEleveur() {
        client = new OkHttpClient();
    }

    public String postRequest(String url, RequestBody requestBody) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String sendEleveur(String url, Eleveur eleveur, File imageFile) throws IOException {
        // Create the multipart request body
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("fullNameFr", eleveur.getFullNameFr())
                .addFormDataPart("fullNameAr", eleveur.getFullNameAr())
                .addFormDataPart("cin", eleveur.getCin())
                .addFormDataPart("address", eleveur.getAddress())
                //.addFormDataPart("birthdate", eleveur.getBirthdate())
                .addFormDataPart("gender", eleveur.getGender())
                .addFormDataPart("phoneNumber", eleveur.getPhoneNumber())
                .addFormDataPart("city", eleveur.getCity())
                .addFormDataPart("password", eleveur.getPassword())
                .addFormDataPart("profile", "profile_image.jpg", RequestBody.create(MediaType.parse("image/*"), imageFile))
                .build();

        return postRequest(url, requestBody);
    }
}
