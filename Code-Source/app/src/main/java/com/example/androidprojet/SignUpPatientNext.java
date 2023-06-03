package com.example.androidprojet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidprojet.model.Patient;
import com.google.android.material.imageview.ShapeableImageView;
import java.io.IOException;

public class SignUpPatientNext extends AppCompatActivity {
  public Button buttonSuivant, buttonPrecedent;
  private EditText phoneNumber ;
  private Patient patient;
  private String imagePath = "";
  private Bitmap bitmap;
  private  Bitmap bitmapCin;
  private static final int PICK_IMAGE_REQUEST = 1;
  private ShapeableImageView buttonimage;
  private int idHopital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification_smsv2_old);
        // Récuperer le patient
        this.patient = getEleveur();

        phoneNumber = findViewById(R.id.input_numero);
        buttonSuivant = (Button) findViewById(R.id.bouttonSuivant);
        buttonPrecedent = (Button) findViewById(R.id.precedentBouton);

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
                String numero = phoneNumber.getText().toString();

                /*if(imagePath.equals("")){
                    Toast.makeText(SignUpPatientNext.this, "the image field is required !", Toast.LENGTH_SHORT).show();
                }else*/
                if(numero.equals("")){
                    phoneNumber.setBackground(ContextCompat.getDrawable(SignUpPatientNext.this, R.drawable.edittext_border));
                    Toast.makeText(SignUpPatientNext.this, "Le champ numéro de téléphone est requis !", Toast.LENGTH_SHORT).show();
                }else{
                    patient.setPhoneNumber(numero);
                    //patient.setProfile(imagePath);

                    Intent intent= new Intent(SignUpPatientNext.this, PasswordActivity.class);
                    intent.putExtra("patient", patient);
                    intent.putExtra("id", idHopital);

                    /*File directory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "VotreDossier");
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
                    intent.putExtra("cinPath", cinPath);*/

                    startActivity(intent);
                }
                //startActivity(new Intent(SignUpPatientNext.this, PasswordActivity.class));
            }
        });

        buttonPrecedent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(SignUpPatientNext.this, SignUpPatient.class);
                startActivity(intent);
            }
        });
    }
    public Patient getEleveur(){
        Intent intent = getIntent();
        Patient patient = (Patient) intent.getSerializableExtra("patient");
        if (intent.hasExtra("id")) {
            idHopital = intent.getIntExtra("id", 1);
        }
        return patient;
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