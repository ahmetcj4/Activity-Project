package com.intern.tmob.activityextreme;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
//        ((CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar_layout)).setTitle(getString(R.string.app_name));
        ((CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar_layout)).setTitle("Aktivite");


        return rootView;
    }
}
