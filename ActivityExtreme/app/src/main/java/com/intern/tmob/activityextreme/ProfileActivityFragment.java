package com.intern.tmob.activityextreme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mustafa.myapplication.backend.myApi.MyApi;
import com.example.mustafa.myapplication.backend.myApi.model.EntityCollection;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.intern.tmob.activityextreme.view.SlidingTabLayout;
import com.example.mustafa.myapplication.backend.myApi.model.Entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProfileActivityFragment extends Fragment {

    private static MyApi myApiService = null;
    String fid;
    TextView name,city,about;
    ImageView image;
    WallItem activity;
    public ProfileActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        Intent intent = getActivity().getIntent();
        fid = intent.getStringExtra("fid");

        image = (ImageView) rootView.findViewById(R.id.profile_image);
        name = (TextView) rootView.findViewById(R.id.profile_name);
        city = (TextView) rootView.findViewById(R.id.profile_city);
        about = (TextView) rootView.findViewById(R.id.profile_about);

        String[] tabs = {"YORUMLAR","YAKLAŞAN ETKİNLİKLER","GEÇMİŞ"};
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabPagerAdapter(getContext(), tabs, fid));

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.sliding_tabs);
        slidingTabLayout.setViewPager(viewPager);

        SharedPreferences settings = getActivity().getSharedPreferences("SplashActivityFragment",Context.MODE_PRIVATE);
        if(fid.equals(SplashActivityFragment.mProfile.getId())){
            Glide.with(getContext()).load(SplashActivityFragment.mProfile.getProfilePictureUri(200, 200))
                    .placeholder(R.color.placeholder).into(image);
            name.setText(SplashActivityFragment.mProfile.getFirstName() + " "
                    + SplashActivityFragment.mProfile.getLastName());
            city.setText(settings.getString("location", "def"));
        } else {
            activity = (WallItem) intent.getSerializableExtra("object");
            name.setText(activity.getname());
            Glide.with(getContext()).load(activity.getImageLink()).into(image);
            city.setText(intent.getStringExtra("location"));
        }

        about.setText("Gokdelenler bence bu sehrin mezar taslaridir.");

        return rootView;
    }
}
