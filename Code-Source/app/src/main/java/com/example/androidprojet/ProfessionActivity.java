package com.example.androidprojet;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ProfessionActivity extends AppCompatActivity {
    public ImageButton buttonPatient;
    public ImageButton buttonDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profession2);

        buttonPatient =(ImageButton) findViewById(R.id.patientButton);
        buttonPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfessionActivity.this, SignUpPatient.class);
                startActivity(intent);
            }
        });

        buttonDoc=(ImageButton) findViewById(R.id.DocButton);
        buttonDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfessionActivity.this, SignInDoctor.class));
            }
        });
    }
}