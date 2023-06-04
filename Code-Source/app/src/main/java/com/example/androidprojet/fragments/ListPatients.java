package com.example.androidprojet.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidprojet.R;
import com.example.androidprojet.TraitementPrescrit;
import com.example.androidprojet.adapter.PatientAdapter;
import com.example.androidprojet.model.Patient;
import com.example.androidprojet.network.ApiConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ListPatients extends Fragment {
    private ArrayList<Patient> patients = new ArrayList<>();
    private ProgressDialog progressDialog;

    public ListPatients() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getPatients();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_patients, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Téléchargement des patients en cours...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RecyclerView rv = getActivity().findViewById(R.id.usersRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(new PatientAdapter(getContext(), patients, new PatientAdapter.ItemClick() {
            @Override
            public void onItemClick(Patient patient) {
                Intent it = new Intent(getContext(), TraitementPrescrit.class);
                it.putExtra("idPatient",patient.getPassword());
                startActivity(it);
            }
        }));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3500); // Attendre pendant 3 secondes (3000 millisecondes)
                    progressDialog.dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void getPatients() {

        // Create a CountDownLatch with initial count 1
        final CountDownLatch latch = new CountDownLatch(1);

        // Récupérer toutes les hôpitaux
        ApiConnection apiConnection = new ApiConnection();
        apiConnection.getFromApi(ApiConnection.URL+"/api/v1/appointments/doctor/2", new ApiConnection.Callback() {
            @Override
            public void onResponse(int code, String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    String firstName, lastName, phoneNumber, image_url;
                    int id;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        firstName = jsonObject.getString("firstName");
                        lastName = jsonObject.getString("lastName");
                        phoneNumber = jsonObject.getString("phoneNumber");
                        image_url = jsonObject.getString("image_url");
                        id = jsonObject.getInt("id");
                        Patient patient = new Patient(
                                firstName,
                                lastName,
                                phoneNumber,
                                ApiConnection.URL+"/api/v1/doctors/display/"+image_url,
                                Long.toString(id));

                        patients.add(patient);
                        //System.out.println("Patient: " + patient + "\n");
                    }
                    latch.countDown();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void onError(int code, String error) {
                System.out.println("Error, code :"+code);
                //latch.countDown();
            }

            @Override
            public void onImageDownloaded(Bitmap image) {
                // Ignore this for now, since it is not used in this code snippet
            }
        });

        // Wait for the latch to be released before proceeding
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
