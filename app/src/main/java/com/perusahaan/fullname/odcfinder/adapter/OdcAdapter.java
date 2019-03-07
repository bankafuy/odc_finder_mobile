package com.perusahaan.fullname.odcfinder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.perusahaan.fullname.odcfinder.R;
import com.perusahaan.fullname.odcfinder.model.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Full Name on 3/6/2019.
 */

public class OdcAdapter extends RecyclerView.Adapter<OdcAdapter.ViewHolder> {

    private LayoutInflater inflater;

    private List<Location> locationList = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtName;
        private final TextView txtLatitude;
        private final TextView txtLongitude;

        public ViewHolder(View v) {
            super(v);
            txtName = v.findViewById(R.id.txtOdcName);
            txtLatitude = v.findViewById(R.id.txtUserId);
            txtLongitude = v.findViewById(R.id.txtCompleted);
        }

        public TextView getTxtName() {
            return txtName;
        }

        public TextView getTxtLatitude() {
            return txtLatitude;
        }

        public TextView getTxtLongitude() {
            return txtLongitude;
        }
    }

    public OdcAdapter(Context context, List<Location> lists) {
        inflater = LayoutInflater.from(context);
        this.locationList = lists;
    }

    @Override
    public OdcAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.odc_list_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OdcAdapter.ViewHolder holder, int position) {
        holder.getTxtName().setText(
                locationList.get(position).getName());
        holder.getTxtLatitude().setText(
                String.valueOf(locationList.get(position).getLatitude()));
        holder.getTxtLongitude().setText(
                String.valueOf(locationList.get(position).getLongitude()));

    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = null;
        this.locationList = locationList;
    }

    public List<Location> getLocationList() {
        return this.locationList;
    }
}
