package com.example.androidprojet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidprojet.R;
import com.example.androidprojet.adapter.DossierMedicalAdapter;
import com.example.androidprojet.model.DossierMedical;
import java.util.ArrayList;
import java.util.Arrays;

public class HomeFragment extends Fragment {
    private ImageButton imageButton;
    private TextView textView;

    private final ArrayList<DossierMedical> dossiersMedical = new ArrayList<>(Arrays.asList(
            new DossierMedical("Maladie 1", "20/08/2023", "Lorem ipsum dolor sit amet, consectetur adipiscing elit Lorem ipsum dolor sit amet, consectetur",R.drawable.maladie1),
            new DossierMedical("Maladie 2", "20/08/2023", "Lorem ipsum dolor sit amet, consectetur adipiscing elit Lorem ipsum dolor sit amet, consectetur", R.drawable.maladie2),
            new DossierMedical("Maladie 3", "20/08/2023", "Lorem ipsum dolor sit amet, consectetur adipiscing elit Lorem ipsum dolor sit amet, consectetur", R.drawable.maladie1)
    ));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_list_rdvs, container, false);

        RecyclerView rv = getActivity().findViewById(R.id.list);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(new DossierMedicalAdapter(getActivity(), dossiersMedical));

        return rootView;
    }


    /*@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

       //return inflater.inflate(R.layout.fragment_home, container, false);
        View rootview=inflater.inflate(R.layout.fragment_home, container, false);
        /*imageButton=rootview.findViewById(R.id.ivimage);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), fiche_details.class);
                startActivity(intent);
            }
        });
        return rootview;

    }*/


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
//
//        BottomNavigationView bottomNavigationView = rootView.findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.nav_home:
//                        // L'utilisateur a cliqué sur l'élément "Home"
//                        return true;
//                    case R.id.nav_my_activity:
//                        // L'utilisateur a cliqué sur l'élément "My Activity"
//                        Intent intent = new Intent(getActivity(), MyActivity.class);
//                        startActivity(intent);
//                        return true;
//                    case R.id.nav_profile:
//                        // L'utilisateur a cliqué sur l'élément "Profile"
//                        return true;
//                }
//                return false;
//            }
//        });
//
//        return rootView;
//    }

}