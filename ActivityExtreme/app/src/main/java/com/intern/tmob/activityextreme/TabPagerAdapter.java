package com.intern.tmob.activityextreme;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mustafa.myapplication.backend.myApi.MyApi;
import com.example.mustafa.myapplication.backend.myApi.model.Entity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.util.ArrayList;
import java.util.List;

public class TabPagerAdapter extends PagerAdapter {
    String[] mTitles;
    Context mContext;
    List<WallItem> mWallItem = new ArrayList<>();
    RecyclerView recyclerView;
    WallItemAdapter mWallItemAdapter;
    SwipeRefreshLayout srl;
    String fid;
    private static MyApi myApiService = null;

    TabPagerAdapter(Context c, String[] titles,String fid){
        mContext = c;
        mTitles = titles;
        this.fid = fid;
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }


    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o == view;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pager_item,
                container, false);
        container.addView(view);
        srl = (SwipeRefreshLayout) view.findViewById(R.id.pager_refresh);
        mWallItem.clear();
        mWallItem.add(new WallItem("https://graph.facebook.com/10207225423855332/picture?height=100&width=100&migration_overrides=%7Boctober_2012%3Atrue%7D"
                , "Lukas Podolski", "21-3-2015", "Adamdir bu adam.", "Cok onemli", "1232143214"));
        mWallItem.add(new WallItem("https://graph.facebook.com/10207225423855332/picture?height=100&width=100&migration_overrides=%7Boctober_2012%3Atrue%7D"
                ,"Lukas Podolski","21-3-2015","Adamdir bu adam.","Cok onemli","1232143214"));
        mWallItemAdapter = new WallItemAdapter(mWallItem,R.layout.pager_item_item);

        recyclerView = (RecyclerView) view.findViewById(R.id.pager_recyclerview);
        recyclerView.setHasFixedSize(true);//bunu silmeyi unutma
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(llm);

        recyclerView.setAdapter(mWallItemAdapter);
        mWallItemAdapter.notifyDataSetChanged();
        srl.setRefreshing(false);

        return view;
    }

    class FetchCommentUserTask extends AsyncTask<Void,Void,List<Entity>> {

        @Override
        protected List<Entity> doInBackground(Void... params) {
            if(myApiService == null){
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://absolute-disk-105007.appspot.com/_ah/api/");
                myApiService = builder.build();
            }
            List<Entity> list = new ArrayList<>();
            try {
                for (int i = 0; i < 20; i++) {
                    Entity e = myApiService.getCommentsUser(fid, i).execute();
                    if(e == null)
                        break;
                    else
                        list.add(e);
                }
            }catch (Exception e){
                Log.i("exception", e.toString());
            }
            Log.i("fetchCommentUserTask", String.valueOf(list.size()));
            return list;
        }

        @Override
        protected void onPostExecute(List<Entity> entities) {
            for(Entity e : entities){
                Log.i("fetchCommentUserTask", (String) e.getProperties().get("comment"));
                mWallItem.add(new WallItem("https://graph.facebook.com/10207225423855332/picture?height=100&width=100&migration_overrides=%7Boctober_2012%3Atrue%7D"
                        , "Lukas Podolski", "21-3-2015", (String) e.getProperties().get("comment"), "Cok onemli",
                        (String) e.getProperties().get("commenterID")));
            }
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
