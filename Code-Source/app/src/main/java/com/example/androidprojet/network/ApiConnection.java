package com.example.androidprojet.network;

import android.graphics.Bitmap;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiConnection {
    public static final String URL = "http://100.76.108.249:8080";
    public void getFromApi(String apiUrl, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String responseBody = response.body().string();
                    int responseCode = response.code();
                    callback.onResponse(responseCode, responseBody);
                } catch (Exception e) {
                    callback.onError(0, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(0, e.getMessage());
            }
        });
    }

    public void getFromApi(String apiUrl, String token, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Authorization", "Bearer " + token)  // Add the token to the request headers
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String responseBody = response.body().string();
                    int responseCode = response.code();
                    callback.onResponse(responseCode, responseBody);
                } catch (Exception e) {
                    callback.onError(0, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(0, e.getMessage());
            }
        });
    }


    public void postToApi(String apiUrl, String requestBody, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, requestBody);

        Request request = new Request.Builder()
                .url(apiUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String responseBody = response.body().string();
                    int responseCode = response.code();

                    callback.onResponse(responseCode,responseBody);
                } catch (Exception e) {
                    //erreur dans cette partie
                    callback.onError(0,e.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(0,e.getMessage());
            }
        });
    }

    public void postToApi(String apiUrl, String requestBody, String token, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, requestBody);

        Request request = new Request.Builder()
                .url(apiUrl)
                .post(body)
                .addHeader("Authorization", "Bearer " + token) // Ajouter l'en-tête Authorization avec le token
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String responseBody = response.body().string();
                    int responseCode = response.code();

                    callback.onResponse(responseCode, responseBody);
                } catch (Exception e) {
                    callback.onError(0, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(0, e.getMessage());
            }
        });
    }

    public void putToApi(String apiUrl, String requestBody, String token, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, requestBody);

        Request request = new Request.Builder()
                .url(apiUrl)
                .put(body)
                .addHeader("Authorization", "Bearer " + token) // Ajouter l'en-tête Authorization avec le token
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String responseBody = response.body().string();
                    int responseCode = response.code();

                    callback.onResponse(responseCode, responseBody);
                } catch (Exception e) {
                    callback.onError(0, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(0, e.getMessage());
            }
        });
    }

    public void putToApi(String apiUrl, String requestBody, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, requestBody);

        Request request = new Request.Builder()
                .url(apiUrl)
                .put(body)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String responseBody = response.body().string();
                    int responseCode = response.code();

                    callback.onResponse(responseCode, responseBody);
                } catch (Exception e) {
                    callback.onError(0, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(0, e.getMessage());
            }
        });
    }


    public void downloadImageFromApi(String imageUrl, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(imageUrl)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    //byte[] imageBytes = response.body().bytes();
                    //Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    //callback.onImageDownloaded(bitmap);
                } catch (Exception e) {
                    //callback.onError(0, e.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(0, e.getMessage());
            }
        });
    }

    public interface Callback {
        void onResponse(int Code,String response);

        void onError(int Code,String error);
        void onImageDownloaded(Bitmap image);
    }
}
