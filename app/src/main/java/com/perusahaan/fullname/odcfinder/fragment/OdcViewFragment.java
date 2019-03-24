package com.perusahaan.fullname.odcfinder.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.perusahaan.fullname.odcfinder.R;
import com.perusahaan.fullname.odcfinder.model.OdcModel;

import static android.content.Context.LOCATION_SERVICE;

public class OdcViewFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private MapView mapView;
    private OdcModel odcModel;

    public OdcViewFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_odc_view, container, false);

        MapsInitializer.initialize(getContext());

        odcModel = getArguments().getParcelable("odc_item");
        mapView = view.findViewById(R.id.mapViewOdc);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        final AppCompatActivity activity = (AppCompatActivity) getActivity();

        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(odcModel.getNamaOdc());
        }

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
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mapView != null)
            mapView.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        this.googleMap.setMyLocationEnabled(true);

        LatLng serangLatLng = new LatLng( odcModel.getLatitude(), odcModel.getLongitude());
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(serangLatLng, 15f));

        Marker marker = this.googleMap.addMarker(new MarkerOptions()
                .position(
                        new LatLng(odcModel.getLatitude(), odcModel.getLongitude()))
                .title(odcModel.getNamaOdc() + " - " + odcModel.getKapasitas()));
        marker.showInfoWindow();

    }

}
