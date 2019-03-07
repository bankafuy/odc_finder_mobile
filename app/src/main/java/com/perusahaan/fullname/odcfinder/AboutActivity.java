package com.perusahaan.fullname.odcfinder;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.perusahaan.fullname.odcfinder.adapter.OdcAdapter;
import com.perusahaan.fullname.odcfinder.adapter.SampleAdapter;
import com.perusahaan.fullname.odcfinder.model.Location;
import com.perusahaan.fullname.odcfinder.model.SampleObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private static ProgressDialog progressDialog;
    private static final String LOCATION_URL = "https://jsonplaceholder.typicode.com/todos";

    List<SampleObject> objectList;

    SampleAdapter sampleAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private Handler handler = new Handler();
    private String queryText = "";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.toolbar_search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchingJson(query);
                recyclerView.scrollToPosition(0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                handler.removeCallbacks(null);
                queryText = newText;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fetchingJson(queryText);
                        recyclerView.scrollToPosition(0);
                    }
                }, 1000);
                return true;
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
//
//        if(sampleAdapter == null) {
//            sampleAdapter = new SampleAdapter(this, objectList);
//        }
//
//
////        locationList = populateDummy();
////        odcAdapter.setLocationList(locationList);
        recyclerView = findViewById(R.id.odcRecyclerView);
//        layoutManager = recyclerView.getLayoutManager();
//        recyclerView.scrollToPosition(0);
//        recyclerView.setAdapter(sampleAdapter);

        fetchingJson("");
    }

//    List<Location> populateDummy() {
//        List<Location> locations = new LinkedList<>();
//
//        for(int i = 0; i <= 20; i++) {
//            // default: -6.2249809,106.8446636;
//            float defaultLat = -6.2249809f;
//            float defaultLong = 106.8446636f;
//            float param = i * 0.004f;
//            float param2 = i * 0.008f;
//
//            Location location = new Location("Location " + i, defaultLat + param, defaultLong + param2);
//            locations.add(location);
//        }
//
//        return locations;
//    }

//    List<Location> filter(String text) {
//        List<Location> filteredLists = new LinkedList<>();
//        for(Location location : locationList) {
//            if(location.getName().toLowerCase().contains(text.toLowerCase())) {
//                filteredLists.add(location);
//            }
//        }
//
//        return filteredLists;
//    }

    private static List<SampleObject> filter(List<SampleObject> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<SampleObject> filteredModelList = new LinkedList<>();
        for (SampleObject model : models) {
            final String text = model.getTitle().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void fetchingJson(final String query) {

//        showSimpleProgressDialog(this, "Loading...","Fetching Json",false);
//
        StringRequest stringRequest = new StringRequest(Request.Method.GET, LOCATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//
//                        removeSimpleProgressDialog();

                        try {
                            JSONArray responseJson = new JSONArray(response);
                            objectList = new LinkedList<>();
                            for (int i = 0; i < responseJson.length(); i++) {

                                JSONObject object = responseJson.getJSONObject(i);

                                SampleObject sampleObject = new SampleObject();

                                sampleObject.setId(object.getInt("id"));
                                sampleObject.setUserId(object.getInt("userId"));
                                sampleObject.setTitle(object.getString("title"));
                                sampleObject.setCompleted(object.getBoolean("completed"));

                                if (sampleObject.getTitle().contains(query.toLowerCase())) {
                                    objectList.add(sampleObject);
                                }

                            }

                            setupRecycler();
                        } catch (JSONException e) {
                            Log.d("Response JSON Error", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setupRecycler() {
        sampleAdapter = new SampleAdapter(this, objectList, new SampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SampleObject object) {
                ItemFragment itemFragment = ItemFragment.newInstance(object);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.aboutFragment, itemFragment)
                        .setCustomAnimations(R.anim.swipe_right, R.anim.swipe_right_back)
                        .commit();

                Toast.makeText(getApplicationContext(), String.format("ID: %s", object.getId()), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(sampleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    public static void removeSimpleProgressDialog() {
        try {
            if (progressDialog != null) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg, boolean isCancelable) {
        try {
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(context, title, msg);
                progressDialog.setCancelable(isCancelable);
            }

            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
