package com.example.androidprojet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidprojet.R;
import com.example.androidprojet.adapter.DossierMedicalAdapter;
import com.example.androidprojet.adapter.NotificationAdapter;
import com.example.androidprojet.model.DossierMedical;
import com.example.androidprojet.model.Notification;

import java.util.ArrayList;
import java.util.Arrays;

public class NotificationFragment extends Fragment {

    private final ArrayList<Notification> notifications = new ArrayList<>(Arrays.asList(
            new Notification("Mr. Kabouri Mouad", "Veuillez régler votre facture en attente dès que possible pour éviter tout retard ou désagrément.", "Le 20/08/2023",R.drawable.a),
            new Notification("Mme. Ilham berrada", "Veuillez régler votre facture en attente dès que possible pour éviter tout retard ou désagrément.", "Il y a 2 heures", R.drawable.anonyme),
            new Notification("Mr. Baha bammou", "Veuillez régler votre facture en attente dès que possible pour éviter tout retard ou désagrément.", "Le 20/08/2023", R.drawable.a),
            new Notification("Mr. Oussalh Ahmed", "Veuillez régler votre facture en attente dès que possible pour éviter tout retard ou désagrément.", "Il y a un jour", R.drawable.a),
            new Notification("Mme. Joun Ddi", "Veuillez régler votre facture en attente dès que possible pour éviter tout retard ou désagrément.", "Le 20/08/2023", R.drawable.anonyme),
            new Notification("Mr. Ounir Said", "Veuillez régler votre facture en attente dès que possible pour éviter tout retard ou désagrément.", "Il y a 2 semaines", R.drawable.a),
            new Notification("Mr. Kabouri Mouad", "Veuillez régler votre facture en attente dès que possible pour éviter tout retard ou désagrément.", "Le 20/08/2023",R.drawable.a),
            new Notification("Mme. Ilham berrada", "Veuillez régler votre facture en attente dès que possible pour éviter tout retard ou désagrément.", "Il y a 2 heures", R.drawable.anonyme),
            new Notification("Mr. Baha bammou", "Veuillez régler votre facture en attente dès que possible pour éviter tout retard ou désagrément.", "Le 20/08/2023", R.drawable.a),
            new Notification("Mr. Oussalh Ahmed", "Veuillez régler votre facture en attente dès que possible pour éviter tout retard ou désagrément.", "Il y a un jour", R.drawable.a),
            new Notification("Mme. Joun Ddi", "Veuillez régler votre facture en attente dès que possible pour éviter tout retard ou désagrément.", "Le 20/08/2023", R.drawable.anonyme),
            new Notification("Mr. Ounir Said", "Veuillez régler votre facture en attente dès que possible pour éviter tout retard ou désagrément.", "Il y a 2 semaines", R.drawable.a)
    ));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = getActivity().findViewById(R.id.listNotification);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(new NotificationAdapter(getContext(), notifications));

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
