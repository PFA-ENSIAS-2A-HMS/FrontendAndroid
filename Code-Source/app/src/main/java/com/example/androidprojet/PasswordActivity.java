package com.example.androidprojet;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.androidprojet.database.DatabaseHelper;
import com.example.androidprojet.enums.StatusDataBiometric;
import com.example.androidprojet.model.Patient;
import com.example.androidprojet.model.User;
import com.example.androidprojet.network.ApiConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class PasswordActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    public Button buttonEnregistrer;
    private EditText pwd_input ;
    private EditText pwd_confirmation_input;
    private Patient patient;
    private Bitmap bitmap;
    private  Bitmap bitmapCin;
    private User userPat;
    private int idHospital;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwdactivity);

        patient = getPatient();

        buttonEnregistrer = (Button) findViewById(R.id.enregistrerButton);
        pwd_input = findViewById(R.id.pwd_input);
        pwd_confirmation_input = findViewById(R.id.pwd_confirmation);

        //Récupérer l'objet Patient de l'intention
        //patient = (Patient) getIntent().getSerializableExtra("patient");
        //bitmap = getIntent().getParcelableExtra("bitmap");

        buttonEnregistrer.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String pwd = pwd_input.getText().toString();
                String pwd_confirmation  = pwd_confirmation_input.getText().toString();
                if(pwd.equals("")){
                    Toast.makeText(PasswordActivity.this, "Le champ mot de passe est requis !", Toast.LENGTH_SHORT).show();
                    pwd_input.setBackground(ContextCompat.getDrawable(PasswordActivity.this, R.drawable.edittext_border));
                }else if(pwd_confirmation.equals("")){
                    Toast.makeText(PasswordActivity.this, "Le champ de confirmation du mot de passe est requis !", Toast.LENGTH_SHORT).show();
                    pwd_confirmation_input.setBackground(ContextCompat.getDrawable(PasswordActivity.this, R.drawable.edittext_border));
                }else if(!pwd.equals(pwd_confirmation)){
                    Toast.makeText(PasswordActivity.this, "Le mot de passe et la confirmation ne correspondent pas !", Toast.LENGTH_SHORT).show();
                }else{
                    patient.setPassword(pwd);

                    String apiUrl = ApiConnection.URL+"/api/v1/auth/register/patient/"+idHospital;
                    String requestBody = toJSON();

                    ApiConnection apiConnection = new ApiConnection();

                    System.out.println("Patient: " +requestBody);

                    ProgressDialog progressDialog = new ProgressDialog(PasswordActivity.this);
                    progressDialog.setMessage("création en cours...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();


                    apiConnection.postToApi(apiUrl, requestBody, new ApiConnection.Callback() {
                        @Override
                        public void onResponse(int Code,String response) {
                            if(Code==200){
                                System.out.println("response : "+response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    System.out.println("jsonObject : "+jsonObject);
                                    int id = jsonObject.getInt("id");
                                    String accessToken = jsonObject.getString("accessToken");
                                    userPat = new User(Integer.toString(id), pwd, "patient", accessToken, StatusDataBiometric.NOT_SUBMITTED);
                                    databaseHelper = new DatabaseHelper(getApplicationContext());
                                    databaseHelper.addUser(userPat.getLogin(), userPat.getPassword(), userPat.getRole(),userPat.getToken(), userPat.getDataBiometric());
                                    Intent intent = new Intent(PasswordActivity.this, InsideAppPatient.class);
                                    intent.putExtra("patient", userPat);
                                    startActivity(intent);
                                    progressDialog.dismiss();
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                //startActivity(new Intent(PasswordActivity.this, SignInPatient.class));

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String toJSON() {
        // Création d'un objet JSON
        JSONObject json = new JSONObject();
        try {
            json.put("firstName", patient.getFirstName());
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
            json.put("status", "PENDING");


            /*String imagePath = getIntent().getStringExtra("profilePath");

            if (imagePath != null) {
                bitmap = BitmapFactory.decodeFile(imagePath);
            }
            String cinPath = getIntent().getStringExtra("cinPath");
            if(cinPath!=null){
                bitmapCin = BitmapFactory.decodeFile(cinPath);
            }

            int targetWidth = 500; // Largeur cible souhaitée
            int targetHeight = 500; // Hauteur cible souhaitée

            if (bitmap != null) {
                // Redimensionner le bitmap à la taille cible
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                json.put("profile", base64Image);
            }

            if (bitmapCin != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmapCin.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                json.put("cin_path", base64Image);

            }*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    private byte[] convertImageToByteArray(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public Patient getPatient(){
        Intent intent = getIntent();
        Patient patient = (Patient) intent.getSerializableExtra("patient");
        if (intent.hasExtra("id")) {
            idHospital = intent.getIntExtra("id", 1);
        }
        return patient;
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}