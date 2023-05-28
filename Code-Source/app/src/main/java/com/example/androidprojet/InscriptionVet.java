package com.example.androidprojet;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.androidprojet.model.Profil;
import com.example.androidprojet.network.ApiConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class InscriptionVet extends AppCompatActivity {
    private Profil veterinaire;
    private Button singInButton;
    private Button enregistrerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription_vet);

        veterinaire = getVeterinaire();
        EditText name = findViewById(R.id.name);
        EditText phoneNumber = findViewById(R.id.phoneNumber);
        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.password);
        EditText passwordConfirmation = findViewById(R.id.passwordConfirmation);

        enregistrerButton = (Button)findViewById(R.id.enregistrerButton);
        singInButton=(Button) findViewById(R.id.signInButton);

        enregistrerButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String nameComplet = name.getText().toString();
                String telephone = phoneNumber.getText().toString();
                String emailVet = email.getText().toString();
                String pwd = password.getText().toString();
                String pwdConf = passwordConfirmation.getText().toString();
                reinitialize(name, phoneNumber, email, password, passwordConfirmation);
                if(nameComplet.equals("")){
                    Toast.makeText(InscriptionVet.this, "the field name  is required ! ", Toast.LENGTH_SHORT).show();
                    name.setBackground(ContextCompat.getDrawable(InscriptionVet.this, R.drawable.edittext_border));
                }else if(telephone.equals("")){
                    Toast.makeText(InscriptionVet.this, "the field phone number is required ! ", Toast.LENGTH_SHORT).show();
                    phoneNumber.setBackground(ContextCompat.getDrawable(InscriptionVet.this, R.drawable.edittext_border));
                }else if (!isValidPhoneNumber(telephone)) {
                    Toast.makeText(InscriptionVet.this, "Please enter a valid phone number!", Toast.LENGTH_SHORT).show();
                    phoneNumber.setBackground(ContextCompat.getDrawable(InscriptionVet.this, R.drawable.edittext_border));
                }else if(emailVet.equals("")){
                    Toast.makeText(InscriptionVet.this, "the field email is required ! ", Toast.LENGTH_SHORT).show();
                    email.setBackground(ContextCompat.getDrawable(InscriptionVet.this, R.drawable.edittext_border));
                }else if (!isValidEmail(emailVet)) {
                    Toast.makeText(InscriptionVet.this, "Please enter a valid email address!", Toast.LENGTH_SHORT).show();
                    email.setBackground(ContextCompat.getDrawable(InscriptionVet.this, R.drawable.edittext_border));
                }else if(pwd.equals("")){
                    Toast.makeText(InscriptionVet.this, "the field password  is required ! ", Toast.LENGTH_SHORT).show();
                    password.setBackground(ContextCompat.getDrawable(InscriptionVet.this, R.drawable.edittext_border));
                }else if(pwdConf.equals("")){
                    Toast.makeText(InscriptionVet.this, "the field password Confirmation is required ! ", Toast.LENGTH_SHORT).show();
                    passwordConfirmation.setBackground(ContextCompat.getDrawable(InscriptionVet.this, R.drawable.edittext_border));
                }else if(!pwd.equals(pwdConf)){
                    Toast.makeText(InscriptionVet.this, "The password and the confirmation do not match !", Toast.LENGTH_SHORT).show();
                    passwordConfirmation.setBackground(ContextCompat.getDrawable(InscriptionVet.this, R.drawable.edittext_border));
                }else {
                    veterinaire.setName(nameComplet);
                    veterinaire.setEmail(emailVet);
                    veterinaire.setPhoneNumber(telephone);
                    veterinaire.setPassword(pwd);
                    veterinaire.setRole("VETERINAIRE");
                    String apiUrl = ApiConnection.URL+"/api/v1/profiles/register";
                    String requestBody = toJSON();
                    ApiConnection apiConnection = new ApiConnection();

                    ProgressDialog progressDialog = new ProgressDialog(InscriptionVet.this);
                    progressDialog.setMessage("création en cours...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    apiConnection.postToApi(apiUrl, requestBody, new ApiConnection.Callback() {
                        @Override
                        public void onResponse(int Code,String response) {
                            if(Code==201){
                                progressDialog.dismiss();
                                Intent intent = new Intent(InscriptionVet.this, SignInVet.class);
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
                }
            }
        });
        singInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(InscriptionVet.this,SignInVet.class);
                startActivity(intent);
            }
        });
    }

    public void showDialogBox(String message) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Erreur")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Compte")
                .setMessage("Votre compte a été créé avec succès!");
        builder.setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(InscriptionVet.this, SignInVet.class));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public Profil getVeterinaire()
    {
        Intent intent = getIntent();
        Profil receivedVeterinaire = (Profil) intent.getSerializableExtra("VETERINAIRE_OBJECT");
        if (receivedVeterinaire == null) {
            System.out.println("Error : Object doen't 'Veterinaire'\n");
        }
        return receivedVeterinaire;
    }

    public void reinitialize(EditText inputNomComplet,EditText telephone,
                             EditText emailVet,EditText pwd,
                             EditText pwdConf){
        inputNomComplet.setBackground(ContextCompat.getDrawable(InscriptionVet.this, R.drawable.input_name));
        telephone.setBackground(ContextCompat.getDrawable(InscriptionVet.this, R.drawable.input_name));
        emailVet.setBackground(ContextCompat.getDrawable(InscriptionVet.this, R.drawable.input_name));
        pwd.setBackground(ContextCompat.getDrawable(InscriptionVet.this, R.drawable.input_name));
        pwdConf.setBackground(ContextCompat.getDrawable(InscriptionVet.this, R.drawable.input_name));
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneNumberPattern = "0\\d{9}";
        return phoneNumber.matches(phoneNumberPattern);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String toJSON() {
        JSONObject json = new JSONObject();

        try {
            json.put("name", veterinaire.getName());
            json.put("phoneNumber", veterinaire.getPhoneNumber());
            json.put("email", veterinaire.getEmail());
            json.put("password", veterinaire.getPassword());
            json.put("role", veterinaire.getRole());


            /*byte[] byteArray = getIntent().getByteArrayExtra("bitmap");
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);


            byte[] byteArrayCin = getIntent().getByteArrayExtra("bitmapcin");
            bitmapCin = BitmapFactory.decodeByteArray(byteArrayCin, 0, byteArrayCin.length);*/

            //String imagePath = eleveur.getProfile();
            //Toast.makeText(this, ""+imagePath, Toast.LENGTH_SHORT).show();
            //Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

            /*if (bitmap != null) {

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                json.put("profile", base64Image);

            }

            if (bitmapCin != null) {
                Toast.makeText(this, "Yes", Toast.LENGTH_SHORT).show();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmapCin.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                json.put("cin_path", base64Image);
                Toast.makeText(this, ""+base64Image.substring(0,40), Toast.LENGTH_SHORT).show();
            }


            json.put("password", eleveur.getPassword());*/


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }
}