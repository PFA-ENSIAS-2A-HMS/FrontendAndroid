package com.example.androidprojet.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidprojet.R;
import com.example.androidprojet.TraitementPrescrit;
import com.example.androidprojet.adapter.UserAdapter;
import com.example.androidprojet.model.FireBaseUser;

import java.util.ArrayList;
import java.util.Arrays;

public class ListPatients extends Fragment {

    private TextView nomCompletPatient;

    private final ArrayList<FireBaseUser> fireBaseUsers = new ArrayList<>(Arrays.asList(
            new FireBaseUser("Mouad Kabouri", "06554028374", "pass12", "KZIEU3920URHFEEDDED", R.drawable.a),
            new FireBaseUser("Mouad Lakouz", "06554028374", "pass12", "KZIEU3920URHFEEDDED", R.drawable.anonyme),
            new FireBaseUser("Mouad Obaha", "06554028374", "pass12", "KZIEU3920URHFEEDDED", R.drawable.a),
            new FireBaseUser("Mouad ohassan", "06554028374", "pass12", "KZIEU3920URHFEEDDED", R.drawable.anonyme)
    ));

    public ListPatients() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_patients, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv = getActivity().findViewById(R.id.usersRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(new UserAdapter(fireBaseUsers, new UserAdapter.ItemClickListener() {
            @Override
            public void onItemClick(FireBaseUser fireBaseUser) {
                    startActivity(new Intent(getActivity(), TraitementPrescrit.class));
            }
        }));

    }

    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}