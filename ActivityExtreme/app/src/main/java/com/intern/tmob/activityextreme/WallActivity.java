package com.intern.tmob.activityextreme;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mustafa.myapplication.backend.myApi.model.Entity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class WallActivity extends AppCompatActivity {
    static Entity mEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wall, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }else  if (id == R.id.action_profile) {
            Intent intent =new Intent(this, ProfileActivity.class);
            intent.putExtra("fid",SplashActivityFragment.mProfile.getId());
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onItemClicked(View v) {
        int id = v.getId();
        Log.d("iddd", id + ", " + R.id.list_item_layout);
        switch (id){
            case R.id.list_item_image: break;
            case R.id.list_item_name:break;
            case R.id.list_item_like:break;
            case R.id.list_item_comment:break;
            case R.id.list_item_share:break;
            default:
                startActivity(new Intent(this, DetailActivity.class));
                break;
        }
    }

    public void pictureIsClicked(View view) {

        Intent intent =new Intent(this, ProfileActivity.class);
        intent.putExtra("fid",(String)mEntity.getProperties().get("fid"));
        intent.putExtra("name",(String)mEntity.getProperties().get("name"));
        intent.putExtra("surname",(String)mEntity.getProperties().get("surname"));
        intent.putExtra("ppUrl",(String)mEntity.getProperties().get("ppUrl"));
        intent.putExtra("location",(String)mEntity.getProperties().get("location"));
        Log.d("loccc", "loccc " + (String) mEntity.getProperties().get("location"));
        startActivity(intent);

    }
}
