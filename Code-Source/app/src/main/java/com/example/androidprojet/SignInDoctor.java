package com.example.androidprojet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.androidprojet.utilities.Constants;
import com.example.androidprojet.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignInDoctor extends AppCompatActivity {
    private EditText inputEmail;
    private EditText password;
    private ProgressBar progressBarr;
    private Button signInButton;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_doctor);

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
            @Override
            public void onClick(View view) {
                /*String email = inputEmail.getText().toString();
                String pwd = password.getText().toString();
                reinitialize(inputEmail, password);
                if(inputEmail.equals("")){
                    Toast.makeText(SignInDoctor.this, "the field email is required!", Toast.LENGTH_SHORT).show();
                    inputEmail.setBackground(ContextCompat.getDrawable(SignInDoctor.this, R.drawable.edittext_border));
                }else if (!isValidEmail(email)) {
                    Toast.makeText(SignInDoctor.this, "Please enter a valid email address!", Toast.LENGTH_SHORT).show();
                    inputEmail.setBackground(ContextCompat.getDrawable(SignInDoctor.this, R.drawable.edittext_border));
                }else if(password.equals("")){
                    Toast.makeText(SignInDoctor.this, "the field password is required!", Toast.LENGTH_SHORT).show();
                    password.setBackground(ContextCompat.getDrawable(SignInDoctor.this, R.drawable.edittext_border));
                }else{
                    //Toast.makeText(SignInDoctor.this, "login with success (:", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignInDoctor.this, InsideAppPatient.class);
                    intent.putExtra("login",email);
                    intent.putExtra("password",pwd);
                    intent.putExtra("role","");
                    startActivity(intent);
                }*/

                if(isValidDetails()) {
                    SignUpToFirebase();
                    SignInToFirebase();
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
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME, "mouad");
        user.put(Constants.KEY_EMAIL, inputEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD, password.getText().toString());
        user.put(Constants.KEY_IMAGE, "example.png");
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                        preferenceManager.putString(Constants.KEY_NAME, "mouad");
                        preferenceManager.putString(Constants.KEY_IMAGE, "example.png");
                        startActivity(new Intent(SignInDoctor.this, InsideAppDoctor.class));
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
                        //ShowToast("NAME : "+documentSnapshot.getString(Constants.KEY_NAME));
                        // Add intent to InsideAppDoctor
                    }else {
                        loading(false);
                        ShowToast("Vous n'avez pas de compte !");
                    }
                });
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

    private void ShowToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
