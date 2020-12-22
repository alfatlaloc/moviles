package com.ae.aeternam.finalmaps;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment {

    GoogleMap mapsAe;

    public void updateLocation(long lat, long lon ,String Place){
        LatLng auxLatting = new LatLng(lat, lon);
        mapsAe.addMarker(new MarkerOptions().position(auxLatting).title("Marker in:" + Place ));
        mapsAe.moveCamera(CameraUpdateFactory.newLatLng(auxLatting));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment SPM = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
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