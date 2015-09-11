package com.intern.tmob.activityextreme;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mustafa.myapplication.backend.myApi.MyApi;
import com.example.mustafa.myapplication.backend.myApi.model.Entity;
import com.example.mustafa.myapplication.backend.myApi.model.EntityCollection;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TabPagerAdapter extends PagerAdapter {
    String[] mTitles;
    static String acomment="";
    Context mContext;
    List<WallItem> mWallItem = new ArrayList<>();
    RecyclerView recyclerView;
    WallItemAdapter mWallItemAdapter;
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
        final View view = LayoutInflater.from(mContext).inflate(R.layout.pager_item,
                container, false);
        container.addView(view);
        mWallItem.clear();
        new FetchCommentUserTask().execute();

        mWallItemAdapter = new WallItemAdapter(mWallItem,R.layout.pager_item_item);

        recyclerView = (RecyclerView) view.findViewById(R.id.pager_recyclerview);
        recyclerView.setHasFixedSize(true);//bunu silmeyi unutma
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(llm);

        recyclerView.setAdapter(mWallItemAdapter);

        Button button = (Button) view.findViewById(R.id.addComment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText comment = (EditText) view.findViewById(R.id.profile_comment);
                acomment = comment.getText().toString();
                comment.setText("");
                new CommentUserTask().execute();
            }
        });

        new FetchCommentUserTask().execute();
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
                EntityCollection x = myApiService.getCommentsUser(fid).execute();
                for(int i=0;i<x.getItems().size();i++)
                    list.add(x.getItems().get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.i("fetchCommentUserTask", String.valueOf(list.size()));
            return list;
        }

        @Override
        protected void onPostExecute(List<Entity> entities) {
            mWallItem.clear();
            for(Entity e : entities){
                mWallItem.add(new WallItem((String)e.getProperties().get("ppUrl")
                        , (String)e.getProperties().get("name")+" "+(String)e.getProperties().get("surname"),
                        (String)e.getProperties().get("date")+" "+(String)e.getProperties().get("time"),
                        (String) e.getProperties().get("comment"), " ",
                        (String) e.getProperties().get("commenterID")));
            }
            mWallItemAdapter.notifyDataSetChanged();
        }
    }

    class CommentUserTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            if(myApiService == null){
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://absolute-disk-105007.appspot.com/_ah/api/");
                myApiService = builder.build();
            }
            try {
                SharedPreferences settings = mContext.getSharedPreferences("SplashActivityFragment", Context.MODE_PRIVATE);
                myApiService.commentUser(fid, SplashActivityFragment.mProfile.getId(),acomment,
                        SplashActivityFragment.mProfile.getProfilePictureUri(200,200).toString(),
                        settings.getString("location", "def"),
                        "21-12-2015","21:30",SplashActivityFragment.mProfile.getFirstName(),
                        SplashActivityFragment.mProfile.getLastName()).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
