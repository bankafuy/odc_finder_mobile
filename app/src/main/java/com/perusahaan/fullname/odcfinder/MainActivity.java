package com.perusahaan.fullname.odcfinder;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBar actionBar;

    private boolean showSearch = true;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(showSearch) {
            getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        final AboutFragment aboutFragment = new AboutFragment();
        final HomeFragment homeFragment = new HomeFragment();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        actionBar.setTitle("Home");
                        showSearch = true;
                        invalidateOptionsMenu();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameContent, homeFragment)
                                .setCustomAnimations(R.anim.swipe_right, R.anim.swipe_right_back)
                                .commit();
                        break;
                    case R.id.menu_about:
                        actionBar.setTitle("About");
                        showSearch = false;
                        invalidateOptionsMenu();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameContent, aboutFragment)
                                .setCustomAnimations(R.anim.swipe_right, R.anim.swipe_right_back)
                                .commit();
                        break;
                    case R.id.menu_list:
                        Toast.makeText(getApplicationContext(), "Menu List", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_logout:
                        Toast.makeText(getApplicationContext(), "Menu Logout", Toast.LENGTH_SHORT).show();
                        msgYesNo(MainActivity.this, "yakin?");
                        break;
                    case R.id.menu_profile:
                        Toast.makeText(getApplicationContext(), "Menu Profile", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_search:
                        Toast.makeText(getApplicationContext(), "Menu Search", Toast.LENGTH_SHORT).show();
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

    public void msgYesNo(Context context, String message) {

        DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case BUTTON_NEGATIVE:
                        break;
                    case BUTTON_POSITIVE:
                        finish();
                        System.exit(0);
                        break;
                }
            }
        };

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Yakin ingin keluar dari aplikasi?")
                .setMessage(message)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    @Override
    public void onBackPressed() {
        msgYesNo(MainActivity.this, "yakin?");
    }

    public void setTitleBar(String title) {
        this.actionBar.setTitle(title);
    }
}
