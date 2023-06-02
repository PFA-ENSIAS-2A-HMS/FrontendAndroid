package com.example.androidprojet;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.example.androidprojet.model.Patient;
import com.example.androidprojet.model.Profil;

public class ProfessionActivity extends AppCompatActivity {
    private Patient patient;
    private Profil veterinaire;
    public ImageButton buttonEleveur;
    public ImageButton buttonVet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profession2);
        buttonEleveur =(ImageButton) findViewById(R.id.eleveurButton);
        buttonEleveur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patient = new Patient();
                Intent intent = new Intent(ProfessionActivity.this, SignUpPatient.class);
                intent.putExtra("ELEVEUR_OBJECT", patient);
                startActivity(intent);
            }
        });

        buttonVet=(ImageButton) findViewById(R.id.vetButton);
        buttonVet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*veterinaire = new Profil();
                veterinaire.setRole("VETERINAIRE");
                Intent intent = new Intent(ProfessionActivity.this, SignInDoctor.class);
                intent.putExtra("VETERINAIRE_OBJECT", veterinaire);
                startActivity(intent);*/

                startActivity(new Intent(ProfessionActivity.this, SignInDoctor.class));
            }
        });
    }
}