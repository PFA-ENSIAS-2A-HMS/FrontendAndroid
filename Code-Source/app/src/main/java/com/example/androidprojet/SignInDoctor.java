package com.example.androidprojet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.androidprojet.database.DatabaseHelper;
import com.example.androidprojet.databinding.ActivitySignInDoctorBinding;
import com.example.androidprojet.enums.StatusDataBiometric;
import com.example.androidprojet.model.User;
import com.example.androidprojet.network.ApiConnection;
import com.example.androidprojet.utilities.Constants;
import com.example.androidprojet.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class SignInDoctor extends AppCompatActivity {

    private ActivitySignInDoctorBinding binding;
    private EditText inputEmail;
    private EditText password;
    private ProgressBar progressBarr;
    private Button signInButton;
    private PreferenceManager preferenceManager;
    private int codeReceived;
    private User docteur;
    private DatabaseHelper databaseHelper;
    private int getInfo;
    private String name;
    private String image;
    private int idHospital=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent intent = new Intent(getApplicationContext(), InsideAppDoctor.class);
            startActivity(intent);
            finish();
        }*/

        // Instancie Preference Manager
        preferenceManager = new PreferenceManager(getApplicationContext());

        inputEmail = findViewById(R.id.inputEmail);
        password = findViewById(R.id.password);
        signInButton = findViewById(R.id.signInButton);
        progressBarr = findViewById(R.id.progressBarr);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(isValidDetails()) {
                    if(SignInToSpringBoot()) {
                        SignUpToFirebase();
                        SignInToFirebase();
                    }else {
                        loading(false);
                        ShowToast("Votre compte n'existe pas. Veuillez contacter votre administrateur");
                    }
                }
            }
        });
    }

    private boolean isValidDetails() {
        String email = inputEmail.getText().toString();
        String pwd = password.getText().toString();
        reinitialize(inputEmail, password);
        if(inputEmail.equals("")){
            Toast.makeText(SignInDoctor.this, "the field email is required!", Toast.LENGTH_SHORT).show();
            inputEmail.setBackground(ContextCompat.getDrawable(SignInDoctor.this, R.drawable.edittext_border));
            return false;
        }else if (!isValidEmail(email)) {
            Toast.makeText(SignInDoctor.this, "Please enter a valid email address!", Toast.LENGTH_SHORT).show();
            inputEmail.setBackground(ContextCompat.getDrawable(SignInDoctor.this, R.drawable.edittext_border));
            return false;
        }else if(password.equals("")){
            Toast.makeText(SignInDoctor.this, "the field password is required!", Toast.LENGTH_SHORT).show();
            password.setBackground(ContextCompat.getDrawable(SignInDoctor.this, R.drawable.edittext_border));
            return false;
        }else{
            return true;
        }
    }

    private void reinitialize(EditText inputEmail,
                              EditText password) {
        inputEmail.setBackground(ContextCompat.getDrawable(SignInDoctor.this, R.drawable.input_name));
        password.setBackground(ContextCompat.getDrawable(SignInDoctor.this, R.drawable.input_name));
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void SignUpToFirebase() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME, name);
        user.put(Constants.KEY_EMAIL, inputEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD, password.getText().toString());
        user.put(Constants.KEY_IMAGE, image);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                        preferenceManager.putString(Constants.KEY_NAME, name);
                        preferenceManager.putString(Constants.KEY_IMAGE, image);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ShowToast(e.getMessage());
                    }
                });
    }

    private void SignInToFirebase() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, inputEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, password.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null
                    && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                        loading(false);
                        Intent intent = new Intent(SignInDoctor.this, InsideAppDoctor.class);
                        intent.putExtra("docteur", docteur);
                        startActivity(intent);
                        finish();
                    }else {
                        loading(false);
                        ShowToast("Vous n'avez pas de compte !");
                    }
                });
    }

    private void ShowToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean SignInToSpringBoot() {
        loading(true);
        codeReceived = 0;
        String apiUrl = ApiConnection.URL+"/api/v1/auth/login";
        String requestBody = toJSON();
        System.out.println(requestBody);
        ApiConnection apiConnection = new ApiConnection();

        final CountDownLatch latch = new CountDownLatch(1);

        apiConnection.postToApi(apiUrl, requestBody, new ApiConnection.Callback() {
            @Override
            public void onResponse(int Code,String response) {
                codeReceived = Code;
                if(Code==200){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int id = jsonObject.getInt("id");
                        String accessToken = jsonObject.getString("accessToken");
                        String role = jsonObject.getString("role");
                        docteur = new User(Integer.toString(id), Integer.toString(idHospital), role, accessToken, StatusDataBiometric.NOT_SUBMITTED);
                        //databaseHelper = new DatabaseHelper(getApplicationContext());
                        //databaseHelper.addUser(idString, binding.password.getText().toString(), role,accessToken, StatusDataBiometric.NOT_SUBMITTED);
                        codeReceived = getAlldata(accessToken, apiConnection, id);
                        latch.countDown();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }else{
                    latch.countDown();
                }

            }
            @Override
            public void onError(int Code,String error) {
                latch.countDown();
            }

            @Override
            public void onImageDownloaded(Bitmap image) {

            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(codeReceived == 200){
            return true;
        }else {
            return false;
        }
    }

    public  int getAlldata(String token, ApiConnection apiConnection, int id) {

        getInfo = 0;
        final CountDownLatch latch = new CountDownLatch(1);
        apiConnection.getFromApi(ApiConnection.URL+"/api/v1/doctors/mobile/hospital/"+id, token,new ApiConnection.Callback() {

            @Override
            public void onResponse(int code, String response) {
                getInfo = code;
                if (code == 200) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONObject doctorObject = jsonResponse.getJSONObject("doctor");
                        JSONObject hospitalObject = jsonResponse.getJSONObject("hospital");

                        // Extract doctor information
                        name = doctorObject.getString("lastName")+" "+doctorObject.getString("firstName");
                        image = ApiConnection.URL+"/api/v1/doctors/display/"+doctorObject.getString("image_url");

                        // Extract hospital information
                        idHospital = hospitalObject.getInt("id");

                        System.out.println("Hospital ID: " + idHospital);

                    } catch (JSONException e) {
                        // Handle JSON parsing exception
                        System.out.println("Error parsing JSON: " + e.getMessage());
                    }
                } else {
                    System.out.println("Error: Code " + code);
                }
                latch.countDown();
            }

            @Override
            public void onError(int code, String error) {
                latch.countDown();
            }

            @Override
            public void onImageDownloaded(Bitmap image) {
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return getInfo;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String toJSON() {
        // Création d'un objet JSON
        JSONObject json = new JSONObject();
        try {
            json.put("email", binding.inputEmail.getText().toString());
            json.put("password", binding.password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    private void loading(Boolean isLoading) {
        if(isLoading) {
            signInButton.setVisibility(View.INVISIBLE);
            progressBarr.setVisibility(View.VISIBLE);
        }else {
            signInButton.setVisibility(View.VISIBLE);
            progressBarr.setVisibility(View.INVISIBLE);
        }
    }
}
