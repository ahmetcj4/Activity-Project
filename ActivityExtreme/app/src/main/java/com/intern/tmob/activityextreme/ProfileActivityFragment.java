package com.intern.tmob.activityextreme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mustafa.myapplication.backend.myApi.MyApi;
import com.example.mustafa.myapplication.backend.myApi.model.Entity;
import com.example.mustafa.myapplication.backend.myApi.model.EntityCollection;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.intern.tmob.activityextreme.view.SlidingTabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProfileActivityFragment extends Fragment{

    private static MyApi myApiService = null;
    String fid;
    TextView name,city,about;
    ImageView image;
    WallItem activity;
    Button pos;
    public ProfileActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        Intent intent = getActivity().getIntent();
        fid = intent.getStringExtra("fid");

        pos = (Button) rootView.findViewById(R.id.profile_positive);
        new GetLikesPerson().execute();

        pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LikePerson().execute();
                new GetLikesPerson().execute();
            }
        });


        image = (ImageView) rootView.findViewById(R.id.profile_image);
        name = (TextView) rootView.findViewById(R.id.profile_name);
        city = (TextView) rootView.findViewById(R.id.profile_city);
        about = (TextView) rootView.findViewById(R.id.profile_about);
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabPagerAdapter(getContext(), getTabs(container)));

        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.sliding_tabs);
        slidingTabLayout.setViewPager(viewPager);

        SharedPreferences settings = getActivity().getSharedPreferences("SplashActivityFragment",Context.MODE_PRIVATE);
        if(fid.equals(SplashActivityFragment.mProfile.getId())){
            Glide.with(getContext()).load(SplashActivityFragment.mProfile.getProfilePictureUri(200, 200)).into(image);
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
    List<WallItem> mWallItem,mWallItem1,mWallItem2;
    WallItemAdapter mWallItemAdapter,mWallItemAdapter1,mWallItemAdapter2;
    String acomment;

    private View[] getTabs(ViewGroup container) {

        String[] tags={"YORUMLAR","YAKLAŞAN ETKİNLİKLER","GEÇMİŞ"};
        final View view0 = LayoutInflater.from(getContext()).inflate(R.layout.pager_comment,
                container, false);
        view0.setTag("YORUMLAR");
        mWallItem = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) view0.findViewById(R.id.pager_recyclerview);
        recyclerView.setHasFixedSize(true);//bunu silmeyi unutma
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);

        mWallItemAdapter = new WallItemAdapter(mWallItem,R.layout.pager_comment_item);
        recyclerView.setAdapter(mWallItemAdapter);
        new FetchCommentUserTask().execute();
        Button button = (Button) view0.findViewById(R.id.addComment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText comment = (EditText) view0.findViewById(R.id.profile_comment);
                acomment = comment.getText().toString();
                comment.setText("");
                new CommentUserTask().execute();
            }
        });

        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.pager_comment,
                container, false);
        view1.setTag("YAKLAŞAN ETKİNLİKLER");

        LinearLayout comment = (LinearLayout) view1.findViewById(R.id.comment);
        comment.setVisibility(View.GONE);

        mWallItem1 = new ArrayList<>();

        RecyclerView recyclerView1 = (RecyclerView) view1.findViewById(R.id.pager_recyclerview);
        recyclerView1.setHasFixedSize(true);//bunu silmeyi unutma
        LinearLayoutManager llm1 = new LinearLayoutManager(getContext());
        recyclerView1.setLayoutManager(llm1);

        mWallItemAdapter1 = new WallItemAdapter(mWallItem1,R.layout.pager_comment_item);
        recyclerView1.setAdapter(mWallItemAdapter1);
        new GetOncomingActivitiesTask().execute();

        View view2 = LayoutInflater.from(getContext()).inflate(R.layout.pager_comment,
                container, false);
        view2.setTag("GEÇMİŞ");
        LinearLayout comment2 = (LinearLayout) view2.findViewById(R.id.comment);
        comment2.setVisibility(View.GONE);
        mWallItem2 = new ArrayList<>();

        RecyclerView recyclerView2 = (RecyclerView) view2.findViewById(R.id.pager_recyclerview);
        recyclerView2.setHasFixedSize(true);//bunu silmeyi unutma
        LinearLayoutManager llm2 = new LinearLayoutManager(getContext());
        recyclerView2.setLayoutManager(llm2);

        mWallItemAdapter2 = new WallItemAdapter(mWallItem2,R.layout.pager_comment_item);
        recyclerView2.setAdapter(mWallItemAdapter2);
        new GetAttendedActivitiesTask().execute();

        View[] views = {view0,view1,view2};
        return views;
    }
    class GetOncomingActivitiesTask extends AsyncTask<Void,Void,List<Entity>> {
        @Override
        protected List<Entity> doInBackground(Void... params) {
            /*
            fid : the guy's id who creates activity
            date: date of activity
            */
            if(myApiService == null){
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://absolute-disk-105007.appspot.com/_ah/api/");
                myApiService = builder.build();
            }
            List<Entity> list= null;
            try {

                EntityCollection ec = myApiService.getOncomingActivities(fid).execute();
                list = ec.getItems();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return list;
        }
        @Override
        protected void onPostExecute(List<Entity> entities) {
            mWallItem1.clear();
            if(entities!=null)
            for(Entity e : entities){
                mWallItem1.add(new WallItem((String)e.getProperties().get("ppUrl"),
                        (String) e.getProperties().get("name")+" "+e.getProperties().get("surname"),
                        (String) e.getProperties().get("date")+" "+e.getProperties().get("time"),
                        (String) e.getProperties().get("details"),
                        (String) e.getProperties().get("type")+" - "+(String) e.getProperties().get("title"),
                        (String) e.getProperties().get("fid")));
            }
            mWallItemAdapter1.notifyDataSetChanged();

        }
    }
    class GetAttendedActivitiesTask extends AsyncTask<Void,Void,List<Entity>> {
        @Override
        protected List<Entity> doInBackground(Void... params) {
            /*
            fid : the guy's id who creates activity
            date: date of activity
            */
            if(myApiService == null){
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://absolute-disk-105007.appspot.com/_ah/api/");
                myApiService = builder.build();
            }
            List<Entity> list= null;
            try {

                EntityCollection ec = myApiService.getAttendedActivities(fid).execute();
                list = ec.getItems();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return list;
        }
        @Override
        protected void onPostExecute(List<Entity> entities) {
            mWallItem2.clear();
            if(entities!=null)
                for(Entity e : entities){
                    mWallItem2.add(new WallItem((String)e.getProperties().get("ppUrl"),
                            (String) e.getProperties().get("name")+" "+e.getProperties().get("surname"),
                            (String) e.getProperties().get("date")+" "+e.getProperties().get("time"),
                            (String) e.getProperties().get("details"),
                            (String) e.getProperties().get("type")+" - "+(String) e.getProperties().get("title"),
                            (String) e.getProperties().get("fid")));
                }
            mWallItemAdapter2.notifyDataSetChanged();

        }
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
                EntityCollection ec = myApiService.getCommentsUser(fid).execute();
                list = ec.getItems();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<Entity> entities) {
            mWallItem.clear();

            for(Entity e : entities){
                mWallItem.add(new WallItem((String)e.getProperties().get("ppUrl")
                        , (String)e.getProperties().get("name")+" "+(String)e.getProperties().get("surname"),
                        (String)e.getProperties().get("date")+" "+(String)e.getProperties().get("time"),
                        (String) e.getProperties().get("comment"), " ",
                        (String) e.getProperties().get("commenterID")));
            }
            mWallItemAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(),"finished " + mWallItem.size(),Toast.LENGTH_SHORT).show();
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
                Calendar c = Calendar.getInstance();
                String sMonth = (c.get(Calendar.MONTH)+1<10?"0":"") + (c.get(Calendar.MONTH)+1);
                String sDayOfMonth = (c.get(Calendar.DAY_OF_MONTH)<10?"0":"") + c.get(Calendar.DAY_OF_MONTH);
                String sHourOfDay = (c.get(Calendar.HOUR_OF_DAY)<10?"0":"") + c.get(Calendar.HOUR_OF_DAY);
                String sMinute = (c.get(Calendar.MINUTE)<10?"0":"") + c.get(Calendar.MINUTE);
                String sDate = c.get(Calendar.YEAR) + "." + sMonth
                        + "." + sDayOfMonth + " " + sHourOfDay
                        + ":" + sMinute;
                SharedPreferences settings = getContext().getSharedPreferences("SplashActivityFragment", Context.MODE_PRIVATE);
                myApiService.commentUser(fid, SplashActivityFragment.mProfile.getId(),acomment,
                        SplashActivityFragment.mProfile.getProfilePictureUri(200,200).toString(),
                        settings.getString("location", "def"),
                        c.get(Calendar.YEAR) + "." + sMonth + "." + sDayOfMonth,
                        sHourOfDay + ":" + sMinute,SplashActivityFragment.mProfile.getFirstName(),
                        SplashActivityFragment.mProfile.getLastName()).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    class GetLikesPerson extends AsyncTask<Void,Void,List<Entity>> {
        @Override
        protected List<Entity> doInBackground(Void... params) {
            /*
            fid : the guy's id who creates activity
            date: date of activity
            */
            if(myApiService == null){
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://absolute-disk-105007.appspot.com/_ah/api/");
                myApiService = builder.build();
            }
            List<Entity> list= null;
            try {

                EntityCollection ec = myApiService.getLikesPerson(fid).execute();
                list = ec.getItems();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return list;
        }
        @Override
        protected void onPostExecute(List<Entity> entities) {
            if(entities!=null)
                pos.setText("+" + entities.size());
            else pos.setText("+0");
        }
    }
    class LikePerson extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            /*
            fid : the guy's id who creates activity
            date: date of activity
            */
            if(myApiService == null){
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://absolute-disk-105007.appspot.com/_ah/api/");
                myApiService = builder.build();
            }
            try {
                myApiService.likeUnlikePerson(fid, SplashActivityFragment.mProfile.getId()).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
