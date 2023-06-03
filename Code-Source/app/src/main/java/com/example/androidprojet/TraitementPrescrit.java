package com.example.androidprojet;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.androidprojet.model.DataPrescribedTreatment;
import com.example.androidprojet.network.ApiConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class TraitementPrescrit extends AppCompatActivity {

    private DataPrescribedTreatment dataPrescribedTreatment;

    private EditText dateConsultation;
    private EditText diagnostic;
    private EditText medicaments;
    private EditText posologie;
    private EditText dureeTraitement;
    private EditText instructionSpeciale;
    private Button btnEnvoiTraitement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traitement_prescrit);

        dateConsultation = findViewById(R.id.dateConsultation);
        diagnostic = findViewById(R.id.diagnostic);
        medicaments = findViewById(R.id.medicaments);
        posologie = findViewById(R.id.posologie);
        dureeTraitement = findViewById(R.id.dureeTraitement);
        instructionSpeciale = findViewById(R.id.instructionSpeciale);
        btnEnvoiTraitement = findViewById(R.id.btnEnvoiTraitement);

        // Définir l'écouteur de clic sur la date de consultation
        dateConsultation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        btnEnvoiTraitement.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String dateC = dateConsultation.getText().toString();
                String dgc = diagnostic.getText().toString();
                String mdcs = medicaments.getText().toString();
                String pslg = posologie.getText().toString();
                String duree = dureeTraitement.getText().toString();
                String instruction = instructionSpeciale.getText().toString();
                reinitialize(dateConsultation, diagnostic, medicaments, posologie, dureeTraitement, instructionSpeciale);
                if(dateC.equals("")) {
                    dateConsultation.setBackground(ContextCompat.getDrawable(TraitementPrescrit.this, R.drawable.edittext_border));
                    Toast.makeText(TraitementPrescrit.this, "La date de consultation est requis !", Toast.LENGTH_SHORT).show();
                }else if(dgc.equals("")) {
                    diagnostic.setBackground(ContextCompat.getDrawable(TraitementPrescrit.this, R.drawable.edittext_border));
                    Toast.makeText(TraitementPrescrit.this, "Le diagnostic est requis !", Toast.LENGTH_SHORT).show();
                }else if(mdcs.equals("")) {
                    medicaments.setBackground(ContextCompat.getDrawable(TraitementPrescrit.this, R.drawable.edittext_border));
                    Toast.makeText(TraitementPrescrit.this, "Le champ des médicaments est requis !", Toast.LENGTH_SHORT).show();
                }else if(pslg.equals("")) {
                    posologie.setBackground(ContextCompat.getDrawable(TraitementPrescrit.this, R.drawable.edittext_border));
                    Toast.makeText(TraitementPrescrit.this, "La posologie est requis !", Toast.LENGTH_SHORT).show();
                }else if(duree.equals("")) {
                    dureeTraitement.setBackground(ContextCompat.getDrawable(TraitementPrescrit.this, R.drawable.edittext_border));
                    Toast.makeText(TraitementPrescrit.this, "La durée de traitement est requis !", Toast.LENGTH_SHORT).show();
                }else {
                    dataPrescribedTreatment = new DataPrescribedTreatment();
                    dataPrescribedTreatment.setDate(dateC);
                    dataPrescribedTreatment.setDiagnostic(dgc);
                    dataPrescribedTreatment.setMedicaments(mdcs);
                    dataPrescribedTreatment.setPosologie(pslg);
                    dataPrescribedTreatment.setDuree(duree);
                    dataPrescribedTreatment.setInstructionSpeciales(instruction);

                    String apiUrl = ApiConnection.URL+"/api/v1/patients/";
                    String requestBody = toJSON();

                    ApiConnection apiConnection = new ApiConnection();

                    ProgressDialog progressDialog = new ProgressDialog(TraitementPrescrit.this);
                    progressDialog.setMessage("Enregistrement en cours...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    apiConnection.postToApi(apiUrl, requestBody, new ApiConnection.Callback() {
                        @Override
                        public void onResponse(int Code,String response) {

                            if(Code==201){
                                progressDialog.dismiss();
                            }else{
                                progressDialog.dismiss();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        final String errorMessage = "Une erreur est survenue !";
                                        showDialogBox(errorMessage);
                                    }
                                });
                            }

                        }
                        @Override
                        public void onError(int Code,String error) {
                            progressDialog.dismiss();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    final String errorMessage = "Une erreur est survenue !";
                                    showDialogBox(errorMessage);
                                }

                            });
                        }

                        @Override
                        public void onImageDownloaded(Bitmap image) {

                        }

                    });

                }
            }
        });
    }
    public void reinitialize(EditText dateConsultation,
                             EditText diagnostic,
                             EditText medicaments,
                             EditText posologie,
                             EditText dureeTraitement,
                             EditText instructionSpeciale){
        dateConsultation.setBackground(ContextCompat.getDrawable(TraitementPrescrit.this, R.drawable.input_name));
        diagnostic.setBackground(ContextCompat.getDrawable(TraitementPrescrit.this, R.drawable.input_name));
        medicaments.setBackground(ContextCompat.getDrawable(TraitementPrescrit.this, R.drawable.input_name));
        posologie.setBackground(ContextCompat.getDrawable(TraitementPrescrit.this, R.drawable.input_name));
        dureeTraitement.setBackground(ContextCompat.getDrawable(TraitementPrescrit.this, R.drawable.input_name));
        instructionSpeciale.setBackground(ContextCompat.getDrawable(TraitementPrescrit.this, R.drawable.input_name));
    }

    private void showDatePicker() {
        // Obtenir la date actuelle
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Créer une instance de DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(TraitementPrescrit.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Mettre à jour l'EditText avec la date sélectionnée
                        String date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        dateConsultation.setText(date);
                    }
                }, year, month, dayOfMonth);

        // Afficher le DatePickerDialog
        datePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String toJSON() {
        // Création d'un objet JSON
        JSONObject json = new JSONObject();
        try {
            /*json.put("firstName", dataPrescribedTreatment.getDate());
            json.put("phoneNumber", patient.getPhoneNumber());
            json.put("email", "");
            json.put("lastName", patient.getLastName());
            json.put("date_of_birth", patient.getBirthdate());
            json.put("image_url", "https://example.com/image.jpg");
            json.put("role", "PATIENT");
            json.put("password", patient.getPassword());
            json.put("gender", patient.getGender());
            json.put("cin", patient.getCin());
            json.put("address", patient.getAddress());
            json.put("emergencyContact", patient.getPhoneNumber());
            json.put("MedicalHistory", "aucune pour le moment");
            json.put("status", "PENDING");*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public void showDialogBox(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Erreur")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}