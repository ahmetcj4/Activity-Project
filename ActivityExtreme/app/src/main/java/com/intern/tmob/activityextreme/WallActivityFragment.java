package com.intern.tmob.activityextreme;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
    static int cnt=0;
    SwipeRefreshLayout srl;
    private List<Entity> mEntities;

    public WallActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wall, container, false);
        ((CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar_layout)).setTitle(getString(R.string.app_name));
        srl = (SwipeRefreshLayout)rootView.findViewById(R.id.wall_refresh);

        new FetchWallTask().execute(getActivity());
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchWallTask().execute(getActivity());
            }
        });

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.myFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),NewActivity.class));
            }
        });

        mWallItemAdapter = new WallItemAdapter(mWallItem,R.layout.list_item_wall);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.wall_recyclerview);
        recyclerView.setHasFixedSize(true);//bunu silmeyi unutma
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(mWallItemAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        WallActivity.mEntity = mEntities.get(position);
                        return;
                    }

                })
        );

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
            mWallItem.clear();
            mEntities = entities;
            for(Entity e : entities){
                Log.i("ppUrl",(String) e.getProperties().get("ppUrl"));
                mWallItem.add(new WallItem((String)e.getProperties().get("ppUrl"),
                        (String) e.getProperties().get("name")+" "+e.getProperties().get("surname"),
                        (String) e.getProperties().get("date")+" "+e.getProperties().get("time"),
                        (String) e.getProperties().get("details"),
                        (String) e.getProperties().get("type")+" - "+(String) e.getProperties().get("title"),
                        (String) e.getProperties().get("fid")));
            }
            mWallItemAdapter.notifyDataSetChanged();
            srl.setRefreshing(false);

        }
    }
}
