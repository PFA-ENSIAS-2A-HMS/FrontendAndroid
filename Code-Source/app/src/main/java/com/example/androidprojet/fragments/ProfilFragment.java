package com.example.androidprojet.fragments;

import android.app.ProgressDialog;
import android.content.Context;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;


public class ProfilFragment extends Fragment {
    private DatabaseHelper databaseHelper;
    private User user;
    private String profile;
    private TextView displayRole;
    private TextView logoutView;
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

        logoutView = getActivity().findViewById(R.id.textViewlogout);
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

        //ApiConnection apiConnection = new ApiConnection();
        //databaseHelper = new DatabaseHelper(getContext());
        //user = databaseHelper.getUser();
        //Toast.makeText(getContext(), ""+user.getLogin(), Toast.LENGTH_SHORT).show();
        /*if(user.getRole().equals("patient")){
            profile_patient(view,apiConnection);
        }else{
            profile_veterinaire(view,apiConnection);
        }*/
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void profile_patient(View view, ApiConnection apiConnection){
        displayRole = getActivity().findViewById(R.id.textView4);
        displayRole.setText(user.getRole().equals("patient") ? "Patient":  "Médecin");
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Chargement des données en cours...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        apiConnection.getFromApi(ApiConnection.URL+"/api/v1/patients/phone/"+user.getLogin(), new ApiConnection.Callback() {

            @Override
            public void onResponse(int code, String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    System.out.println(response);

                    if (jsonObject != null) {
                        String firstName = jsonObject.getString("firstName");
                        String lastName = jsonObject.getString("lastName");
                        String cin = jsonObject.getString("cin");
                        String address = jsonObject.getString("address");
                        String phoneNumber = jsonObject.getString("phoneNumber");
                        String bloodTypeProfil = jsonObject.getString("bloodType");
                        String password = jsonObject.getString("password");

                        String imageUrl = jsonObject.getString("image_url");

                        EditText NameProfil = view.findViewById(R.id.NameProfil);
                        NameProfil.setText(lastName +" "+ firstName);
                        EditText cinProfil = view.findViewById(R.id.cinProfil);
                        cinProfil.setText(cin);
                        EditText addresse = view.findViewById(R.id.addressProfil);
                        if(address.equals("null")) {
                            addresse.setText("Votre adresse ?");
                        }else {
                            addresse.setText(address);
                        }
                        EditText telephone = view.findViewById(R.id.phoneNumber);
                        telephone.setText(phoneNumber);
                        EditText bloodType = view.findViewById(R.id.bloodTypeProfil);
                        if(bloodTypeProfil.equals("null")){
                            bloodType.setText("Type sanguin ?");
                        }else {
                            bloodType.setText(bloodTypeProfil);
                        }
                        EditText passwordP = view.findViewById(R.id.passwordProfil);
                        passwordP.setText(password);

                        ProgressBar loadingProgress = view.findViewById(R.id.loading_progress);

                        progressDialog.dismiss();
                        loadingProgress.setVisibility(View.GONE);

                        /*String imageUrl = ApiConnection.URL+"/"+profile;
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
                        });*/


                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
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
        /*displayRole = getActivity().findViewById(R.id.textView4);
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
        });*/

    }

    private void updateProfilePicture(Bitmap image) {
        ShapeableImageView profilePictureImageView = getActivity().findViewById(R.id.profile_picture);
        profilePictureImageView.setImageBitmap(image);
        profilePictureImageView.setVisibility(View.VISIBLE);
        ProgressBar loadingProgress = getActivity().findViewById(R.id.loading_progress);
        loadingProgress.setVisibility(View.GONE);
    }


}
