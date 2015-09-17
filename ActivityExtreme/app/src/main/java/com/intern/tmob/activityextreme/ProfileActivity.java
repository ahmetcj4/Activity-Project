package com.intern.tmob.activityextreme;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupToolbar();
        setupDrawer();
    }
    private void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.detail_app_bar);
        if(toolbar!=null)
            setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if(ab!=null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }
    private void setupDrawer(){
        //TODO drawer header gozukmuyor
        drawerLayout = (DrawerLayout) findViewById(R.id.profile_drawer_layout);
        ((CollapsingToolbarLayout) findViewById(R.id.profile_collapsing_toolbar_layout)).setTitle("");
        //drawer header setup
        TextView name = (TextView) findViewById(R.id.drawer_name);
        name.setText(SplashActivityFragment.mProfile.getName());
        SharedPreferences settings = getSharedPreferences("SplashActivityFragment", Context.MODE_PRIVATE);
        TextView location = (TextView) findViewById(R.id.drawer_city);
        location.setText(settings.getString("location", "def"));
        ImageView avatar = (ImageView) findViewById(R.id.drawer_image);
        Glide.with(this).load(SplashActivityFragment.mProfile.getProfilePictureUri(200, 200)).into(avatar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.profile_navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.drawer_wall:
                            startActivity(new Intent(ProfileActivity.this,WallActivity.class));
                        return true;
                    case R.id.drawer_profile:
                        if(!ProfileActivityFragment.fid.equals(SplashActivityFragment.mProfile.getId())){
                            Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                            intent.putExtra("fid", SplashActivityFragment.mProfile.getId());
                            startActivity(intent);
                        }
                        return true;
                    case R.id.drawer_favourites:
                        startActivity(new Intent(ProfileActivity.this, FavoritesActivity.class));
                        return true;
                    case R.id.drawer_about:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startActivity(new Intent(ProfileActivity.this, AboutActivity.class),
                                    ActivityOptions.makeSceneTransitionAnimation(ProfileActivity.this).toBundle());
                        } else
                            startActivity(new Intent(ProfileActivity.this, AboutActivity.class));
                        return true;
                }
                menuItem.setChecked(true);
                return true;
            }
        });
    }

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
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }

    /*
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (findViewById(R.id.pager_refresh) != null) {
                    if (ProfileActivityFragment.index == 1) {
                        findViewById(R.id.pager_refresh).setEnabled(true);
                    } else {
                        findViewById(R.id.pager_refresh).setEnabled(false);
                    }
                }
                break;
        }
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }*/

}
