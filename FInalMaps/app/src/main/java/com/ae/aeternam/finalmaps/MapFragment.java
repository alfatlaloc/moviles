package com.ae.aeternam.finalmaps;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment{

    private GoogleMap mapsAe;

    public MapFragment(){}

    public void updateLocation(double lat,double lon ,String Place){
        LatLng auxLatting = new LatLng(lat, lon);
        mapsAe.addMarker(new MarkerOptions().position(auxLatting).title("Marker in:" + Place ));
        mapsAe.moveCamera(CameraUpdateFactory.newLatLng(auxLatting));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        String [] values =
                {"Mohacs","Viena","Sebastopol","Ciudad de Mexico","Samarkanda","Baghdad","Malta","Tripoli","Armenia","Beijing"};

        Spinner spinner = (Spinner) view.findViewById(R.id.locationChangeSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                String item = adapterView.getItemAtPosition(position).toString();
                mapsAe.clear();
                switch(item){
                    case "Mohacs":
                        updateLocation(46.004337624284325, 18.681758035672217,"Mohacs");
                        break;
                    case "Viena":
                        updateLocation(48.277753807933706, 16.563385796859535,"Viena");
                        break;

                    case "Sebastopol":
                        updateLocation(44.80418932783559, 33.47044819322112,"Sebastopol");
                        break;

                    case "Ciudad de Mexico":
                        updateLocation(19.421605539612067, -99.1711208211607,"Ciudad de Mexico");
                        break;

                    case "Samarkanda":
                        updateLocation(39.63002822866296, 66.97268510365008,"Samarkanda");
                        break;

                    case "Baghdad":
                        updateLocation(33.41689210509363, 44.36960092222454,"Baghdad");
                        break;

                    case "Malta":
                        updateLocation(35.885645096850446, 14.43052464127729,"Malta");
                        break;

                    case "Tripoli":
                        updateLocation(32.88747654279352, 13.19245555286019,"Tripoli");
                        break;

                    case "Armenia":
                        updateLocation(40.4096497962128, 45.02731138620461,"Armenia");
                        break;

                    case "Beijing":
                        updateLocation(40.591384957605165, 116.2381179620914,"Beijing");
                        break;



                };

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        SupportMapFragment SPM = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        assert SPM != null;
        SPM.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mapsAe = googleMap;
                LatLng sydney = new LatLng(-34, 151);
                mapsAe.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                mapsAe.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            }
        });
        return view;
    }

}