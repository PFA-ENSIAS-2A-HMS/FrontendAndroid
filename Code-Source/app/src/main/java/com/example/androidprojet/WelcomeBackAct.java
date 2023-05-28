package com.example.androidprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class WelcomeBackAct extends AppCompatActivity {
    public Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_back);
        Intent intent = new Intent(this,ProfessionActivity.class);
        button = (Button) findViewById(R.id.bouttonSignin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfessionActivity.class);
                startActivity(intent);
            }
        });
    }
}