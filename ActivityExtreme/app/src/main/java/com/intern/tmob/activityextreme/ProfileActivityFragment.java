package com.intern.tmob.activityextreme;

import android.content.Intent;
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
public class ProfileActivityFragment extends Fragment {
    String fib="";
    public ProfileActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        Intent intent = getActivity().getIntent();
        fib = intent.getStringExtra("fib");

        ImageView image = (ImageView) rootView.findViewById(R.id.profile_image);
        TextView name = (TextView) rootView.findViewById(R.id.profile_name);
        TextView city = (TextView) rootView.findViewById(R.id.profile_city);
        TextView about = (TextView) rootView.findViewById(R.id.profile_about);
        TextView edit = (TextView) rootView.findViewById(R.id.profile_edit);


        Glide.with(getContext()).load(SplashActivityFragment.mProfile.getProfilePictureUri(100,100))
                .into(image);

        name.setText(SplashActivityFragment.mProfile.getFirstName()+" "
                +SplashActivityFragment.mProfile.getLastName());

        city.setText("Istanbul");
        about.setText("Gokdelenler bence bu sehrin mezar taslaridir.");
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),EditProfileActivity.class);
                startActivity(intent);
            }
        });


        return rootView;
    }
}
