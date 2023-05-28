package com.example.androidprojet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SmsVerify extends AppCompatActivity {
 public Button buttonSuivant ,buttonPrecedent;
    EditText et_otpNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_verify);
        buttonSuivant = (Button) findViewById(R.id.bouttonSuivant);
        buttonPrecedent = (Button) findViewById(R.id.precedentBouton);
        requestSMSPermission();
        et_otpNumber = findViewById(R.id.et_otpNumber);
        new OTP_Receiver().setEditText(et_otpNumber);
        buttonSuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(SmsVerify.this,Pwdactivity.class);
                startActivity(intent);
            }
        });
        buttonPrecedent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(SmsVerify.this,IdentificationSmsv2.class);
                startActivity(intent);
            }
        });
    }
    private void requestSMSPermission()
    {
        String permission = Manifest.permission.RECEIVE_SMS;

        int grant = ContextCompat.checkSelfPermission(this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED)
        {
            String[] permission_list = new String[1];
            permission_list[0] = permission;

            ActivityCompat.requestPermissions(this, permission_list,1);
        }
    }

}