package com.perusahaan.fullname.odcfinder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.perusahaan.fullname.odcfinder.model.OdcModel;

public class OdcViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private MapView mapView;
    private OdcModel odcModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odc_view);

        MapsInitializer.initialize(this);

        final Bundle extras = getIntent().getExtras();

        if(extras != null) {
            odcModel = extras.getParcelable("odc_item");
        }

        mapView = findViewById(R.id.mapViewOdc);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Toolbar toolbar = findViewById(R.id.toolbar_back);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(odcModel.getNamaOdc());
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        this.onBackPressed();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        this.googleMap.setMyLocationEnabled(true);

        LatLng odcLatLong = new LatLng( odcModel.getLatitude(), odcModel.getLongitude());
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(odcLatLong, 20f));

        Marker marker = this.googleMap.addMarker(new MarkerOptions()
                .position(
                        new LatLng(odcModel.getLatitude(), odcModel.getLongitude()))
                .title(odcModel.getNamaOdc() + " - " + odcModel.getKapasitas()));
        marker.showInfoWindow();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.swipe_right, R.anim.swipe_right_back);
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
