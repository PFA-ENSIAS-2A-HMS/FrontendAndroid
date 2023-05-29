package com.example.androidprojet.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;


import com.example.androidprojet.MainActivity;
import com.example.androidprojet.R;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.androidprojet.database.DatabaseHelper;
import com.example.androidprojet.model.User;
import com.example.androidprojet.network.ApiConnection;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;


public class ProfilFragment extends Fragment {
    private DatabaseHelper databaseHelper;
    private User user;
    private String profile;
    private TextView displayRole;
    private ImageView logoutView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*logoutView = getActivity().findViewById(R.id.logout);
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Deconnexion...");
        progressDialog.setCancelable(false);

        logoutView.setOnClickListener(view1 -> {
            progressDialog.show();

            new Handler().postDelayed(() -> {
                databaseHelper.deleteAllUsers();

                progressDialog.dismiss();

                Intent intent_logout = new Intent(getActivity(), MainActivity.class);
                startActivity(intent_logout);
            }, 1000);
        });

        ApiConnection apiConnection = new ApiConnection();
        databaseHelper = new DatabaseHelper(getContext());
        user = databaseHelper.getUser();
        if(user.getRole().equals("breeder")){
            profile_eleveur(view,apiConnection);
        }else{
            profile_veterinaire(view,apiConnection);
        }*/
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void profile_eleveur(View view, ApiConnection apiConnection){
        displayRole = getActivity().findViewById(R.id.textView4);
        displayRole.setText(user.getRole().equals("breeder") ? "éleveur":  "vétérinaire");
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Chargement des données en cours...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        apiConnection.getFromApi(ApiConnection.URL+"/api/v1/breeders?phoneNumber[eq]="+user.getLogin(), new ApiConnection.Callback() {

            @Override
            public void onResponse(int code, String response) {


                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonResponse = objectMapper.readTree(response);

                    JsonNode dataArray = jsonResponse.get("data");
                    if (dataArray.isArray() && dataArray.size() > 0) {
                        JsonNode firstObject = dataArray.get(0);
                        String fullNameFr = firstObject.get("fullNameFr").asText();
                        String fullNameAr = firstObject.get("fullNameAr").asText();
                        String cin = firstObject.get("cin").asText();
                        String address = firstObject.get("address").asText();
                        String birthdate = firstObject.get("birthdate").asText();
                        String gender = firstObject.get("gender").asText();
                        String phoneNumber = firstObject.get("phoneNumber").asText();
                        String city = firstObject.get("city").asText();
                        profile = firstObject.get("profile").asText();
                        EditText nameFR = view.findViewById(R.id.nameFR);
                        nameFR.setText(fullNameFr);
                        EditText nameAR = view.findViewById(R.id.nameAR);
                        nameAR.setText(fullNameAr);
                        EditText addresse = view.findViewById(R.id.addresse);
                        addresse.setText(address);
                        EditText telephone = view.findViewById(R.id.phoneNumber);
                        telephone.setText(phoneNumber);
                        EditText CIN = view.findViewById(R.id.cin);
                        CIN.setText(cin);
                        progressDialog.dismiss();

                        String imageUrl = ApiConnection.URL+"/"+profile;
                        apiConnection.downloadImageFromApi(imageUrl, new ApiConnection.Callback() {
                            @Override
                            public void onResponse(int code, String response) {

                            }

                            @Override
                            public void onError(int code, String error) {

                            }

                            @Override
                            public void onImageDownloaded(Bitmap image) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        updateProfilePicture(image);
                                    }
                                });
                            }
                        });


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code, String error) {

            }

            @Override
            public void onImageDownloaded(Bitmap image) {

            }
        });

    }
    private void profile_veterinaire(View view, ApiConnection apiConnection){
        displayRole = getActivity().findViewById(R.id.textView4);
        displayRole.setText(user.getRole().equals("breeder") ? "éleveur":  "vétérinaire");
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Chargement des données en cours...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        apiConnection.getFromApi(ApiConnection.URL+"/api/v1/profiles?email[eq]="+user.getLogin(), new ApiConnection.Callback() {

            @Override
            public void onResponse(int code, String response) {

                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonResponse = objectMapper.readTree(response);

                    JsonNode dataArray = jsonResponse.get("data");
                    if (dataArray.isArray() && dataArray.size() > 0) {
                        JsonNode firstObject = dataArray.get(0);
                        String name = firstObject.get("name").asText();
                        String email = firstObject.get("email").asText();
                        String phoneNumber = firstObject.get("phoneNumber").asText();
                        String address = firstObject.get("role").asText();
                        profile = "storage/veterinaires/profile_logo.png";
                        EditText nameFR = view.findViewById(R.id.nameFR);
                        nameFR.setText(name);
                        EditText nameAR = view.findViewById(R.id.nameAR);
                        nameAR.setText(email);
                        EditText addresse = view.findViewById(R.id.addresse);
                        addresse.setText(address);
                        EditText telephone = view.findViewById(R.id.phoneNumber);
                        telephone.setText(phoneNumber);


                        progressDialog.dismiss();

                        String imageUrl = ApiConnection.URL+"/"+profile;
                        apiConnection.downloadImageFromApi(imageUrl, new ApiConnection.Callback() {
                            @Override
                            public void onResponse(int code, String response) {

                            }

                            @Override
                            public void onError(int code, String error) {

                            }

                            @Override
                            public void onImageDownloaded(Bitmap image) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        updateProfilePicture(image);
                                    }
                                });
                            }
                        });


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code, String error) {

            }

            @Override
            public void onImageDownloaded(Bitmap image) {

            }
        });

    }




    private void updateProfilePicture(Bitmap image) {
        ShapeableImageView profilePictureImageView = getActivity().findViewById(R.id.profile_picture);
        profilePictureImageView.setImageBitmap(image);
        profilePictureImageView.setVisibility(View.VISIBLE);
        ProgressBar loadingProgress = getActivity().findViewById(R.id.loading_progress);
        loadingProgress.setVisibility(View.GONE);
    }

}
