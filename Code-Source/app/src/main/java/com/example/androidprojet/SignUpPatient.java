package com.example.androidprojet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.androidprojet.model.Eleveur;

import java.util.Calendar;


public class SignUpPatient extends AppCompatActivity {
    public Button buttonPrecedent,buttonSuivant;
    private String selectedGender = "";
    private Eleveur eleveur;
    private Bitmap bitmapCin;
    private EditText dataNaissanceEditText;
    private EditText inputNom;
    private EditText inputPrenom;
    private EditText inputCin;
    private EditText inputAdress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eleveur = getEleveur();

        setContentView(R.layout.activity_patient_identification);
        inputNom = findViewById(R.id.input_nom);
        inputPrenom = findViewById(R.id.input_prenom);
        inputCin = findViewById(R.id.cin_patient);
        inputAdress = findViewById(R.id.adresse_patient);
        dataNaissanceEditText = findViewById(R.id.dataNaissance);

        Button buttonFemale = findViewById(R.id.button_female);
        Button buttonMale = findViewById(R.id.button_male);

        buttonSuivant = (Button) findViewById(R.id.bouttonSuivant);
        buttonPrecedent = (Button) findViewById(R.id.precedentBouton);


        buttonFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedGender = "F";
            }
        });

        buttonMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedGender = "M";
            }
        });

        // Définir l'écouteur de clic sur l'EditText
        dataNaissanceEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        buttonPrecedent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(SignUpPatient.this,ProfessionActivity.class);
                startActivity(intent);
            }
        });
        buttonSuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //extraire les valeurs saisies par l'utilisateur dans les champs de saisie

                String nom = inputNom.getText().toString();

                String prenom = inputPrenom.getText().toString();
                String cin = inputCin.getText().toString();
                String adress = inputAdress.getText().toString();


                // naviguer vers l'activite suivante et envoyer les donnee collecter de cette activite
                reinitialize(inputNom,inputPrenom,inputCin,inputAdress);
                /*if(selectedGender.equals("")){
                    Toast.makeText(SignUpPatient.this, "the field civilité  is required ! ", Toast.LENGTH_SHORT).show();
                }else if(nom.equals("")){
                    inputNom.setBackground(ContextCompat.getDrawable(SignUpPatient.this, R.drawable.edittext_border));
                    Toast.makeText(SignUpPatient.this, "the field nom francais is required ! ", Toast.LENGTH_SHORT).show();
                }else if(prenom.equals("")){
                    inputPrenom.setBackground(ContextCompat.getDrawable(SignUpPatient.this, R.drawable.edittext_border));
                    Toast.makeText(SignUpPatient.this, "the field nom arabe is required ! ", Toast.LENGTH_SHORT).show();
                }else if(cin.equals("")){
                    inputCin.setBackground(ContextCompat.getDrawable(SignUpPatient.this, R.drawable.edittext_border));
                    Toast.makeText(SignUpPatient.this, "the field cin is required !", Toast.LENGTH_SHORT).show();
                }else if(adress.equals("")){
                    inputAdress.setBackground(ContextCompat.getDrawable(SignUpPatient.this, R.drawable.edittext_border));
                    Toast.makeText(SignUpPatient.this, "the field adress is required !", Toast.LENGTH_SHORT).show();
                }else{
                    eleveur.setFullNameAr(prenom);
                    eleveur.setFullNameFr(nom);
                    eleveur.setCin(cin);
                    eleveur.setGender(selectedGender);
                    eleveur.setAddress(adress);
                    Intent intent= new Intent(SignUpPatient.this,IdentificationSmsv2.class);
                    intent.putExtra("eleveur", eleveur);

                    String cinPath = getIntent().getStringExtra("cinPath");
                    intent.putExtra("cinPath", cinPath);
                    startActivity(intent);
                }*/
                startActivity(new Intent(SignUpPatient.this,IdentificationSmsv2.class));
            }
        });
    }

    private void showDatePicker() {
        // Obtenir la date actuelle
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Créer une instance de DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(SignUpPatient.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Mettre à jour l'EditText avec la date sélectionnée
                        String date = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        dataNaissanceEditText.setText(date);
                    }
                }, year, month, dayOfMonth);

        // Afficher le DatePickerDialog
        datePickerDialog.show();
    }

    public Eleveur getEleveur(){
        Intent intent = getIntent();
        Eleveur eleveur = (Eleveur) intent.getSerializableExtra("eleveur");
        return eleveur;
    }
   public void reinitialize(EditText inputNomCompletFr,EditText inputNomCompletAr,
                            EditText inputCin,EditText inputAdress ){
       inputNomCompletFr.setBackground(ContextCompat.getDrawable(SignUpPatient.this, R.drawable.input_name));
       inputNomCompletAr.setBackground(ContextCompat.getDrawable(SignUpPatient.this, R.drawable.input_name));
       inputCin.setBackground(ContextCompat.getDrawable(SignUpPatient.this, R.drawable.input_name));
       inputAdress.setBackground(ContextCompat.getDrawable(SignUpPatient.this, R.drawable.input_name));
   }

}