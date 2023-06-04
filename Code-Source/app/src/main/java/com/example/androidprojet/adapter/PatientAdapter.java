package com.example.androidprojet.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidprojet.R;
import com.example.androidprojet.model.Patient;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {

    private ItemClick myitemClick;

    private ArrayList<Patient> patients;
    private Context context;

    public PatientAdapter(Context context, ArrayList<Patient> patients, ItemClick itemClick) {
        this.context = context;
        this.patients = patients;
        this.myitemClick = itemClick;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_patient, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        Patient patient = patients.get(position);
        final Bitmap[] bitmap = new Bitmap[1];

        final CountDownLatch latch = new CountDownLatch(1);

        holder.nomCompletPatient.setText(patient.getLastName() + " " + patient.getFirstName());
        holder.phoneNumber.setText(patient.getPhoneNumber());

        holder.itemView.setOnClickListener(view -> {
            myitemClick.onItemClick(patient); // this will get the position of our item in RecyclerView
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = new URL(patient.getProfile()).openStream();
                    bitmap[0] = BitmapFactory.decodeStream(inputStream);
                    holder.img.setImageBitmap(bitmap[0]);
                    latch.countDown();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public class PatientViewHolder extends RecyclerView.ViewHolder{

        private TextView nomCompletPatient;
        private TextView phoneNumber;
        private ImageView img;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            nomCompletPatient = itemView.findViewById(R.id.nomCompletPatient);
            phoneNumber = itemView.findViewById(R.id.statutTraitement);
            img = itemView.findViewById(R.id.image_patient);

            //itemView.setOnClickListener(this);
        }

        /*@Override
        public void onClick(View view) {
            Patient patient = patients.get(getAdapterPosition());
            /*new AlertDialog.Builder(context)
                    .setTitle(patient.getPhoneNumber())
                    .show();
        }*/
    }

    public interface ItemClick {
        void onItemClick(Patient patient);
    }
}
