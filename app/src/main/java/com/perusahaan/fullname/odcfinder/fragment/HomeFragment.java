package com.perusahaan.fullname.odcfinder.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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
import com.perusahaan.fullname.odcfinder.model.LocationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;


public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private MapView mapView;
    private List<LocationModel> lists;

    private static final String LOCATION_URL = "https://app.fakejson.com/q/XTe9P4lk?token=O96n4fneUTZmwhtKuZUBpQ";

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
        if (mapView != null) {
            mapView.onDestroy();
        }
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
        // this.googleMap.setMaxZoomPreference(15);

        // set default to kota serang
        LatLng serangLatLng = new LatLng(-6.1149f, 106.1502f);
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(serangLatLng));
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLng(serangLatLng));

        LocationManager service = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

        if (service != null) {
            String provider = service.getBestProvider(new Criteria(), false);
            android.location.Location location = service.getLastKnownLocation(provider);

            if (location != null) {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                this.googleMap.animateCamera(CameraUpdateFactory.newLatLng(userLocation));
            }
        }

        populateData();

        if(lists != null) {

            for (LocationModel loc : lists) {
                Marker marker = this.googleMap.addMarker(new MarkerOptions()
                        .position(
                                new LatLng(loc.getLatitude(), loc.getLongitude()))
                        .title(loc.getName()));
                marker.showInfoWindow();
            }
        }

    }

    private void populateData() {
//
//        LocationModel loc1 = new LocationModel("jakarta", -6.2293184f, 106.84418f);
//        LocationModel loc2 = new LocationModel("bandung", -6.2254042f, 106.84774f);
//        LocationModel loc3 = new LocationModel("merak", -6.2234042f, 106.845274f);
//        LocationModel loc4 = new LocationModel("pandeglang", -6.2224042f, 106.8465274f);
//
//        lists.add(loc1);
//        lists.add(loc2);
//        lists.add(loc3);
//        lists.add(loc4);
//        return lists;

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

                                final String name = object.getString("name");
                                final Double latitude = object.getDouble("latitude");
                                final Double longitude = object.getDouble("longitude");

                                Float fLatitude = Float.valueOf(String.valueOf(latitude));
                                Float fLongitude = Float.valueOf(String.valueOf(longitude));

                                LocationModel sampleObject = new LocationModel(name, fLatitude, fLongitude);

                                lists.add(sampleObject);
                            }

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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}