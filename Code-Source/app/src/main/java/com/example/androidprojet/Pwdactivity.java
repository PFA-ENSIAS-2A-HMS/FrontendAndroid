package com.example.androidprojet;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidprojet.model.Eleveur;
import com.example.androidprojet.network.ApiConnection;
import com.example.androidprojet.network.PostEleveur;
import com.example.androidprojet.retrofit.ApiService;
import com.example.androidprojet.retrofit.EleveurApi;
import com.example.androidprojet.retrofit.RetrofitService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.nio.file.Paths;

import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Pwdactivity extends AppCompatActivity {
    public Button buttonEnregistrer;
    private EditText pwd_input ;
    private EditText pwd_confirmation_input;
    private Eleveur eleveur;
    private Bitmap bitmap;
    TextView textView ;
    private  Bitmap bitmapCin;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwdactivity);
        eleveur = getEleveur();
        buttonEnregistrer = (Button) findViewById(R.id.enregistrerButton);
        pwd_input=findViewById(R.id.pwd_input);
        pwd_confirmation_input = findViewById(R.id.pwd_confirmation);
        // Récupérer l'objet Eleveur de l'intention
        eleveur = (Eleveur) getIntent().getSerializableExtra("eleveur");
        bitmap = getIntent().getParcelableExtra("bitmap");

        buttonEnregistrer.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String pwd = pwd_input.getText().toString();
                String pwd_confirmation  = pwd_confirmation_input.getText().toString();
                /*if(pwd.equals("")){
                    Toast.makeText(Pwdactivity.this, "the password field is required !", Toast.LENGTH_SHORT).show();
                    pwd_input.setBackground(ContextCompat.getDrawable(Pwdactivity.this, R.drawable.edittext_border));

                }else if(pwd_confirmation.equals("")){
                    Toast.makeText(Pwdactivity.this, "the password confirmation field is required !", Toast.LENGTH_SHORT).show();
                    pwd_confirmation_input.setBackground(ContextCompat.getDrawable(Pwdactivity.this, R.drawable.edittext_border));

                }else if(!pwd.equals(pwd_confirmation)){
                    Toast.makeText(Pwdactivity.this, "The password and the confirmation do not match.", Toast.LENGTH_SHORT).show();
                }else{
                    eleveur.setPassword(pwd);

                    String apiUrl = ApiConnection.URL+"/api/v1/breeders/";
                    String requestBody = toJSON();

                    ApiConnection apiConnection = new ApiConnection();

                    ProgressDialog progressDialog = new ProgressDialog(Pwdactivity.this);
                    progressDialog.setMessage("création en cours...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();


                    apiConnection.postToApi(apiUrl, requestBody, new ApiConnection.Callback() {
                        @Override
                        public void onResponse(int Code,String response) {
                            if(Code==201){
                                progressDialog.dismiss();
                                Intent intent = new Intent(Pwdactivity.this, Attachs1.class);
                                intent.putExtra("login",eleveur.getPhoneNumber());
                                intent.putExtra("password",eleveur.getPassword());
                                intent.putExtra("role","breeder");
                                startActivity(intent);
                            }else{
                                progressDialog.dismiss();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        final String errorMessage = "Erreur interne du serveur";
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
                                    final String errorMessage = "Une erreur est survenue";
                                    showDialogBox(errorMessage);
                                }

                            });
                        }

                        @Override
                        public void onImageDownloaded(Bitmap image) {

                        }

                    });

                }*/

                startActivity(new Intent(Pwdactivity.this, Attachs1.class));

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

    //@RequiresApi(api = Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String toJSON() {
        JSONObject json = new JSONObject();

        try {
            json.put("fullNameFr", eleveur.getFullNameFr());
            json.put("fullNameAr", eleveur.getFullNameAr());
            json.put("cin", eleveur.getCin());
            json.put("address", eleveur.getAddress());
            json.put("gender", eleveur.getGender());
            json.put("phoneNumber", eleveur.getPhoneNumber());

            String imagePath = getIntent().getStringExtra("profilePath");

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

            }


            json.put("password", eleveur.getPassword());


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

    public Eleveur getEleveur(){
        Intent intent = getIntent();
        Eleveur eleveur = (Eleveur) intent.getSerializableExtra("eleveur");
        return eleveur;
    }
}