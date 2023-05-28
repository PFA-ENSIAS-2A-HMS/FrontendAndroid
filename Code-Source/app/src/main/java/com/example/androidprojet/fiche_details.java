package com.example.androidprojet;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidprojet.fragments.HomeFragment;

public class fiche_details extends AppCompatActivity {

    ImageButton imageButton;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_details);
        textView=findViewById(R.id.detailDesc);
        textView.setMovementMethod(new ScrollingMovementMethod());
        (findViewById(R.id.imageButton1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}