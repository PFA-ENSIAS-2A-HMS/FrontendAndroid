package com.example.androidprojet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidprojet.database.DatabaseHelper;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    public Button button;
    private DatabaseHelper databaseHelper;
    private ImageButton buttonFr;
    private ImageButton buttonAr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_interface);

        button = (Button) findViewById(R.id.buttonCommencer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ProfessionActivity.class));
            }
        });
        buttonFr = findViewById(R.id.buttonFrance);
        buttonAr = findViewById(R.id.buttonUEA);
        buttonFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguage("fr");
            }
        });
        buttonAr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguage("en");
            }
        });


    }
    public void changeLanguage(String languageCode) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Modification de la langue en cours...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Locale newLocale = new Locale(languageCode);
                Locale.setDefault(newLocale);
                Configuration configuration = getResources().getConfiguration();
                configuration.setLocale(newLocale);
                getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
                progressDialog.dismiss();
                recreate();
            }
        }, 1000);
    }

}