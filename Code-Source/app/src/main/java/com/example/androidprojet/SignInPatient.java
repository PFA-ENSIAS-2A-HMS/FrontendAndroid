package com.example.androidprojet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignInPatient extends AppCompatActivity {

    private EditText inputPhoneNumber;
    private EditText passwordPatSignIn;
    private Button signInButtonPat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_patient);

        inputPhoneNumber = findViewById(R.id.inputPhoneNumber);
        passwordPatSignIn = findViewById(R.id.passwordPatSignIn);
        signInButtonPat = findViewById(R.id.signInButtonPat);

        signInButtonPat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = inputPhoneNumber.getText().toString();
                String pwd = passwordPatSignIn.getText().toString();
                reinitialize(inputPhoneNumber, passwordPatSignIn);
                if(phoneNumber.equals("")){
                    Toast.makeText(SignInPatient.this, "Le champ téléphone est requis !", Toast.LENGTH_SHORT).show();
                    inputPhoneNumber.setBackground(ContextCompat.getDrawable(SignInPatient.this, R.drawable.edittext_border));
                }else if(pwd.equals("")){
                    Toast.makeText(SignInPatient.this, "Le champ mot de passe est requis !", Toast.LENGTH_SHORT).show();
                    passwordPatSignIn.setBackground(ContextCompat.getDrawable(SignInPatient.this, R.drawable.edittext_border));
                }else{
                    //Toast.makeText(SignInDoctor.this, "login with success (:", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignInPatient.this, InsideAppPatient.class);
                    intent.putExtra("login",phoneNumber);
                    intent.putExtra("password",pwd);
                    intent.putExtra("role","patient");
                    startActivity(intent);
                }
            }
        });

    }

    private void reinitialize(EditText inputEmail,
                              EditText password) {
        inputEmail.setBackground(ContextCompat.getDrawable(SignInPatient.this, R.drawable.input_name));
        password.setBackground(ContextCompat.getDrawable(SignInPatient.this, R.drawable.input_name));
    }
}