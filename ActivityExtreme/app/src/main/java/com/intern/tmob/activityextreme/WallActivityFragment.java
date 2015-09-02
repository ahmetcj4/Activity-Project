package com.intern.tmob.activityextreme;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mustafa.myapplication.backend.myApi.MyApi;
import com.example.mustafa.myapplication.backend.myApi.model.Entity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class WallActivityFragment extends Fragment {
    private static MyApi myApiService = null;
    List<WallItem> mWallItem = new ArrayList<>();
    RecyclerView recyclerView;
    WallItemAdapter mWallItemAdapter;
    public WallActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wall, container, false);
        new FetchWallTask().execute(getActivity());


        recyclerView = (RecyclerView)rootView.findViewById(R.id.wall_recyclerview);
        recyclerView.setHasFixedSize(true);//bunu silmeyi unutma
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        recyclerView.setAdapter(mWallItemAdapter);


        return rootView;
    }

    class FetchWallTask extends AsyncTask<Context,Void, List<Entity>> {
        Context context;
        @Override
        protected List<Entity> doInBackground(Context... params) {
            if(myApiService == null){
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://absolute-disk-105007.appspot.com/_ah/api/");
                myApiService = builder.build();
            }
            context = params[0];
            List<Entity> list = new ArrayList<>();
            try {
                for(int i=0;i<20;i++) {
                //TODO 20 tane cekiyor bunu ayarlariz
                    Entity res = myApiService.fetchWall(i).execute();
                    if(res!=null)
                        list.add(res);
                    else
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Entity> entities) {
            Log.i("entities", String.valueOf(entities.size()));
            for(Entity e : entities){
                mWallItem.add(new WallItem(R.mipmap.ic_launcher,
                        (String) e.getProperties().get("name"),
                        (String) e.getProperties().get("date"),
                        (String) e.getProperties().get("details"),
                        (String) e.getProperties().get("title")));
            }
            mWallItemAdapter = new WallItemAdapter(mWallItem,R.layout.list_item_wall);
        }
    }
}
