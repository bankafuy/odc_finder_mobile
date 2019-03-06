package com.perusahaan.fullname.odcfinder;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.perusahaan.fullname.odcfinder.model.Location;

import java.util.LinkedList;
import java.util.List;

public class AboutActivity extends AppCompatActivity {

    String[] searchItems = new String[]
    {
        "Jakarta",
        "Bandung",
        "Surabaya",
        "Pandeglang",
        "Serang"
    };
    //        searchItems[5] = "Cilegon";
    private SearchView searchView;

    List<Location> locationList;

    OdcAdapter odcAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.toolbar_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("###about2", query);
                List<Location> filtered = filter(query);
                odcAdapter.setLocationList(filtered);
                recyclerView.scrollToPosition(0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Log.d("###about2", newText);
//                odcAdapter.setLocationList(filter(locationList, newText));
//                recyclerView.scrollToPosition(0);
//                return true;
                return false;
            }
        });

        searchView.setQueryHint("Pencarian...");

        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);

        if(odcAdapter == null) {
            odcAdapter = new OdcAdapter();
        }
        locationList = populateDummy();
        odcAdapter.setLocationList(locationList);
        recyclerView = findViewById(R.id.odcRecyclerView);
        layoutManager = recyclerView.getLayoutManager();
        recyclerView.scrollToPosition(0);
        recyclerView.setAdapter(odcAdapter);

    }

    List<Location> populateDummy() {
        List<Location> locations = new LinkedList<>();

        for(int i = 0; i <= 20; i++) {
            // default: -6.2249809,106.8446636;
            float defaultLat = -6.2249809f;
            float defaultLong = 106.8446636f;
            float param = i * 0.004f;
            float param2 = i * 0.008f;

            Location location = new Location("Location " + i, defaultLat + param, defaultLong + param2);
            locations.add(location);
        }

        return locations;
    }

    List<Location> filter(String text) {
        List<Location> filteredLists = new LinkedList<>();
        for(Location location : locationList) {
            if(location.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredLists.add(location);
            }
        }

        return filteredLists;
    }

}
