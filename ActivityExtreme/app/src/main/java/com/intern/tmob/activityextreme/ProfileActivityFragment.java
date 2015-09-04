package com.intern.tmob.activityextreme;

import android.content.Intent;
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
    static String acomment="";
    public ProfileActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        Intent intent = getActivity().getIntent();
        fid = intent.getStringExtra("fid");

        ImageView image = (ImageView) rootView.findViewById(R.id.profile_image);
        TextView name = (TextView) rootView.findViewById(R.id.profile_name);
        TextView city = (TextView) rootView.findViewById(R.id.profile_city);
        TextView about = (TextView) rootView.findViewById(R.id.profile_about);
        Button button = (Button) rootView.findViewById(R.id.addComment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText comment = (EditText) rootView.findViewById(R.id.profile_comment);
                acomment = comment.getText().toString();
                new CommentUserTask().execute();
            }
        });


        String[] tabs = {"YORUMLAR","YAKLAŞAN ETKİNLİKLER","GEÇMİŞ"};
        ViewPager mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new TabPagerAdapter(getContext(),tabs));

        SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);


        Glide.with(getContext()).load(SplashActivityFragment.mProfile.getProfilePictureUri(100,100))
                .into(image);

        name.setText(SplashActivityFragment.mProfile.getFirstName() + " "
                + SplashActivityFragment.mProfile.getLastName());

        city.setText("Istanbul");
        about.setText("Gokdelenler bence bu sehrin mezar taslaridir.");
        new FetchCommentUserTask().execute();

        return rootView;
    }
    class FetchCommentUserTask extends AsyncTask<Void,Void,List<Entity>> {

        @Override
        protected List<Entity> doInBackground(Void... params) {
            if(myApiService == null){
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://absolute-disk-105007.appspot.com/_ah/api/");
                myApiService = builder.build();
            }
            List<Entity> list = new ArrayList<>();
            try {
                for (int i = 0; i < 20; i++) {
                    Entity e = myApiService.getCommentsUser(SplashActivityFragment.mProfile.getId(), i).execute();
                    if(e == null)
                        break;
                    else
                        list.add(e);
                }
            }catch (Exception e){
                Log.i("exception",e.toString());
            }
            Log.i("fetchCommentUserTask", String.valueOf(list.size()));
            return list;
        }

        @Override
        protected void onPostExecute(List<Entity> entities) {
            for(Entity e : entities){
                Log.i("fetchCommentUserTask", (String) e.getProperties().get("comment"));
            }
        }
    }

    class CommentUserTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            if(myApiService == null){
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://absolute-disk-105007.appspot.com/_ah/api/");
                myApiService = builder.build();
            }
            try {
                myApiService.commentUser(fid, SplashActivityFragment.mProfile.getId(),acomment).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
