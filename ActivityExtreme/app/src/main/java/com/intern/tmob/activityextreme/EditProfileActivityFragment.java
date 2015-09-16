package com.intern.tmob.activityextreme;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class EditProfileActivityFragment extends Fragment {

    public EditProfileActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        ImageView image = (ImageView) rootView.findViewById(R.id.edit_image);
        EditText name = (EditText) rootView.findViewById(R.id.edit_name);
        EditText city = (EditText) rootView.findViewById(R.id.edit_city);
        EditText about = (EditText) rootView.findViewById(R.id.edit_about);

        Glide.with(getContext()).load(SplashActivityFragment.mProfile.getProfilePictureUri(100,100))
                .into(image);

        name.setText(SplashActivityFragment.mProfile.getFirstName()+" "
                +SplashActivityFragment.mProfile.getLastName());



        return rootView;
    }
}
