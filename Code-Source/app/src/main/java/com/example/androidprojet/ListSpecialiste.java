package com.example.androidprojet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Toast;
import com.example.androidprojet.adapter.UserAdapter;
import com.example.androidprojet.model.FireBaseUser;

import java.util.ArrayList;
import java.util.Arrays;

public class ListSpecialiste extends AppCompatActivity {


    private final ArrayList<FireBaseUser> fireBaseUsers = new ArrayList<>(Arrays.asList(
            new FireBaseUser("Mouad Kabouri", "Kabmouad@gmail.com", "pass12", "KZIEU3920URHFEEDDED", R.drawable.anonyme),
            new FireBaseUser("Mouad Lakouz", "Kabmouad@gmail.com", "pass12", "KZIEU3920URHFEEDDED", R.drawable.anonyme),
            new FireBaseUser("Mouad Obaha", "Kabmouad@gmail.com", "pass12", "KZIEU3920URHFEEDDED", R.drawable.anonyme),
            new FireBaseUser("Mouad ohassan", "Kabmouad@gmail.com", "pass12", "KZIEU3920URHFEEDDED", R.drawable.anonyme)
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_activity);

        RecyclerView rv = findViewById(R.id.usersRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new UserAdapter(fireBaseUsers, new UserAdapter.ItemClickListener() {
            @Override
            public void onItemClick(FireBaseUser fireBaseUser) {
                showToast(fireBaseUser.name + "is clicked !");
            }
        }));
    }
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}