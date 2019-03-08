package com.perusahaan.fullname.odcfinder;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.perusahaan.fullname.odcfinder.Utils.CircleTransformation;
import com.perusahaan.fullname.odcfinder.Utils.Constant;
import com.perusahaan.fullname.odcfinder.fragment.AboutFragment;
import com.perusahaan.fullname.odcfinder.fragment.HomeFragment;
import com.perusahaan.fullname.odcfinder.fragment.OdcViewFragment;
import com.perusahaan.fullname.odcfinder.fragment.ProfileFragment;
import com.perusahaan.fullname.odcfinder.fragment.SearchFragment;
import com.squareup.picasso.Picasso;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();

        boolean isLoggedIn = prefs.getBoolean(Constant.PREF_LOGIN, false);

        if(!isLoggedIn) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        checkPermissionAndEnableIt();


    }

    private DrawerLayout drawerLayout;
    private ActionBar actionBar;

    private boolean showSearch = false;
    private SharedPreferences prefs;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (showSearch) {
            getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = this.getSharedPreferences("com.perusahaan.fullname.odcfinder", Context.MODE_PRIVATE);

        boolean isLoggedIn = prefs.getBoolean(Constant.PREF_LOGIN, false);

        if(!isLoggedIn) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        checkPermissionAndEnableIt();

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        final SearchFragment searchFragment = new SearchFragment();
        final HomeFragment homeFragment = new HomeFragment();
        final AboutFragment aboutFragment = new AboutFragment();
        final ProfileFragment profileFragment = new ProfileFragment();

        NavigationView navigationView = findViewById(R.id.nav_view);

        ImageView imgLogo = navigationView.getHeaderView(0).findViewById(R.id.logoDrawer);

        if(imgLogo != null) {
            Picasso.get()
                    .load(R.drawable.img_profile)
                    .transform(new CircleTransformation())
                    .into(imgLogo);
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        actionBar.setTitle("Home");
                        showSearch = false;
                        invalidateOptionsMenu();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameContent, homeFragment)
                                .setCustomAnimations(R.anim.swipe_right, R.anim.swipe_right_back)
                                .commit();
                        break;
                    case R.id.menu_about:
                        actionBar.setTitle("Tentang");
                        showSearch = false;
                        invalidateOptionsMenu();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameContent, aboutFragment)
                                .setCustomAnimations(R.anim.swipe_right, R.anim.swipe_right_back)
                                .commit();
                        break;
                    case R.id.menu_list:
                        actionBar.setTitle("Daftar ODC");
                        showSearch = false;
                        invalidateOptionsMenu();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameContent, searchFragment)
                                .setCustomAnimations(R.anim.swipe_right, R.anim.swipe_right_back)
                                .commit();
                        break;
                    case R.id.menu_logout:
                        Toast.makeText(getApplicationContext(), "Menu Logout", Toast.LENGTH_SHORT).show();
                        msgYesNo(MainActivity.this, "Yakin?");
                        break;
                    case R.id.menu_profile:
                        actionBar.setTitle("Profile");
                        showSearch = false;
                        invalidateOptionsMenu();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameContent, profileFragment)
                                .setCustomAnimations(R.anim.swipe_right, R.anim.swipe_right_back)
                                .commit();
                        break;
                    case R.id.menu_search:
                        actionBar.setTitle("Pencarian...");
                        showSearch = false;
                        invalidateOptionsMenu();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameContent, searchFragment)
                                .setCustomAnimations(R.anim.swipe_right, R.anim.swipe_right_back)
                                .commit();
                        break;
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setTitle("Home");
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameContent, homeFragment)
                .setCustomAnimations(R.anim.swipe_right, R.anim.swipe_right_back)
                .commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.toolbar_search:
                Toast.makeText(getApplicationContext(), "Search", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void msgYesNo(Context context, String message) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Yakin ingin keluar dari aplikasi?")
                .setMessage(message)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prefs.edit().putBoolean(Constant.PREF_LOGIN, false).apply();
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    @Override
    public void onBackPressed() {
        msgYesNo(MainActivity.this, "Yakin?");
    }

    public void setTitleBar(String title) {
        this.actionBar.setTitle(title);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "You must enable gps.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(this, "You must enable gps.", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void checkPermissionAndEnableIt() {

        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        if (locationManager != null
                && (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
            String provider = locationManager.getBestProvider(new Criteria(), false);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, 1);

                return;
            }
            locationManager.getLastKnownLocation(provider);
        } else {
            buildAlertMessageNoGps();
        }

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Gps tidak menyala, hidupkan?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
