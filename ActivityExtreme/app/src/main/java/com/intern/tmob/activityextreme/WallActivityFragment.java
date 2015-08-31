package com.intern.tmob.activityextreme;

import android.support.v4.app.Fragment;
import android.os.Bundle;
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
public class WallActivityFragment extends Fragment {

    List<WallItem> mWallItem = new ArrayList<>();
    RecyclerView recyclerView;
    WallItemAdapter mWallItemAdapter;
    public WallActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wall, container, false);

        mWallItem.add(new WallItem(R.mipmap.ic_launcher, "Lukas Podolski","1 saat once","az ornek"));
        mWallItem.add(new WallItem(R.mipmap.ic_launcher, "Ahmet Zorer","2 saat once","ornek yazi"));
        mWallItem.add(new WallItem(R.mipmap.ic_launcher, "Mustafa Erdogan","3 saat once","ornek detay"));
        mWallItem.add(new WallItem(R.mipmap.ic_launcher, "Lukas Podolski","4 saat once","ornek"));
        mWallItem.add(new WallItem(R.mipmap.ic_launcher, "Mehmet Ozdemir","5 saat once","ornek detay"));
        mWallItemAdapter = new WallItemAdapter(mWallItem);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.wall_recyclerview);
        recyclerView.setHasFixedSize(true);//bunu silmeyi unutma
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        recyclerView.setAdapter(mWallItemAdapter);


        return rootView;
    }
}
