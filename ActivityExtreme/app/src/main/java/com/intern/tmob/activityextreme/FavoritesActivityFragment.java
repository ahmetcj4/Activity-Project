package com.intern.tmob.activityextreme;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.flipboard.bottomsheet.BottomSheetLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class FavoritesActivityFragment extends Fragment {
    List<WallItem> mWallItem = new ArrayList<>();
    Boolean[] mSelectedItems;
    RecyclerView recyclerView;
    WallItemAdapter mWallItemAdapter;
    private String favorites = "Favorites";

    public FavoritesActivityFragment() {
        mWallItem.add(new WallItem(R.mipmap.ic_launcher, "Spor"));
        mWallItem.add(new WallItem(R.mipmap.ic_launcher, "Kültür Sanat"));
        mWallItem.add(new WallItem(R.mipmap.ic_launcher, "Gezi"));
        mWallItem.add(new WallItem(R.mipmap.ic_launcher, "Eğlence"));
        mWallItem.add(new WallItem(R.mipmap.ic_launcher, "Ders"));
        mWallItem.add(new WallItem(R.mipmap.ic_launcher, "Vasıta"));
        mSelectedItems = new Boolean[mWallItem.size()];
        mWallItemAdapter = new WallItemAdapter(mWallItem,R.layout.list_item_favorites);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return getFavoritesView(inflater, container,getContext());
    }

    public View getFavoritesView(LayoutInflater inflater, final ViewGroup container, final Context context) {
        final View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.wall_recyclerview);
        recyclerView.setHasFixedSize(true);//bunu silmeyi unutma
        GridLayoutManager glm = new GridLayoutManager(getActivity(),3);
        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(mWallItemAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        selectItem(v.isSelected(), position);
                    }
                })
        );
        FloatingActionButton floatingActionButton = (FloatingActionButton)rootView.findViewById(R.id.continue_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor sPEditor = context.getSharedPreferences(favorites, Context.MODE_PRIVATE).edit();
                sPEditor.putBoolean("spor", mSelectedItems[0]);
                sPEditor.putBoolean("kultur", mSelectedItems[1]);
                sPEditor.putBoolean("gezi", mSelectedItems[2]);
                sPEditor.putBoolean("eglence", mSelectedItems[3]);
                sPEditor.putBoolean("ders", mSelectedItems[4]);
                sPEditor.putBoolean("vasıta", mSelectedItems[5]);
                sPEditor.commit();
                if(SplashActivityFragment.bottomSheet.getState().equals(BottomSheetLayout.State.HIDDEN)){
                    getActivity().onBackPressed();
                    getActivity().finish();
                }else{
                    SplashActivityFragment.bottomSheet.dismissSheet();
                }

            }
        });

        ViewTreeObserver observer = rootView .getViewTreeObserver();

        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                restoreSharedPreferences(context);
            }
        });
        return rootView;
    }


    private void selectItem(boolean selected, int position) {
        View v = recyclerView.getChildAt(position);
        if(v!=null)
            if (selected) {
                mSelectedItems[position] = false;
                v.setSelected(false);
                v.setBackgroundResource(R.color.indigo_500);
            } else {
                mSelectedItems[position] = true;
                v.setSelected(true);
                v.setBackgroundResource(R.color.green);
            }

    }

    private void restoreSharedPreferences(Context context) {
        SharedPreferences settings = context.getSharedPreferences(favorites,Context.MODE_PRIVATE);
        mSelectedItems[0] = settings.getBoolean("spor", false);
        mSelectedItems[1] = settings.getBoolean("kultur", false);
        mSelectedItems[2] = settings.getBoolean("gezi", false);
        mSelectedItems[3] = settings.getBoolean("eglence", false);
        mSelectedItems[4] = settings.getBoolean("ders", false);
        mSelectedItems[5] = settings.getBoolean("vasıta", false);
        for(int i = 0; i<mWallItem.size();i++){
            //TODO bu hatayı düzelt

            selectItem(!mSelectedItems[i],i);
        }
    }

}
