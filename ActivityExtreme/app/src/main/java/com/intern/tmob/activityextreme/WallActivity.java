package com.intern.tmob.activityextreme;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MotionEventCompat;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mustafa.myapplication.backend.myApi.MyApi;
import com.example.mustafa.myapplication.backend.myApi.model.Entity;
import com.example.mustafa.myapplication.backend.myApi.model.EntityCollection;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

public class WallActivity extends AppCompatActivity {
    private static MyApi myApiService = null;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);
        setupToolbar();
        setupDrawer();
    }

    private void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        if(toolbar!=null)
            setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if(ab!=null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawer(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ImageView avatar = (ImageView) findViewById(R.id.profile_image);
        Glide.with(this).load(SplashActivityFragment.mProfile.getProfilePictureUri(200, 200))
                .placeholder(R.color.placeholder).into(avatar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.drawer_profile:
                        Intent intent =new Intent(WallActivity.this, ProfileActivity.class);
                        intent.putExtra("fid",SplashActivityFragment.mProfile.getId());
                        startActivity(intent);
                        return true;
                    case R.id.drawer_settings:
                        startActivity(new Intent(WallActivity.this, SettingsActivity.class));
                        return true;
                    case R.id.drawer_favourites:
                        startActivity(new Intent(WallActivity.this, FavoritesActivity.class));
                        return true;
                }
                menuItem.setChecked(true);
                return true;
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_wall, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                if(drawerLayout!=null)
                    drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_profile:
                Intent intent =new Intent(this, ProfileActivity.class);
                intent.putExtra("fid",SplashActivityFragment.mProfile.getId());
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

  @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (findViewById(R.id.wall_refresh) != null) {
                    if (WallActivityFragment.index == 0) {
                        findViewById(R.id.wall_refresh).setEnabled(true);
                    } else {
                        findViewById(R.id.wall_refresh).setEnabled(false);
                    }
                }
                break;
        }
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }

    /*
    *   Burasi gerekli yere tasinacak.
    * */
/*
    class CommentActivityTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            *//*
                fid : the guy's id who creates activity
                date: date of activity
                time: time of activity
                commenterID: id of commenter
                comment: comment message
            *//*
            if(myApiService == null){
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://absolute-disk-105007.appspot.com/_ah/api/");
                myApiService = builder.build();
            }
            try {
                Log.i("commentActivityTask","girdi");
                *//*myApiService.commentActivity("707265706085188","2015.09.09-13:40","707265706085188","Beyler bu ikinci yorum").execute();*//*
                myApiService.commentActivity(fid,date + " " + time,commenterID,comment);

            } catch (IOException e) {
                e.printStackTrace();
                Log.i("commentActivityTask","Error");
            }
            return null;
        }
    }

    class GetCommentActivityTask extends AsyncTask<Void,Void,Void>{
        *//*
            fid : the guy's id who creates activity
            date: date of activity
            time: time of activity
         *//*
        @Override
        protected Void doInBackground(Void... params) {
            if(myApiService == null){
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://absolute-disk-105007.appspot.com/_ah/api/");
                myApiService = builder.build();
            }
            try {
                *//*EntityCollection res = myApiService.getCommentsActivity("707265706085188", "2015.09.09-13:40").execute();*//*
                EntityCollection res = myApiService.getCommentsActivity(fid,date + " " + time);
                for(int i=0;i<res.getItems().size();i++)
                    Log.i("GetCommentActivityTask", (String) res.getItems().get(i).getProperties().get("comment"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class LikeUnlikeActivity extends AsyncTask<Void,Void,Void>{

        *//*
            fid : the guy's id who creates activity
            date: date of activity
            time: time of activity
            commenterID: id of commenter
         *//*

        @Override
        protected Void doInBackground(Void... params) {
            if(myApiService == null){
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://absolute-disk-105007.appspot.com/_ah/api/");
                myApiService = builder.build();
            }
            try {
                *//*if(myApiService.isLiked("123","2015.09.09 14:14","123").execute()==null)*//*
                if(myApiService.isLiked(fid,date + " " + time,commenterID).execute() == null)
                    Log.i("likeUnlike","not liked");
                else
                    Log.i("likeUnlike","liked");
                *//*myApiService.likeUnlikeActivity("123","2015.09.09 14:14","123").execute();*//*
                myApiService.likeUnlikeActivity(fid,date + " " + time,commenterID).execute();
                Log.i("likeUnlike","liked");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }*/
}
