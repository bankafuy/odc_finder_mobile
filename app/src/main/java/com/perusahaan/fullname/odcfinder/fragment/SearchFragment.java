package com.perusahaan.fullname.odcfinder.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.perusahaan.fullname.odcfinder.R;
import com.perusahaan.fullname.odcfinder.Utils.MyUtils;
import com.perusahaan.fullname.odcfinder.adapter.OdcAdapter;
import com.perusahaan.fullname.odcfinder.model.OdcModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class SearchFragment extends Fragment {

    private SearchView searchView;

    private static final String LOCATION_URL = "http://khotibul.herokuapp.com/odc";

    List<OdcModel> odcLists;

    OdcAdapter odcAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private Handler handler = new Handler();
    private String queryText = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.toolbar_search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

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
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        FragmentActivity activity = getActivity();

        recyclerView = view.findViewById(R.id.odcRecyclerView);

        searchView = activity.findViewById(R.id.menu_search);

        fetchingJson("");

        return view;
    }

    private void fetchingJson(final String query) {
        MyUtils.showSimpleProgressDialog(getActivity(), "Loading...","Please wait...",true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, LOCATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        MyUtils.removeSimpleProgressDialog();

                        try {
                            JSONArray responseJson = new JSONArray(response);
                            odcLists = new LinkedList<>();
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

                                if (sampleObject.getNamaOdc().toLowerCase().contains(query.toLowerCase())) {
                                    odcLists.add(sampleObject);
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
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void setupRecycler() {
        odcAdapter = new OdcAdapter(getActivity(), odcLists);
        recyclerView.setAdapter(odcAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

}
