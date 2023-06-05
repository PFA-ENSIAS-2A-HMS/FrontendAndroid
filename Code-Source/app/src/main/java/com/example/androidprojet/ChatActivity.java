package com.example.androidprojet;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidprojet.databinding.ActivityChatBinding;
import com.example.androidprojet.model.FireBaseUser;
import com.example.androidprojet.utilities.Constants;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private FireBaseUser receivedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadReceivedDetails();
    }

    private void loadReceivedDetails() {
        receivedUser = (FireBaseUser) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.textName.setText(receivedUser.name);
    }

    private void setListeners() {
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }
}