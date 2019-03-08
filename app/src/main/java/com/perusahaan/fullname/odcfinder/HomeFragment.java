package com.perusahaan.fullname.odcfinder;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.perusahaan.fullname.odcfinder.model.LocationModel;

import java.util.LinkedList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;


public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        MapsInitializer.initialize(getContext());

        mapView = view.findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        this.googleMap.setMyLocationEnabled(true);


        LocationManager service = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        if (service != null) {
            String provider = service.getBestProvider(new Criteria(), false);
            android.location.Location location = service.getLastKnownLocation(provider);

            if(location != null) {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                this.googleMap.setMinZoomPreference(15);
                this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                this.googleMap.animateCamera(CameraUpdateFactory.newLatLng(userLocation));
            }
        }

        final List<LocationModel> locationModels = populateData();

        for (LocationModel loc : locationModels) {
            Marker marker = this.googleMap.addMarker(new MarkerOptions()
                    .position(
                            new LatLng(loc.getLatitude(), loc.getLongitude()))
                    .title(loc.getName()));
            marker.showInfoWindow();
        }


    }

    private List<LocationModel> populateData() {
        List<LocationModel> list = new LinkedList<>();

        LocationModel loc1 = new LocationModel("jakarta", -6.2293184f, 106.84418f);
        LocationModel loc2 = new LocationModel("bandung", -6.2254042f, 106.84774f);
        LocationModel loc3 = new LocationModel("merak", -6.2234042f, 106.845274f);
        LocationModel loc4 = new LocationModel("pandeglang", -6.2224042f, 106.8465274f);

        list.add(loc1);
        list.add(loc2);
        list.add(loc3);
        list.add(loc4);
        return list;
    }
}