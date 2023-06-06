package com.example.androidprojet.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.androidprojet.R;
import com.example.androidprojet.database.DatabaseHelper;
import com.example.androidprojet.enums.StatusDataBiometric;
import com.example.androidprojet.model.DataBiometric;
import com.example.androidprojet.model.User;
import com.example.androidprojet.network.ApiConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class DataBiometricFragment extends Fragment {
    private DataBiometric dataBiometric;
    private DatabaseHelper databaseHelper;
    private User user;
    private EditText poids_patient;
    private EditText taille_patient;
    private EditText temperature_patient;
    private EditText tension_patient;
    private EditText pouls_patient;
    private EditText glycemie_patient;
    private TextView textView;
    private Button bouttonEnvoi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(getContext());
        user = databaseHelper.getUser();

        View rootView = inflater.inflate(R.layout.fragment_data_biometric, container, false);

        textView = rootView.findViewById(R.id.textView);

        // button d'envoi des donnees biomethrique
        bouttonEnvoi = rootView.findViewById(R.id.bouttonEnvoi);

        if(user.getDataBiometric() == StatusDataBiometric.SUBMITTED){
            textView.setText("Vos données biométriques ont déjà été envoyées. Si vous avez d'autres modifications, veuillez les enregistrer.");
            bouttonEnvoi.setText("Enregistrer");
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        poids_patient = view.findViewById(R.id.poids_patient);
        taille_patient = view.findViewById(R.id.taille_patient);
        temperature_patient = view.findViewById(R.id.temperature_patient);
        tension_patient = view.findViewById(R.id.tension_patient);
        pouls_patient = view.findViewById(R.id.pouls_patient);
        glycemie_patient = view.findViewById(R.id.glycemie_patient);

        bouttonEnvoi.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String poidsPString = poids_patient.getText().toString();

                String taillePString = taille_patient.getText().toString();
                String temperaturePString = temperature_patient.getText().toString();

                String tensoinP = tension_patient.getText().toString();

                String poulsPString = pouls_patient.getText().toString();

                String glycemie = glycemie_patient.getText().toString();

                reinitialize(poids_patient, taille_patient, temperature_patient, tension_patient, pouls_patient, glycemie_patient);
                if(poidsPString.equals("")) {
                    Toast.makeText(getContext(), "Le champ poids est requis !", Toast.LENGTH_SHORT).show();
                    poids_patient.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edittext_border));
                }else if(taillePString.equals("")) {
                    Toast.makeText(getContext(), "Le champ taille est requis !", Toast.LENGTH_SHORT).show();
                    taille_patient.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edittext_border));
                }else if(temperaturePString.equals("")) {
                    Toast.makeText(getContext(), "Le champ température est requis !", Toast.LENGTH_SHORT).show();
                    temperature_patient.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edittext_border));
                }else if(tensoinP.equals("")) {
                    Toast.makeText(getContext(), "Le champ tension est requis !", Toast.LENGTH_SHORT).show();
                    tension_patient.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edittext_border));
                }else if(poulsPString.equals("")) {
                    Toast.makeText(getContext(), "Le champ pouls est requis !", Toast.LENGTH_SHORT).show();
                    pouls_patient.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edittext_border));
                }else if(glycemie.equals("")) {
                    Toast.makeText(getContext(), "Le champ glycemie est requis !", Toast.LENGTH_SHORT).show();
                    glycemie_patient.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.edittext_border));
                }else {
                    ApiConnection apiConnection = new ApiConnection();

                    Double poidsP = Double.parseDouble(poidsPString);
                    Double tailleP = Double.parseDouble(taillePString);
                    Double temperatureP = Double.parseDouble(temperaturePString);
                    Double poulsP = Double.parseDouble(poulsPString);
                    // Instancie le model DataBiometric
                    dataBiometric = new DataBiometric();
                    dataBiometric.setPoids(poidsP);
                    dataBiometric.setTaille(tailleP);
                    dataBiometric.setTemperature(temperatureP);
                    dataBiometric.setTension(tensoinP);
                    dataBiometric.setPouls(poulsP);
                    dataBiometric.setGlycemie(glycemie);

                    String apiUrl = ApiConnection.URL+"/api/v1/biometrics/"+user.getLogin();
                    String requestBody = toJSON();
                    //Toast.makeText(getActivity(), requestBody, Toast.LENGTH_SHORT).show();

                    ProgressDialog progressDialog = new ProgressDialog(getActivity());

                    if(user.getDataBiometric() == StatusDataBiometric.NOT_SUBMITTED) {
                        progressDialog.setMessage("Envoi en cours...");
                    }else {
                        progressDialog.setMessage("Enregistrement en cours...");
                    }
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    if(user.getDataBiometric() == StatusDataBiometric.NOT_SUBMITTED) {
                        apiConnection.postToApi(apiUrl, requestBody, user.getToken(), new ApiConnection.Callback() {
                            @Override
                            public void onResponse(int Code,String response) {

                                if(Code==201){
                                    databaseHelper.updateUserByStatus(user.getLogin(), StatusDataBiometric.SUBMITTED);
                                    Thread thread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // Attente de 2 secondes (2000 millisecondes)
                                                Thread.sleep(2000);
                                                // Instruction à exécuter après l'attente
                                                progressDialog.dismiss();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    thread.start();
                                }else{
                                    progressDialog.dismiss();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            final String errorMessage = "Une erreur est survenue !";
                                            showDialogBox(errorMessage);
                                        }
                                    }).start();
                                }
                            }
                            @Override
                            public void onError(int Code,String error) {
                                progressDialog.dismiss();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        final String errorMessage = "Une erreur est survenue !";
                                        showDialogBox(errorMessage);
                                    }
                                }).start();
                            }

                            @Override
                            public void onImageDownloaded(Bitmap image) {

                            }

                        });
                    }else {
                        apiConnection.putToApi(apiUrl, requestBody, user.getToken(), new ApiConnection.Callback() {
                            @Override
                            public void onResponse(int Code,String response) {

                                if(Code==200){
                                    Thread thread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                // Attente de 2 secondes (2000 millisecondes)
                                                Thread.sleep(2000);
                                                // Instruction à exécuter après l'attente
                                                progressDialog.dismiss();

                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    thread.start();
                                }else{
                                    progressDialog.dismiss();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            final String errorMessage = "Une erreur est survenue !";
                                            showDialogBox(errorMessage);
                                        }
                                    }).start();

                                }

                            }
                            @Override
                            public void onError(int Code,String error) {
                                progressDialog.dismiss();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        final String errorMessage = "Une erreur est survenue !";
                                        showDialogBox(errorMessage);
                                    }
                                }).start();
                            }

                            @Override
                            public void onImageDownloaded(Bitmap image) {

                            }

                        });
                    }
                }
            }
        });
    }

    private void reinitialize(EditText poids_patient,
                              EditText taille_patient,
                              EditText temperature_patient,
                              EditText tension_patient,
                              EditText pouls_patient,
                              EditText glycemie_patient) {
        poids_patient.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.input_name));
        taille_patient.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.input_name));
        temperature_patient.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.input_name));
        tension_patient.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.input_name));
        pouls_patient.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.input_name));
        glycemie_patient.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.input_name));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String toJSON() {
        // Création d'un objet JSON
        JSONObject json = new JSONObject();
        try {
            json.put("weight", dataBiometric.getPoids());
            json.put("height", dataBiometric.getTaille());
            json.put("temperature", dataBiometric.getTemperature());
            json.put("bloodPressure", dataBiometric.getTension());
            json.put("pulse", dataBiometric.getPouls().toString());
            json.put("bloodSugarLevel", dataBiometric.getGlycemie());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public void showDialogBox(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

