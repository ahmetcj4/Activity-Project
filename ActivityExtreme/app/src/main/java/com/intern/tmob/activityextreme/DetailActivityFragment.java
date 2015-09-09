package com.intern.tmob.activityextreme;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    WallItem activity;
    String location;
    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        activity = (WallItem) getActivity().getIntent().getSerializableExtra("object");
        location = getActivity().getIntent().getStringExtra("location");

        ((CollapsingToolbarLayout) rootView.findViewById(R.id.detail_collapsing_toolbar_layout)).setTitle(activity.getheader());
        TextView name = (TextView) rootView.findViewById(R.id.detail_name);
        TextView date = (TextView) rootView.findViewById(R.id.detail_date);
        TextView detail = (TextView) rootView.findViewById(R.id.detail_details);
        TextView header = (TextView) rootView.findViewById(R.id.detail_header);
        ImageView image = (ImageView) rootView.findViewById(R.id.detail_pp);

        Glide.with(getContext()).load(activity.getImageLink())
                .into(image);
        header.setText(activity.getheader());
        name.setText(activity.getname());
        date.setText(activity.getsent());
        detail.setText(activity.getdetail());

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();
            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();
            }
        });

        return rootView;
    }

    private void openProfile() {
        Intent intent =new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra("fid", activity.getFid());
        intent.putExtra("object", activity);
        intent.putExtra("location",location);
        startActivity(intent);
    }

}
