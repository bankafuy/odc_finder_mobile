package com.perusahaan.fullname.odcfinder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

    public interface OnItemClickListener {
        void onItemClick(SampleObject object);
    }

    public OnItemClickListener listener;

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

        public void bind(final SampleObject sampleObject, final OnItemClickListener listener) {

            if(sampleObject != null) {
                txtTitle.setText(String.valueOf(sampleObject.getTitle()));
                txtUserId.setText(String.valueOf(sampleObject.getUserId()));
                txtCompleted.setText(String.valueOf(sampleObject.isCompleted()));

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onItemClick(sampleObject);
                    }
                });
            }
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

    public SampleAdapter(Context context, List<SampleObject> lists, OnItemClickListener listener) {
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
