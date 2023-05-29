package com.example.androidprojet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignInDoctor extends AppCompatActivity {
    private EditText inputEmail;
    private EditText password;
    private Button signUpButton;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_doctor);

        inputEmail = findViewById(R.id.inputEmail);
        password = findViewById(R.id.password);
        signInButton = findViewById(R.id.signInButton);

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
                    intent.putExtra("role","VETERINAIRE");
                    startActivity(intent);
                }*/
                startActivity(new Intent(SignInDoctor.this, InsideAppDoctor.class));
            }
        });
    }

    private void reinitialize(EditText inputEmail,
                              EditText password) {
        inputEmail.setBackground(ContextCompat.getDrawable(SignInDoctor.this, R.drawable.input_name));
        password.setBackground(ContextCompat.getDrawable(SignInDoctor.this, R.drawable.input_name));
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }
}