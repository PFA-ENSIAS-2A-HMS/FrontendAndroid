package com.example.androidprojet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidprojet.adapter.UserAdapter;
import com.example.androidprojet.database.DatabaseHelper;
import com.example.androidprojet.databinding.ActivityListSpecialisteBinding;
import com.example.androidprojet.model.FireBaseUser;
import com.example.androidprojet.model.User;
import com.example.androidprojet.network.ApiConnection;
import com.example.androidprojet.utilities.Constants;
import com.example.androidprojet.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class ListSpecialiste extends AppCompatActivity {
    private ProgressBar progressBarSpecialiste;
    private ActivityListSpecialisteBinding binding;
    private PreferenceManager preferenceManager;
    private DatabaseHelper databaseHelper;
    private User docteur;
    private boolean getValid=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListSpecialisteBinding.inflate(getLayoutInflater());

        databaseHelper = new DatabaseHelper(this);
        docteur = databaseHelper.getUser();

        preferenceManager = new PreferenceManager(getApplicationContext());

        getDoctors();
            // En relation avec firebase
        setContentView(binding.getRoot());

        /*RecyclerView rv = findViewById(R.id.usersRecyclerView);
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

    public boolean getDoctors() {
        //loading(true);
        final CountDownLatch latch = new CountDownLatch(1);

        // Récupérer toutes les hôpitaux
        ApiConnection apiConnection = new ApiConnection();
        apiConnection.getFromApi(ApiConnection.URL+"/api/v1/doctors/hospitals/"+docteur.getPassword(), docteur.getToken(), new ApiConnection.Callback() {
            @Override
            public void onResponse(int code, String response) {
                System.out.println("response : "+response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    String firstName, lastName, image_url, email, passwrod;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        firstName = jsonObject.getString("firstName");
                        lastName = jsonObject.getString("lastName");
                        image_url = jsonObject.getString("image_url");
                        email = jsonObject.getString("email");
                        passwrod = jsonObject.getString("password");
                        int id = jsonObject.getInt("id");

                        SignUpToFirebase(lastName+" "+firstName, email, passwrod, ApiConnection.URL+"/api/v1/doctors/display/"+image_url, Integer.toString(id));

                    }
                    getUsers();
                    getValid = false;
                    latch.countDown();
                } catch (Exception e) {
                    latch.countDown();
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void onError(int code, String error) {
                System.out.println("Error, code :"+code);
                latch.countDown();
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
        if(getValid) {
            return true;
        }else {
            return false;
        }
    }

    private void SignUpToFirebase(String name, String email, String password, String image, String id) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        // Vérifier si le docteur existe déjà dans Firebase
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_USER_ID, id)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot.isEmpty()) {
                        // Le docteur n'existe pas encore dans Firebase, on peut l'ajouter
                        HashMap<String, Object> user = new HashMap<>();
                        user.put(Constants.KEY_NAME, name);
                        user.put(Constants.KEY_EMAIL, email);
                        user.put(Constants.KEY_PASSWORD, password);
                        user.put(Constants.KEY_IMAGE, image);
                        user.put(Constants.KEY_USER_ID, id);

                        database.collection(Constants.KEY_COLLECTION_USERS)
                                .add(user)
                                .addOnSuccessListener(documentReference -> {
                                    // Succès de l'ajout du docteur dans Firebase
                                })
                                .addOnFailureListener(e -> {
                                    // Échec de l'ajout du docteur dans Firebase
                                });
                    } else {
                        // Le docteur existe déjà dans Firebase, on ne fait rien
                    }
                })
                .addOnFailureListener(e -> {
                    // Échec de la requête pour vérifier l'existence du docteur dans Firebase
                });
    }


    /*private void getUsers() {
        //final CountDownLatch latch = new CountDownLatch(1);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    //loading(false);
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
                            user.id = queryDocumentSnapshot.getId();
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
                        //latch.countDown();
                    }else {
                        showErrorMessage();
                        //latch.countDown();
                    }

                });
    }*/

    private void getUsers() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);

        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Set<FireBaseUser> users = new HashSet<>();
                         Set<String> emails = new HashSet<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            String userId = queryDocumentSnapshot.getId();

                            // Vérifier si l'utilisateur est le docteur actuellement authentifié
                            if (currentUserId.equals(userId)) {
                                continue; // Ignorer le docteur actuellement authentifié
                            }

                            FireBaseUser user = new FireBaseUser();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.id = userId;
                            if(!emails.contains(user.email)){
                                emails.add(user.email);
                                users.add(user);
                            }

                        }

                        List<FireBaseUser> lists = new ArrayList<>(users);
                        if (users.size() > 0) {
                            UserAdapter userAdapter = new UserAdapter(lists, fireBaseUser -> {
                                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                                intent.putExtra(Constants.KEY_USER, fireBaseUser);
                                startActivity(intent);
                                finish();
                            });
                            binding.usersRecyclerView.setAdapter(userAdapter);
                            binding.usersRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            showErrorMessage();
                        }
                    } else {
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

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}