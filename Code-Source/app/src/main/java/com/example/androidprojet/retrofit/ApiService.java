package com.example.androidprojet.retrofit;
import com.example.androidprojet.model.Eleveur;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST("api/endpoint")
    Call<Void> postEleveur(@Part MultipartBody.Part profileImage, @Body Eleveur eleveur);
}