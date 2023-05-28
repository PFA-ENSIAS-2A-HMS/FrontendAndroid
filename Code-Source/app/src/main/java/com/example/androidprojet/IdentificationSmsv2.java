package com.example.androidprojet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidprojet.model.Eleveur;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class IdentificationSmsv2 extends AppCompatActivity {
  public Button buttonSuivant, buttonPrecedent;
  private EditText input_numero ;
  private Eleveur eleveur ;
  private String imagePath = "";
  private Bitmap bitmap;
  private  Bitmap bitmapCin;
  private static final int PICK_IMAGE_REQUEST = 1;
  private ShapeableImageView buttonimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification_smsv2_old);

        this.eleveur = getEleveur();
        buttonSuivant = (Button) findViewById(R.id.bouttonSuivant);
        buttonPrecedent = (Button) findViewById(R.id.precedentBouton);
        input_numero = findViewById(R.id.input_numero);
        // acces a la galerie
        buttonimage = findViewById(R.id.imvCircularWithStroke);
        buttonimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer une intention pour ouvrir la galerie de photos
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        buttonSuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String numero = input_numero.getText().toString();
                // definir le numero de l'eleveur
                //eleveur.setPhoneNumber(numero);
                //eleveur.setProfile(imagePath);
                /*if(imagePath.equals("")){
                    Toast.makeText(IdentificationSmsv2.this, "the image field is required !", Toast.LENGTH_SHORT).show();
                }else if(numero.equals("")){
                    input_numero.setBackground(ContextCompat.getDrawable(IdentificationSmsv2.this, R.drawable.edittext_border));
                    Toast.makeText(IdentificationSmsv2.this, "the number field is required !", Toast.LENGTH_SHORT).show();
                }else{
                    eleveur.setPhoneNumber(numero);
                    eleveur.setProfile(imagePath);
                    Intent intent= new Intent(IdentificationSmsv2.this,Pwdactivity.class);
                    intent.putExtra("eleveur", eleveur);

                    File directory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "VotreDossier");
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }
                    File file = new File(directory, "profile.png");

                    try {
                        FileOutputStream outputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    intent.putExtra("profilePath", file.getAbsolutePath());
                    String cinPath = getIntent().getStringExtra("cinPath");
                    intent.putExtra("cinPath", cinPath);

                    startActivity(intent);
                }*/
                startActivity(new Intent(IdentificationSmsv2.this,Pwdactivity.class));
            }
        });

        buttonPrecedent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(IdentificationSmsv2.this, IdentificationPatient.class);
                startActivity(intent);
            }
        });
    }
    public Eleveur getEleveur(){
        Intent intent = getIntent();
        Eleveur eleveur = (Eleveur) intent.getSerializableExtra("eleveur");
        return eleveur;
    }
    // Recevoir les résultats de l'intention pour sélectionner une photo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Récupérer l'URI de l'image sélectionnée dans la galerie
            Uri uri = data.getData();

            try {
                // Convert the URI to a Bitmap object
                //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                //imagePath = uri.getPath();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imagePath = getRealPathFromURI(uri);
                buttonimage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return null;
    }

}