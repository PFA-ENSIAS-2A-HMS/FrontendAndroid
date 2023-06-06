package com.example.androidprojet;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.androidprojet.fragments.DataBiometricFragment;
import com.example.androidprojet.fragments.HomeFragment;
import com.example.androidprojet.fragments.NotificationFragment;
import com.example.androidprojet.fragments.ProfilFragment;
import com.example.androidprojet.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InsideAppPatient extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private User patientReceived;
    private String isAuthenticated = "";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_app_patient);

        recupererData();

        bottomNavigationView = findViewById(R.id.btn_navigation);//
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);//

        // Ajouter le fragment Home par défaut
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_patient,
                new HomeFragment()).commit();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void recupererData(){
        Intent intent = getIntent();
        if(intent != null) {
            patientReceived = (User) intent.getSerializableExtra("patient");
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_comite:
                            selectedFragment = new DataBiometricFragment();
                            break;
                        case R.id.nav_search:
                            selectedFragment = new NotificationFragment();
                            break;
                        case R.id.nav_profil:
                            selectedFragment = new ProfilFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_patient,
                            selectedFragment).commit();

                    return true;
                }
            };
}