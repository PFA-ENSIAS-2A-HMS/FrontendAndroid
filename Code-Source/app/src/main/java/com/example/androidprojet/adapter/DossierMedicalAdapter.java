package com.example.androidprojet.adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.*;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidprojet.R;
import com.example.androidprojet.model.DossierMedical;
import java.util.ArrayList;
import java.util.Arrays;

public class DossierMedicalAdapter extends RecyclerView.Adapter<DossierMedicalAdapter.MyViewHolder> {

    private  final ArrayList DossiersMedical;

    public DossierMedicalAdapter(FragmentActivity activity, ArrayList<DossierMedical> dossiersMedical) {
        this.DossiersMedical = dossiersMedical;
    }

    // retournele nb total de cellule que contiendra la liste
    @Override
    public int getItemCount() {
        return DossiersMedical.size();
    }

    //// inflates the row layout from xml when needed
    //crée la vu d'une cellule
    // parent pour créer la vu et int pour spécifier le type de la cellule si on a plusieurrs type (orgnaisation differts)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //pour créer un laouyt depuis un XML
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_list_rdvs, parent, false);
        return new MyViewHolder(view);
    }

    //appliquer Une donnée à une vue
    //// binds the data to the TextView... in each row
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DossierMedical dossier = (DossierMedical) DossiersMedical.get(position);
        holder.malade.setText(dossier.getMalade());
        holder.date.setText(dossier.getDate());
        holder.desription.setText(dossier.getDescription());
        holder.logo.setImageResource(dossier.getLogo());
    }

    //// stores and recycles views as they are scrolled off screen
    public class MyViewHolder extends RecyclerView.ViewHolder  {

        public final TextView malade;
        private final TextView desription;
        private final TextView date;
        private final ImageView logo;

        //  private Pair<String, String> currentPair;
        private DossierMedical currentDossier;

        public MyViewHolder(final View itemView) {
            super(itemView);
            malade = itemView.findViewById(R.id.malade);
            desription = itemView.findViewById(R.id.description);
            date =  itemView.findViewById(R.id.date);
            logo = itemView.findViewById(R.id.logo);

            //         itemView.setOnClickListener(this);
            //  item = (TextView) itemView.findViewById(R.id.row_item);

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

