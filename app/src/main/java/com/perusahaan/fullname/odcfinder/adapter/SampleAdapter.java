package com.perusahaan.fullname.odcfinder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.perusahaan.fullname.odcfinder.R;
import com.perusahaan.fullname.odcfinder.model.LocationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Full Name on 3/8/2019.
 */

public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.ViewHolder> {

    private LayoutInflater inflater;

    private List<LocationModel> objectList = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(LocationModel object);
    }

    public OnItemClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTitle;
        private final TextView txtLatitude;
        private final TextView txtLongitude;

        public ViewHolder(View v) {
            super(v);
            txtTitle = v.findViewById(R.id.txtOdcName);
            txtLatitude = v.findViewById(R.id.txtOdcLatitude);
            txtLongitude = v.findViewById(R.id.txtOdcLongitude);
        }

        public void bind(final LocationModel locationModel, final OnItemClickListener listener) {

            if(locationModel != null) {
                txtTitle.setText(String.valueOf(locationModel.getName()));
                txtLatitude.setText(String.valueOf(locationModel.getLatitude()));
                txtLongitude.setText(String.valueOf(locationModel.getLongitude()));

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onItemClick(locationModel);
                    }
                });
            }
        }

        public TextView getTxtTitle() {
            return txtTitle;
        }

        public TextView getTxtLatitude() {
            return txtLatitude;
        }

        public TextView getTxtLongitude() {
            return txtLongitude;
        }
    }

    public SampleAdapter(Context context, List<LocationModel> lists, OnItemClickListener listener) {
        inflater = LayoutInflater.from(context);
        this.objectList = lists;
        this.listener = listener;
    }

    @Override
    public SampleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.odc_list_row, parent, false);
        return new SampleAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SampleAdapter.ViewHolder holder, int position) {
        holder.bind(objectList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    public void replaceAll(List<LocationModel> models) {
        for (int i = objectList.size() - 1; i >= 0; i--) {
            final LocationModel model = objectList.get(i);
            if (!models.contains(model)) {
                objectList.remove(model);
            }
        }
        objectList.addAll(models);
    }


}
