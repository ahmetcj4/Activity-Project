package com.intern.tmob.activityextreme;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionValues;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.example.mustafa.myapplication.backend.myApi.MyApi;
import com.example.mustafa.myapplication.backend.myApi.model.Entity;
import com.example.mustafa.myapplication.backend.myApi.model.EntityCollection;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class WallActivityFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {
    private static MyApi myApiService = null;
    List<WallItem> mWallItem = new ArrayList<>();
    RecyclerView recyclerView;
    WallItemAdapter mWallItemAdapter;
    private AppBarLayout appBarLayout;
    static int cnt=0;
    SwipeRefreshLayout srl;
    private List<Entity> mEntities;
    View rootView;
    public WallActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_wall, container, false);
        ((CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar_layout)).setTitle(getString(R.string.app_name));
        appBarLayout = (AppBarLayout) rootView.findViewById(R.id.app_bar_layout);
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
                        Intent intent = new Intent(getActivity(),DetailActivity.class);
                        intent.putExtra("object", mWallItem.get(position));
                        intent.putExtra("location", (String) mEntities.get(position).getProperties().get("location"));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getContext().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                        } else
                        startActivity(intent);

                        return;
                    }

                })
        );

        return rootView;
    }
    static int index = 0;
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        index = i;
    }
    @Override
    public void onResume() {
        super.onResume();
        appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        appBarLayout.removeOnOffsetChangedListener(this);
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
            List<Entity> list = null;
            EntityCollection ec = null;
            try {
                ec = myApiService.fetchWall().execute();
                list = ec.getItems();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return list;
        }

        @Override
        protected void onPostExecute(List<Entity> entities) {
            mWallItem.clear();
            mEntities = entities;
            if(entities!=null)
            for(Entity e : entities){
                mWallItem.add(new WallItem((String)e.getProperties().get("ppUrl"),
                        (String) e.getProperties().get("name")+" "+e.getProperties().get("surname"),
                        (String) e.getProperties().get("date")+" "+e.getProperties().get("time"),
                        (String) e.getProperties().get("details"),
                        (String) e.getProperties().get("type")+" - "+(String) e.getProperties().get("title"),
                        (String) e.getProperties().get("fid")));
            }
            mWallItemAdapter.notifyDataSetChanged();
            startTransition();
            srl.setRefreshing(false);
        }
    }

    public void startTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Explode explode = new Explode();
            explode.addTarget(R.id.list_item_cardview);
            explode.setInterpolator(AnimationUtils.loadInterpolator(getActivity(), android.R.interpolator
                    .linear_out_slow_in));
            explode.setDuration(300);
            TransitionManager.beginDelayedTransition((ViewGroup) rootView,explode);
        }
    }
}
