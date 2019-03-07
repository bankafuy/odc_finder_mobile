package com.perusahaan.fullname.odcfinder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.perusahaan.fullname.odcfinder.R;
import com.perusahaan.fullname.odcfinder.model.SampleObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Full Name on 3/8/2019.
 */

public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.ViewHolder> {

    private LayoutInflater inflater;

    private List<SampleObject> objectList = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTitle;
        private final TextView txtUserId;
        private final TextView txtCompleted;

        public ViewHolder(View v) {
            super(v);
            txtTitle = v.findViewById(R.id.txtOdcName);
            txtUserId = v.findViewById(R.id.txtUserId);
            txtCompleted = v.findViewById(R.id.txtCompleted);
        }

        public TextView getTxtTitle() {
            return txtTitle;
        }

        public TextView getTxtUserId() {
            return txtUserId;
        }

        public TextView getTxtCompleted() {
            return txtCompleted;
        }
    }

    public SampleAdapter(Context context, List<SampleObject> lists) {
        inflater = LayoutInflater.from(context);
        this.objectList = lists;
    }

    @Override
    public SampleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.odc_list_row, parent, false);
        return new SampleAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SampleAdapter.ViewHolder holder, int position) {
        holder.getTxtTitle().setText(
                objectList.get(position).getTitle());
        holder.getTxtUserId().setText(
                String.valueOf(objectList.get(position).getUserId()));
        holder.getTxtCompleted().setText(
                String.valueOf(objectList.get(position).isCompleted()));

    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    public void replaceAll(List<SampleObject> models) {
        for (int i = objectList.size() - 1; i >= 0; i--) {
            final SampleObject model = objectList.get(i);
            if (!models.contains(model)) {
                objectList.remove(model);
            }
        }
        objectList.addAll(models);
    }


}
