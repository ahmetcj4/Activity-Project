package com.intern.tmob.activityextreme;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class FavoritesActivityFragment extends Fragment {
    List<WallItem> mWallItem = new ArrayList<>();
    RecyclerView recyclerView;
    WallItemAdapter mWallItemAdapter;

    public FavoritesActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        FloatingActionButton floatingActionButton = (FloatingActionButton)rootView.findViewById(R.id.continue_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WallActivity.class);
                startActivity(intent);
            }
        });
        mWallItem.add(new WallItem(R.mipmap.ic_launcher, "Spor", "", ""));
        mWallItem.add(new WallItem(R.mipmap.ic_launcher, "Kültür Sanat", "", ""));
        mWallItem.add(new WallItem(R.mipmap.ic_launcher, "Gezi", "", ""));
        mWallItem.add(new WallItem(R.mipmap.ic_launcher, "Eğlence", "", ""));
        mWallItem.add(new WallItem(R.mipmap.ic_launcher, "Ders", "", ""));
        mWallItem.add(new WallItem(R.mipmap.ic_launcher, "Vasıta", "", ""));
        mWallItemAdapter = new WallItemAdapter(mWallItem,R.layout.list_item_favorites);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.wall_recyclerview);
        recyclerView.setHasFixedSize(true);//bunu silmeyi unutma
        GridLayoutManager glm = new GridLayoutManager(getActivity(),3);
        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(mWallItemAdapter);
        return rootView;
    }
}
