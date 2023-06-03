package com.example.androidprojet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.androidprojet.database.DatabaseHelper;
import com.example.androidprojet.enums.StatusDataBiometric;
import com.example.androidprojet.fragments.HomeFragment;
import com.example.androidprojet.fragments.NotificationFragment;

import com.example.androidprojet.fragments.DataBiometricFragment;
import com.example.androidprojet.fragments.ProfilFragment;
import com.example.androidprojet.model.User;
import com.example.androidprojet.network.ApiConnection;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;

public class InsideAppPatient extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;//
    private String login;
    private String password;
    private String role;
    private String token;
    private StatusDataBiometric statusDataBiometric;
    private DatabaseHelper databaseHelper;
    //boolean isAuthenticated = false;
    private String isAuthenticated = "";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_app_patient);

        recupererData();

        bottomNavigationView = findViewById(R.id.btn_navigation);//
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);//


        // Ajouter le fragment Home par défaut
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_patient,
                new HomeFragment()).commit();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void recupererData(){
        Intent intent = getIntent();
        if (intent != null) {
            login = intent.getStringExtra("login");
            //Toast.makeText(this, ""+login, Toast.LENGTH_SHORT).show();
            password = intent.getStringExtra("password");
            role = intent.getStringExtra("role");
            statusDataBiometric = StatusDataBiometric.NOT_SUBMITTED;
            /*if(intent.hasExtra("isAthenticated")){
                isAuthenticated = intent.getStringExtra("isAthenticated");
            }
            
            databaseHelper = new DatabaseHelper(this);
            if (login != null && password != null && role != null && !isAuthenticated.equals("true")){
                login(new User(login,password,role,""));
            }*/
            databaseHelper = new DatabaseHelper(this);
            if (login != null && password != null && role != null){
               login(new User(login,password,role,"", StatusDataBiometric.NOT_SUBMITTED));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void login(User user){
        /*String apiUrl = "http://100.76.108.249:8000/api/v1/access-tokens";
        String requestBody = toJSON(user);
        ApiConnection apiConnection = new ApiConnection();
        ProgressDialog progressDialog = new ProgressDialog(InsideAppPatient.this);
        progressDialog.setMessage("Chargement des données en cours...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        CountDownLatch latch = new CountDownLatch(1);
        apiConnection.postToApi(apiUrl, requestBody, new ApiConnection.Callback() {
            @Override
            public void onResponse(int Code,String response) {
                if(Code==201){
                    progressDialog.dismiss();
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
                    if (jsonObject.has("token")) {
                        token = jsonObject.get("token").getAsString();
                    }
                    latch.countDown();
                    Toast.makeText(InsideAppPatient.this, "user is added in SQLite Database successfully (:", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.dismiss();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final String errorMessage = "Erreur interne du serveur";
                        }
                    });
                    latch.countDown();
                }

            }
            @Override
            public void onError(int Code,String error) {
                progressDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final String errorMessage = "Une erreur est survenue";
                    }
                });
            }

            @Override
            public void onImageDownloaded(Bitmap image) {

            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        databaseHelper.addUser(login, password, role,token, statusDataBiometric);
        //Toast.makeText(InsideAppPatient.this, "user is added in SQLite Database successfully (:", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String toJSON(User user) {
        JSONObject json = new JSONObject();
        try {
            json.put("login", user.getLogin());
            json.put("password",user.getPassword());
            json.put("role","patient");
        }catch (JSONException e) {
        e.printStackTrace();
        }
        return json.toString();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_comite:
                            selectedFragment = new DataBiometricFragment();
                            break;
                        case R.id.nav_search:
                            selectedFragment = new NotificationFragment();
                            break;
                        case R.id.nav_profil:
                            selectedFragment = new ProfilFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_patient,
                            selectedFragment).commit();

                    return true;
                }
            };
}