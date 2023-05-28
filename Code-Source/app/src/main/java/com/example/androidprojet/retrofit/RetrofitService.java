package com.example.androidprojet.retrofit;
import com.google.gson.Gson;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitService {
    private Retrofit retrofit;
    public RetrofitService() {
        initializeRetrofit();
    }

    private void initializeRetrofit() {
        retrofit = new Retrofit.Builder()

                .baseUrl("http://192.168.1.3:8000")
//                .baseUrl("http://192.168.0.236:9000") // l'url du serveur
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }
    public Retrofit getRetrofit() {
        return retrofit;
    }
}
