package com.perusahaan.fullname.odcfinder;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.perusahaan.fullname.odcfinder.model.SampleObject;

public class ItemFragment extends Fragment {

    private static String ITEM_KEY = "item_key";
    private SampleObject sampleObject;
    private TextView txtCompleted;

    public static ItemFragment newInstance(SampleObject sampleObject) {
        ItemFragment itemFragment = new ItemFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(ITEM_KEY, sampleObject);

        itemFragment.setArguments(bundle);

        return itemFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sampleObject = (SampleObject) getArguments().getSerializable(ITEM_KEY);

        SampleObject parcelable = getArguments().getParcelable(ITEM_KEY);


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        txtCompleted = view.findViewById(R.id.txtCompleted);

        if(parcelable != null) {

            txtCompleted.setText(parcelable.getTitle());
        }

        return view;
    }

}
