package com.example.androidprojet.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.*;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidprojet.R;
import com.example.androidprojet.model.DossierMedical;
import java.util.ArrayList;

public class DossierMedicalAdapter extends RecyclerView.Adapter<DossierMedicalAdapter.MyViewHolder> {
    Context context;
    private  final ArrayList DossiersMedical;

    public DossierMedicalAdapter(Context context, ArrayList<DossierMedical> dossiersMedical) {
        this.context = context;
        this.DossiersMedical = dossiersMedical;
    }

    @Override
    public int getItemCount() {
        return DossiersMedical.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_rdv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DossierMedical dossier = (DossierMedical) DossiersMedical.get(position);
        holder.malade.setText(dossier.getMalade());
        holder.docteur.setText(dossier.getDocteur());
        holder.date.setText(dossier.getDate());
        holder.desription.setText(dossier.getDescription());
        holder.logo.setImageResource(dossier.getLogo());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        public final TextView malade;
        public final TextView docteur;
        private final TextView desription;
        private final TextView date;
        private final ImageView logo;

        private DossierMedical currentDossier;

        public MyViewHolder(final View itemView) {
            super(itemView);
            malade = itemView.findViewById(R.id.malade);
            docteur = itemView.findViewById(R.id.docteur);
            desription = itemView.findViewById(R.id.description);
            date =  itemView.findViewById(R.id.date);
            logo = itemView.findViewById(R.id.logo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    currentDossier = (DossierMedical) DossiersMedical.get(getLayoutPosition());

                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle(currentDossier.getMalade())
                            .show();
                }
            });
        }
    }
}

