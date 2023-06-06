package com.example.androidprojet.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidprojet.MainActivity;
import com.example.androidprojet.R;
import com.example.androidprojet.database.DatabaseHelper;
import com.example.androidprojet.model.User;
import com.example.androidprojet.network.ApiConnection;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CountDownLatch;


public class ProfilFragment extends Fragment {
    private DatabaseHelper databaseHelper;
    private User user;
    private String profile;
    private TextView displayRole;
    private TextView logoutView;
    private Bitmap bitmap;
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

        ApiConnection apiConnection = new ApiConnection();
        databaseHelper = new DatabaseHelper(getContext());
        user = databaseHelper.getUser();
        //Toast.makeText(getContext(), ""+user.getLogin(), Toast.LENGTH_SHORT).show();
        if(user.getRole().equals("patient")){
            profile_patient(view,apiConnection);
        }else{
            profile_docteur(view,apiConnection);
        }
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
    private void profile_docteur(View view, ApiConnection apiConnection){
        displayRole = getActivity().findViewById(R.id.textView4);
        displayRole.setText(user.getRole().equals("patient") ? "Patient":  "Docteur");
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Chargement des données en cours...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        apiConnection.getFromApi(ApiConnection.URL+"/api/v1/doctors/"+user.getLogin(), user.getToken(), new ApiConnection.Callback() {

            @Override
            public void onResponse(int code, String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    System.out.println("response : "+response);

                    if (jsonObject != null) {
                        String firstName = jsonObject.getString("firstName");
                        String lastName = jsonObject.getString("lastName");
                        String location = jsonObject.getString("location");
                        String phoneNumber = jsonObject.getString("phoneNumber");
                        String dateOfBirth = jsonObject.getString("date_of_birth");
                        String password = jsonObject.getString("password");
                        String imageUrl = jsonObject.getString("image_url");



                        EditText NameProfil = view.findViewById(R.id.NameProfil);
                        NameProfil.setText(lastName +" "+ firstName);
                        EditText cinProfil = view.findViewById(R.id.cinProfil);
                        cinProfil.setText(dateOfBirth);

                        EditText addresse = view.findViewById(R.id.addressProfil);
                        if(location.equals("null")) {
                            addresse.setText("Votre location ?");
                        }else {
                            addresse.setText(location);
                        }
                        EditText telephone = view.findViewById(R.id.phoneNumber);
                        telephone.setText(phoneNumber);
                        EditText bloodType = view.findViewById(R.id.bloodTypeProfil);
                        bloodType.setText(password);
                        EditText passwordP = view.findViewById(R.id.passwordProfil);
                        passwordP.setText(password);
                        ProgressBar loadingProgress = view.findViewById(R.id.loading_progress);

                        final CountDownLatch latch = new CountDownLatch(1);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    InputStream inputStream = new URL(ApiConnection.URL+"/api/v1/doctors/display/"+imageUrl).openStream();
                                    bitmap = BitmapFactory.decodeStream(inputStream);
                                    updateProfilePicture(bitmap, view);
                                    latch.countDown();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                latch.countDown();
                            }
                        }).start();

                        try {
                            latch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                        loadingProgress.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
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

    private void updateProfilePicture(Bitmap image, View view) {
        /*ShapeableImageView profilePictureImageView = getActivity().findViewById(R.id.profile_picture);
        profilePictureImageView.setImageBitmap(image);
        profilePictureImageView.setVisibility(View.VISIBLE);
        ProgressBar loadingProgress = getActivity().findViewById(R.id.loading_progress);
        loadingProgress.setVisibility(View.INVISIBLE);*/
    }


}
