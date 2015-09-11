package com.intern.tmob.activityextreme;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.intern.tmob.activityextreme.view.SlidingTabLayout;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    WallItem activity;
    String location;
    ImageView cover,image;
    TextView name,date,detail,header;
    View rootView;
    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        activity = (WallItem) getActivity().getIntent().getSerializableExtra("object");
        location = getActivity().getIntent().getStringExtra("location");
        String[] tabs = {"YORUMLAR","YAKLAŞAN ETKİNLİKLER","GEÇMİŞ"};
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabPagerAdapter(getContext(), tabs, SplashActivityFragment.mProfile.getId()));

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.sliding_tabs);
        slidingTabLayout.setViewPager(viewPager);
        ((CollapsingToolbarLayout) rootView.findViewById(R.id.detail_collapsing_toolbar_layout)).setTitle(activity.getheader());

        findAndFill();
        return rootView;
    }
    private void findAndFill(){
        name = (TextView) rootView.findViewById(R.id.detail_name);
        date = (TextView) rootView.findViewById(R.id.detail_date);
        detail = (TextView) rootView.findViewById(R.id.detail_details);
        header = (TextView) rootView.findViewById(R.id.detail_header);
        image = (ImageView) rootView.findViewById(R.id.detail_pp);
        cover = (ImageView) rootView.findViewById(R.id.detail_cover);

        switch (activity.getheader().charAt(0)){
            case 'S':
                Glide.with(getContext()).load(R.drawable.spor).placeholder(R.color.placeholder)
                    .into(cover);
                break;
            case 'K':
                Glide.with(getContext()).load(R.drawable.kultur_sanat).placeholder(R.color.placeholder)
                        .into(cover);
                break;
            case 'G':
                Glide.with(getContext()).load(R.drawable.gezi).placeholder(R.color.placeholder)
                        .into(cover);
                break;
            case 'E':
                Glide.with(getContext()).load(R.drawable.eglence).placeholder(R.color.placeholder)
                        .into(cover);
                break;
            case 'D':
                Glide.with(getContext()).load(R.drawable.ders).placeholder(R.color.placeholder)
                        .into(cover);
                break;
            case 'A':
                Glide.with(getContext()).load(R.drawable.arac).placeholder(R.color.placeholder)
                        .into(cover);
                break;
        }

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

    }
    private void openProfile() {
        Intent intent =new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra("fid", activity.getFid());
        intent.putExtra("object", activity);
        intent.putExtra("location",location);
        startActivity(intent);
    }

}
