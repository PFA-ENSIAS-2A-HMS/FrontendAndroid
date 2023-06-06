package com.example.androidprojet;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.androidprojet.database.DatabaseHelper;
import com.example.androidprojet.fragments.ListPatients;
import com.example.androidprojet.fragments.MessageFragment;
import com.example.androidprojet.fragments.ProfilFragment;
import com.example.androidprojet.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InsideAppDoctor extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private DatabaseHelper databaseHelper;
    private User docteur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_app_doctor);

        bottomNavigationView = findViewById(R.id.bottom_navigation);//
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);//

        databaseHelper = new DatabaseHelper(this);
        getDocteur();

        // Ajouter le fragment specialiste par défaut
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_doctor,
                new ListPatients()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_patients:
                            selectedFragment = new ListPatients();
                            break;
                        case R.id.nav_message:
                            selectedFragment = new MessageFragment();
                            break;
                        case R.id.nav_profil:
                            selectedFragment = new ProfilFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_doctor,
                            selectedFragment).commit();

                    return true;
                }
            };
    public void getDocteur() {
        Intent intent = getIntent();
        docteur = (User) intent.getSerializableExtra("docteur");
        databaseHelper.addUser(docteur.getLogin(), docteur.getPassword(), docteur.getRole(),docteur.getToken(),docteur.getDataBiometric());
    }
}