package com.intern.tmob.activityextreme;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProfileActivityFragment extends Fragment {

    public ProfileActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageView image = (ImageView) rootView.findViewById(R.id.profile_image);
        TextView name = (TextView) rootView.findViewById(R.id.profile_name);
        TextView city = (TextView) rootView.findViewById(R.id.profile_city);
        TextView about = (TextView) rootView.findViewById(R.id.profile_about);
        TextView edit = (TextView) rootView.findViewById(R.id.profile_edit);

        image.setImageResource(R.mipmap.ic_launcher);
        name.setText("Lukas Podolski");
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
