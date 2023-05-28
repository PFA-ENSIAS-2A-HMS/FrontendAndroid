package com.example.androidprojet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.androidprojet.database.DatabaseHelper;
import com.example.androidprojet.model.User;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

public class BackPage2Activity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private boolean isFirstLaunch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.back_page);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        int count = databaseHelper.getTotalUserCount();

        if (isFirstLaunch) {
            isFirstLaunch = false;
            if (count != 0) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        User user = databaseHelper.getUser();
                        Intent intent = new Intent(BackPage2Activity.this, Attachs1.class);
                        intent.putExtra("login", user.getLogin());
                        intent.putExtra("password", user.getPassword());
                        intent.putExtra("role", user.getRole());
                        intent.putExtra("isAthenticated", "true");
                        startActivity(intent);
                        finish();
                    }
                }, 1500);
            } else {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(BackPage2Activity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 1500);
            }
        } else {
            finish();
        }
    }
}
