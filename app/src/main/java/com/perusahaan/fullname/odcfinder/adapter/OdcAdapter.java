package com.perusahaan.fullname.odcfinder.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.perusahaan.fullname.odcfinder.R;
import com.perusahaan.fullname.odcfinder.Utils.CustomAdapterListener;
import com.perusahaan.fullname.odcfinder.fragment.OdcViewFragment;
import com.perusahaan.fullname.odcfinder.model.OdcModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Full Name on 3/6/2019.
 */

public class OdcAdapter extends RecyclerView.Adapter<OdcAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<OdcModel> OdcModelList = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView txtName;
        private final TextView txtKapasitas;
        private final TextView txtDatel;
        private final TextView txtWitel;
        private final TextView txtLatitude;
        private final TextView txtLongitude;
        private OdcModel odcModel;

        public ViewHolder(View v) {
            super(v);
            txtName = v.findViewById(R.id.txtOdcName);
            txtKapasitas = v.findViewById(R.id.txtOdcKapasitas);
            txtDatel = v.findViewById(R.id.txtOdcDatel);
            txtWitel = v.findViewById(R.id.txtOdcWitel);
            txtLatitude = v.findViewById(R.id.txtOdcLatitude);
            txtLongitude = v.findViewById(R.id.txtOdcLongitude);
            v.setOnClickListener(this);
        }

        public TextView getTxtName() {
            return txtName;
        }
        public TextView getTxtKapasitas() {
            return txtKapasitas;
        }
        public TextView getTxtDatel() {
            return txtDatel;
        }
        public TextView getTxtWitel() {
            return txtWitel;
        }
        public TextView getTxtLatitude() {
            return txtLatitude;
        }
        public TextView getTxtLongitude() {
            return txtLongitude;
        }

        @Override
        public void onClick(View view) {
            OdcViewFragment odcViewFragment = new OdcViewFragment();

            Bundle bundle = new Bundle();
            bundle.putParcelable("odc_item", odcModel);

            odcViewFragment.setArguments(bundle);

            AppCompatActivity activity = (AppCompatActivity) view.getContext();

            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameContent, odcViewFragment)
                    .setCustomAnimations(
                            R.anim.swipe_left,
                            R.anim.swipe_left_back,
                            R.anim.swipe_right,
                            R.anim.swipe_right_back)
                    .addToBackStack("odc_list")
                    .commit();
        }

        public OdcModel getOdcModel() {
            return odcModel;
        }

        public void setOdcModel(OdcModel odcModel) {
            this.odcModel = odcModel;
        }
    }

    public OdcAdapter(Context context, List<OdcModel> lists) {
        this.inflater = LayoutInflater.from(context);
        this.OdcModelList = lists;
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
                OdcModelList.get(position).getNamaOdc());
        holder.getTxtKapasitas().setText(
                OdcModelList.get(position).getKapasitas());
        holder.getTxtDatel().setText(
                OdcModelList.get(position).getDatel());
        holder.getTxtWitel().setText(
                OdcModelList.get(position).getWitel());
        holder.getTxtLatitude().setText(
                String.valueOf(OdcModelList.get(position).getLatitude()));
        holder.getTxtLongitude().setText(
                String.valueOf(OdcModelList.get(position).getLongitude()));

        holder.setOdcModel(OdcModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return OdcModelList.size();
    }

}
