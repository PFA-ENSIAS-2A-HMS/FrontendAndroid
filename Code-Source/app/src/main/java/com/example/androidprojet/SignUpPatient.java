package com.example.androidprojet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.androidprojet.model.Patient;
import com.example.androidprojet.network.ApiConnection;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class SignUpPatient extends AppCompatActivity {
    private Patient patient;
    private Bitmap bitmapCin;
    private Button buttonMale;
    private Button buttonFemale;
    private EditText firstName;
    private EditText lastName;
    private EditText gender;
    private EditText cin;
    private EditText birthdate;
    private EditText address;
    private Spinner hospital_spinner;
    public Button buttonPrecedent,buttonSuivant;
    private String selectedGender = "";
    public Map<String, Integer> HSP = new HashMap<>();
    private int selectedValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Téléchargement des hopitaux en cours...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Supprimer tous dans le map HSP
        HSP.clear();

        // Create a CountDownLatch with initial count 1
        final CountDownLatch latch = new CountDownLatch(1);
        setContentView(R.layout.activity_patient_identification);

        // Récuperer toutes les hopitaux
        ApiConnection apiConnection = new ApiConnection();
        apiConnection.getFromApi(ApiConnection.URL+"/api/v1/hospitals/all", new ApiConnection.Callback() {
            @Override
            public void onResponse(int code, String response) {
                try {
                    //JSONObject jsonObject = new JSONObject(response);
                    //System.out.println("Hopitaux : "+response);
                    JSONArray jsonArray = new JSONArray(response); // jsonString is the JSON string you provided
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        Integer id = jsonObject.getInt("id");
                        HSP.put(name, id);
                    }
                    System.out.println(HSP);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000); // Wait for 2 seconds
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        // Release the latch to signal that the map is filled
                                    }
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    latch.countDown();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
            @Override
            public void onError(int code, String error) {
                progressDialog.dismiss();
                latch.countDown();
            }
            @Override
            public void onImageDownloaded(Bitmap image) {

            }
        });

        // Wait for the latch to be released before proceeding
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        lastName = findViewById(R.id.input_nom);
        firstName = findViewById(R.id.input_prenom);
        cin = findViewById(R.id.cin_patient);
        birthdate = findViewById(R.id.dataNaissance);
        address = findViewById(R.id.adresse_patient);
        hospital_spinner = findViewById(R.id.hospital_spinner);

        buttonFemale = findViewById(R.id.button_female);
        buttonMale = findViewById(R.id.button_male);

        buttonSuivant = (Button) findViewById(R.id.bouttonSuivant);
        buttonPrecedent = (Button) findViewById(R.id.precedentBouton);


        buttonFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedGender = "FEMALE";
            }
        });

        buttonMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedGender = "MALE";
            }
        });

        // Définir l'écouteur de clic sur l'birthdate
        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        List<String> optionValues = new ArrayList<>(HSP.keySet());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, optionValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        hospital_spinner.setAdapter(adapter);

        hospital_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = optionValues.get(position);
                selectedValue = HSP.get(selectedItem);
                Toast.makeText(getApplicationContext(), "Hopital sélectionné : " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the event when nothing is selected
                hospital_spinner.setBackground(ContextCompat.getDrawable(SignUpPatient.this, R.drawable.edittext_border));
                Toast.makeText(SignUpPatient.this, "Le champ hospital est requis !", Toast.LENGTH_SHORT).show();
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

                // Extraire les valeurs saisies par le patient dans les champs de saisie
                String nom = lastName.getText().toString();
                String prenom = firstName.getText().toString();
                String cinPatient = cin.getText().toString();
                String dte = birthdate.getText().toString();
                String adressePatient = address.getText().toString();
                //String hsp = hospital.getText().toString();


                // naviguer vers l'activite suivante et envoyer les données collecter de cette activite
                reinitialize(firstName,lastName,cin,birthdate, address);
                if(selectedGender.equals("")){
                    Toast.makeText(SignUpPatient.this, "Le champ civilité  est requis ! ", Toast.LENGTH_SHORT).show();
                }else if(nom.equals("")){
                    lastName.setBackground(ContextCompat.getDrawable(SignUpPatient.this, R.drawable.edittext_border));
                    Toast.makeText(SignUpPatient.this, "Le champ non est requis ! ", Toast.LENGTH_SHORT).show();
                }else if(prenom.equals("")){
                    firstName.setBackground(ContextCompat.getDrawable(SignUpPatient.this, R.drawable.edittext_border));
                    Toast.makeText(SignUpPatient.this, "Le champ prénom est requis ! ", Toast.LENGTH_SHORT).show();
                }else if(cinPatient.equals("")){
                    cin.setBackground(ContextCompat.getDrawable(SignUpPatient.this, R.drawable.edittext_border));
                    Toast.makeText(SignUpPatient.this, "Le champ cin est requis !", Toast.LENGTH_SHORT).show();
                }else if(dte.equals("")){
                    birthdate.setBackground(ContextCompat.getDrawable(SignUpPatient.this, R.drawable.edittext_border));
                    Toast.makeText(SignUpPatient.this, "Le champ date de naissance est requis !", Toast.LENGTH_SHORT).show();
                }else if(adressePatient.equals("")){
                    address.setBackground(ContextCompat.getDrawable(SignUpPatient.this, R.drawable.edittext_border));
                    Toast.makeText(SignUpPatient.this, "Le champ adresse est requis !", Toast.LENGTH_SHORT).show();
                }else {
                    patient = new Patient();
                    patient.setLastName(nom);
                    patient.setFirstName(prenom);
                    patient.setGender(selectedGender);
                    patient.setCin(cinPatient);
                    patient.setBirthdate(dte);
                    patient.setAddress(adressePatient);

                    Intent intent= new Intent(SignUpPatient.this, SignUpPatientNext.class);
                    intent.putExtra("patient", patient);
                    intent.putExtra("id", selectedValue);
                    startActivity(intent);

                    /*String cinPath = getIntent().getStringExtra("cinPath");
                    intent.putExtra("cinPath", cinPath);
                    startActivity(intent);*/
                    //Toast.makeText(SignUpPatient.this, ""+patient, Toast.LENGTH_SHORT).show();
                }
                //startActivity(new Intent(SignUpPatient.this,SignUpPatientNext.class));
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
                        //String date = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        String date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        birthdate.setText(date);
                    }
                }, year, month, dayOfMonth);

        // Afficher le DatePickerDialog
        datePickerDialog.show();
    }

    public Patient getEleveur(){
        Intent intent = getIntent();
        Patient patient = (Patient) intent.getSerializableExtra("patient");
        return patient;
    }
   public void reinitialize(EditText firstName,
                            EditText lastName,
                            EditText cin,
                            EditText birthdate,
                            EditText address){
       firstName.setBackground(ContextCompat.getDrawable(SignUpPatient.this, R.drawable.input_name));
       lastName.setBackground(ContextCompat.getDrawable(SignUpPatient.this, R.drawable.input_name));
       cin.setBackground(ContextCompat.getDrawable(SignUpPatient.this, R.drawable.input_name));
       birthdate.setBackground(ContextCompat.getDrawable(SignUpPatient.this, R.drawable.input_name));
       address.setBackground(ContextCompat.getDrawable(SignUpPatient.this, R.drawable.input_name));
   }

}