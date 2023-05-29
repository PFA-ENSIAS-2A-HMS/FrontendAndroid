package com.example.androidprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidprojet.model.Eleveur;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CinActivity extends AppCompatActivity {

    private Eleveur receivedEleveur;
    public Button precedentButton;
    public ImageButton cameraButton;

    public Button suivantButton;
    public TextView alertPictureCin;
    private static final int PICK_IMAGE_REQUEST = 1;
    private String imagePath;
    private Bitmap bitmap;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cin2);
        cameraButton=(ImageButton) findViewById(R.id.cameraButton);
        precedentButton=(Button)findViewById(R.id.precedentBouton);
        suivantButton=(Button)findViewById(R.id.bouttonSuivant);
        alertPictureCin = (TextView)findViewById(R.id.alertPictureCin);

        alertPictureCin.setVisibility(View.GONE);
        Intent intent = getIntent();
        receivedEleveur = (Eleveur)intent.getSerializableExtra("ELEVEUR_OBJECT");
        if (receivedEleveur == null) {
                System.out.println("Error : Object doen't 'Eleveur'\n");
        }
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        precedentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CinActivity.this,ProfessionActivity.class);
                startActivity(intent);
            }
        });
        suivantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(receivedEleveur.getCinPath() != null){
                    Intent intent = new Intent(CinActivity.this, SignUpPatient.class);
                    intent.putExtra("eleveur", receivedEleveur);

                    ProgressDialog progressDialog = new ProgressDialog(CinActivity.this);
                    progressDialog.setMessage("veuillez patienter...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    File directory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "VotreDossier");
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }
                    File file = new File(directory, "cin.png");

                    try {
                        FileOutputStream outputStream = new FileOutputStream(file);
                        bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, true);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                    intent.putExtra("cinPath", file.getAbsolutePath());
                    startActivity(intent);
                }else{
                    Toast.makeText(CinActivity.this, "You should upload your cin picture !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                imagePath = uri.getPath();
                cameraButton.setImageBitmap(bitmap);
                receivedEleveur.setCinPath(imagePath);
                alertPictureCin.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}