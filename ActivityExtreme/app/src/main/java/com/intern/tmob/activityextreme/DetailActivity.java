package com.intern.tmob.activityextreme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setupToolbar();
        setupDrawer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.i("geldimi", "geldi");
            Slide slide = new Slide(Gravity.BOTTOM);
            slide.addTarget(R.id.div1);
            slide.addTarget(R.id.div2);
            slide.addTarget(R.id.div3);
            slide.addTarget(R.id.detail_card);
            slide.addTarget(R.id.detail_name);
            slide.addTarget(R.id.detail_header);
            slide.addTarget(R.id.detail_date);
            slide.addTarget(R.id.detail_pp);
            slide.addTarget(R.id.attendButton);
            slide.addTarget(R.id.detail_details);
            slide.setInterpolator(AnimationUtils.loadInterpolator(this, android.R.interpolator
                    .linear_out_slow_in));
            slide.setDuration(300);
            getWindow().setEnterTransition(slide);
        }
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
        drawerLayout = (DrawerLayout) findViewById(R.id.detail_drawer_layout);

        //drawer header setup
        TextView name = (TextView) findViewById(R.id.profile_name);
        name.setText(SplashActivityFragment.mProfile.getName());
        SharedPreferences settings = getSharedPreferences("SplashActivityFragment", Context.MODE_PRIVATE);
        TextView location = (TextView) findViewById(R.id.profile_city);
        location.setText(settings.getString("location", "def"));
        ImageView avatar = (ImageView) findViewById(R.id.profile_image);
        Glide.with(this).load(SplashActivityFragment.mProfile.getProfilePictureUri(200, 200)).into(avatar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.detail_navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.drawer_wall:
                        onBackPressed();
                        return true;
                    case R.id.drawer_profile:
                        Intent intent = new Intent(DetailActivity.this, ProfileActivity.class);
                        intent.putExtra("fid", SplashActivityFragment.mProfile.getId());
                        startActivity(intent);
                        return true;
                    case R.id.drawer_settings:
                        startActivity(new Intent(DetailActivity.this, SettingsActivity.class));
                        return true;
                    case R.id.drawer_favourites:
                        startActivity(new Intent(DetailActivity.this, FavoritesActivity.class));
                        return true;
                }
                finish();
                menuItem.setChecked(true);
                return true;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

}
