package com.example.androidprojet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidprojet.database.DatabaseHelper;
import com.example.androidprojet.model.User;

public class demandes extends Fragment {
    private DatabaseHelper databaseHelper;
    private User user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(getContext());
        user = databaseHelper.getUser();
        if(user.getRole().equals("breeder")){
            return inflater.inflate(R.layout.fragment_comite, container, false);
        }else{
            return inflater.inflate(R.layout.fragment_profil, container, false);
        }
    }
}
