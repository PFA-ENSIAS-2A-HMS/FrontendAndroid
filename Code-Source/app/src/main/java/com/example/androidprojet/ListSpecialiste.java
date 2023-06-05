package com.example.androidprojet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidprojet.adapter.UserAdapter;
import com.example.androidprojet.databinding.ActivityListSpecialisteBinding;
import com.example.androidprojet.model.FireBaseUser;
import com.example.androidprojet.model.Patient;
import com.example.androidprojet.network.ApiConnection;
import com.example.androidprojet.utilities.Constants;
import com.example.androidprojet.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ListSpecialiste extends AppCompatActivity {
    private final ArrayList<Patient> doctors = new ArrayList<>();
    private ProgressBar progressBarSpecialiste;
    private ActivityListSpecialisteBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListSpecialisteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());

        progressBarSpecialiste = findViewById(R.id.progressBarSpecialiste);

        // En relation avec firebase
        getUsers();

        /*getDoctors();

        RecyclerView rv = findViewById(R.id.usersRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new PatientAdapter(this, doctors, new PatientAdapter.ItemClick() {
            @Override
            public void onItemClick(Patient patient) {

            }
        }));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3500); // Attendre pendant 3 secondes (3000 millisecondes)
                    progressBarSpecialiste.setVisibility(View.INVISIBLE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void getDoctors() {
        final CountDownLatch latch = new CountDownLatch(1);

        // Récupérer toutes les hôpitaux
        ApiConnection apiConnection = new ApiConnection();
        apiConnection.getFromApi(ApiConnection.URL+"/api/v1/doctors", new ApiConnection.Callback() {
            @Override
            public void onResponse(int code, String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    String firstName, lastName, phoneNumber, image_url;
                    int id;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        firstName = jsonObject.getString("firstName");
                        lastName = jsonObject.getString("lastName");
                        phoneNumber = jsonObject.getString("email");
                        image_url = jsonObject.getString("image_url");
                        id = jsonObject.getInt("id");
                        Patient patient = new Patient(
                                firstName,
                                lastName,
                                phoneNumber,
                                ApiConnection.URL+"/api/v1/doctors/display/"+image_url,
                                Long.toString(id));

                        doctors.add(patient);
                        //System.out.println("Patient: " + patient + "\n");
                    }
                    latch.countDown();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void onError(int code, String error) {
                System.out.println("Error, code :"+code);
                //latch.countDown();
            }

            @Override
            public void onImageDownloaded(Bitmap image) {
                // Ignore this for now, since it is not used in this code snippet
            }
        });

        // Wait for the latch to be released before proceeding
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getUsers() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if(task.isSuccessful() && task.getResult() != null) {
                        ArrayList<FireBaseUser> users = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if(currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }
                            FireBaseUser user = new FireBaseUser();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            users.add(user);
                        }
                        if(users.size() > 0) {
                            UserAdapter userAdapter = new UserAdapter(users, new UserAdapter.ItemClickListener() {
                                @Override
                                public void onItemClick(FireBaseUser fireBaseUser) {
                                   Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                                   intent.putExtra(Constants.KEY_USER, fireBaseUser);
                                   startActivity(intent);
                                   finish();
                                }
                            });
                            binding.usersRecyclerView.setAdapter(userAdapter);
                            binding.usersRecyclerView.setVisibility(View.VISIBLE);
                        }else {
                            showErrorMessage();
                        }
                    }else {
                        showErrorMessage();
                    }
                });
    }

    private void loading(Boolean isLoading) {
        if(isLoading) {
            binding.progressBarSpecialiste.setVisibility(View.VISIBLE);
        }else {
            binding.progressBarSpecialiste.setVisibility(View.INVISIBLE);
        }
    }

    private void showErrorMessage() {
        binding.textErrorMessage.setText(String.format("%s", "Aucun spécialiste trouvé !"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

}