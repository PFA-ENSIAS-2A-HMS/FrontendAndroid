package com.example.androidprojet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidprojet.R;
import com.example.androidprojet.database.DatabaseHelper;
import com.example.androidprojet.model.User;

import java.util.ArrayList;

public class NotifFragment extends Fragment {
    private DatabaseHelper databaseHelper;
    private User user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(getContext());
        user = databaseHelper.getUser();
        if(user.getRole().equals("breeder")){
           // return inflater.inflate(R.layout.fragment_comite, container, false);

            View rootView = inflater.inflate(R.layout.fragment_comite, container, false);
            ImageButton galleryButton = rootView.findViewById(R.id.cameraButton);
            ImageButton videoButton = rootView.findViewById(R.id.videoButton);
            galleryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Code pour lancer l'activité de la caméra du téléphone
                    Intent cameraIntent=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(cameraIntent);
                }
            });
            videoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Code pour lancer l'activité d'enregistrement vidéo
                    Intent videoIntent=new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
                    startActivity(videoIntent);
                }
            });
            return rootView;

        }else{

            return inflater.inflate(R.layout.fragment_demandes, container, false);
        }
    }
}

