package com.perusahaan.fullname.odcfinder.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import com.perusahaan.fullname.odcfinder.Utils.MyUtils;
import com.perusahaan.fullname.odcfinder.model.OdcModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;


public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private MapView mapView;
    private List<OdcModel> lists;
    private RequestQueue requestQueue;

    private static final String LOCATION_URL = "http://khotibul.herokuapp.com/odc";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        MapsInitializer.initialize(getContext());
        requestQueue = Volley.newRequestQueue(getActivity());

        mapView = view.findViewById(R.id.mapView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        final AppCompatActivity activity = (AppCompatActivity) getActivity();

        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle("Home");
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
        // this.googleMap.setMaxZoomPreference(15);

        // set default to kota serang
        LatLng serangLatLng = new LatLng(-6.1149f, 106.1502f);
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(serangLatLng, 15f));
//        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(serangLatLng));

        LocationManager service = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

        if (service != null) {
            String provider = service.getBestProvider(new Criteria(), false);
            android.location.Location location = service.getLastKnownLocation(provider);

            if (location != null) {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f));
                this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f));
            }
        }

        populateData();

    }

    private void populateData() {
        MyUtils.showSimpleProgressDialog(getActivity(), "Loading...", "Please wait...", true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, LOCATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        MyUtils.removeSimpleProgressDialog();

                        try {
                            lists = new LinkedList<>();
                            JSONArray responseJson = new JSONArray(response);
                            for (int i = 0; i < responseJson.length(); i++) {

                                JSONObject object = responseJson.getJSONObject(i);

                                final String name = object.getString("nama_odc");
                                final String kapasitas = object.getString("kapasitas");
                                final String datel = object.getString("datel");
                                final String witel = object.getString("witel");
                                final Double latitude = object.getDouble("latitude");
                                final Double longitude = object.getDouble("longitude");

                                Float fLatitude = Float.valueOf(String.valueOf(latitude));
                                Float fLongitude = Float.valueOf(String.valueOf(longitude));

                                OdcModel sampleObject = new OdcModel(name, kapasitas, datel, witel
                                        , fLatitude, fLongitude);

                                lists.add(sampleObject);
                            }

                            addMarkerToMap(lists);

                        } catch (JSONException e) {
                            Log.d("Response JSON Error", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        requestQueue.add(stringRequest);
    }

    private void addMarkerToMap(List<OdcModel> lists) {
        for (OdcModel odc : lists) {
            Marker marker = this.googleMap.addMarker(new MarkerOptions()
                    .position(
                            new LatLng(odc.getLatitude(), odc.getLongitude()))
                    .title(odc.getNamaOdc() + " - " + odc.getKapasitas()));
            marker.showInfoWindow();
        }
    }
}