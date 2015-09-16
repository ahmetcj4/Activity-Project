package com.intern.tmob.activityextreme;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
public class DetailActivityFragment extends Fragment {
    boolean attended = false;
    private static MyApi myApiService = null;
    WallItem activity;
    String location;
    ImageView cover;
    ImageView image;
    TextView name,date,detail,header;
    View rootView;
    Button attendButton;
    List<WallItem> mWallItem;
    WallItemAdapter mWallItemAdapter;
    String acomment,fid;
    RecyclerView recyclerView;
    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        activity = (WallItem) getActivity().getIntent().getSerializableExtra("object");
        location = getActivity().getIntent().getStringExtra("location");
        fid = activity.getFid();
        ((CollapsingToolbarLayout) rootView.findViewById(R.id.detail_collapsing_toolbar_layout)).setTitle(" ");//activity.getheader());
//invisible olsun sonra animation ile visible olsun.
        findAndFill();
        return rootView;
    }

    private void findAndFill(){
        name = (TextView) rootView.findViewById(R.id.detail_name);
        date = (TextView) rootView.findViewById(R.id.detail_date);
        detail = (TextView) rootView.findViewById(R.id.detail_details);
        header = (TextView) rootView.findViewById(R.id.detail_header);
        image = (ImageView) rootView.findViewById(R.id.detail_pp);
        cover = (ImageView) rootView.findViewById(R.id.detail_cover);
        int coverDrawable = -1;
        switch (activity.getheader().charAt(0)){
            case 'S':
                coverDrawable =  R.drawable.spor;
                break;
            case 'K':
                coverDrawable =  R.drawable.kultur_sanat;
                break;
            case 'G':
                coverDrawable = R.drawable.gezi;
                break;
            case 'E':
                coverDrawable = R.drawable.eglence;
                break;
            case 'D':
                coverDrawable = R.drawable.ders;
                break;
            case 'A':
                coverDrawable =  R.drawable.arac;
                break;
        }
        Glide.with(getContext()).load(coverDrawable).placeholder(R.color.placeholder).into(cover);
        Glide.with(getContext()).load(activity.getImageLink()).into(image);
        header.setText(activity.getheader());
        name.setText(activity.getname());
        date.setText(activity.getsent());
        detail.setText(activity.getdetail());

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();
            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();
            }
        });
        mWallItem = new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.detail_activity_recyclerview);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);

        mWallItemAdapter = new WallItemAdapter(mWallItem,R.layout.pager_comment_item);
        recyclerView.setAdapter(mWallItemAdapter);
        Button commentButton = (Button) rootView.findViewById(R.id.addComment);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText comment = (EditText) rootView.findViewById(R.id.activity_comment);
                acomment = comment.getText().toString();
                comment.setText("");
                new CommentActivityTask().execute();
            }
        });
        attendButton = (Button) rootView.findViewById(R.id.attendButton);
        attendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attended);//TODO unattend
                else{
                    new AttendActivityTask().execute();
                }
            }
        });
        new GetCommentActivityTask().execute();
        new whoAttendedActivityTask().execute();
    }
    private void openProfile() {
        Intent intent =new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra("fid", activity.getFid());
        intent.putExtra("object", activity);
        intent.putExtra("location",location);
        startActivity(intent);
    }

    class CommentActivityTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            /*
                fid : the guy's id who creates activity
                date: date of activity
                time: time of activity
                commenterID: id of commenter
                comment: comment message
            */
            if(myApiService == null){
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://absolute-disk-105007.appspot.com/_ah/api/");
                myApiService = builder.build();
            }
            try {
                Log.i("commentActivityTask", "girdi");
                //myApiService.commentActivity("707265706085188","2015.09.09-13:40","707265706085188","Beyler bu ikinci yorum").execute();
                Log.i("commentActivityTask",fid+" " + activity.getsent()+ " " +SplashActivityFragment.mProfile.getId() + " " + acomment);
                myApiService.commentActivity(fid, activity.getsent(), SplashActivityFragment.mProfile.getId(), acomment).execute();
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("commentActivityTask","Error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new GetCommentActivityTask().execute();
        }
    }
    class AttendActivityTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            /*
                fid : the guy's id who creates activity
                activityId : id of activity
            */
            if(myApiService == null){
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://absolute-disk-105007.appspot.com/_ah/api/");
                myApiService = builder.build();
            }
            try {
                Log.i("AttendActivityTask", "girdi");
                //myApiService.commentActivity("707265706085188","2015.09.09-13:40","707265706085188","Beyler bu ikinci yorum").execute();
                Log.i("AttendActivityTask", fid + " " + activity.getsent() + " " + SplashActivityFragment.mProfile.getId() + " " + acomment);
                myApiService.attendActivity(fid + "_" + activity.getsent(), SplashActivityFragment.mProfile.getId()).execute();
                attended = true;
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("AttendActivityTask","Error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(attended) {
                attendButton.setTextColor(getActivity().getResources().getColor(R.color.primary_dark));
                attendButton.setText("Katıldın");
            }
        }
    }

    class GetCommentActivityTask extends AsyncTask<Void,Void,List<Entity>> {
        @Override
        protected List<Entity> doInBackground(Void... params) {
            if(myApiService == null){
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://absolute-disk-105007.appspot.com/_ah/api/");
                myApiService = builder.build();
            }
            List<Entity> list = new ArrayList<>();
            try {
                EntityCollection x = myApiService.getCommentsActivity(fid, activity.getsent()).execute();
                Log.i("commentActivityTask",fid+ " " + activity.getsent());
                list = x.getItems();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<Entity> entities) {
            mWallItem.clear();

            if(entities!=null)
                for(Entity e : entities){
                    mWallItem.add(new WallItem((String)e.getProperties().get("ppUrl"),
                            (String)e.getProperties().get("name")+" "+(String)e.getProperties().get("surname"),
                            (String) e.getProperties().get("dateTime"),
                            (String) e.getProperties().get("comment"), " ",
                            (String) e.getProperties().get("commenterID")));
                 //   recyclerView.getLayoutParams().height += recyclerView.getChildAt(recyclerView.getChildCount()-1).getHeight();
                }
            mWallItemAdapter.notifyDataSetChanged();
            recyclerView.getLayoutParams().height = mWallItemAdapter.getItemCount() * 182;
        }
    }
    class whoAttendedActivityTask extends AsyncTask<Void,Void,List<Entity>> {
        @Override
        protected List<Entity> doInBackground(Void... params) {
            if(myApiService == null){
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://absolute-disk-105007.appspot.com/_ah/api/");
                myApiService = builder.build();
            }
            List<Entity> list = new ArrayList<>();

            try {
                EntityCollection x = myApiService.whoAttends(fid + "_" + activity.getsent()).execute();
                Log.i("whoAttendedActivityTask",fid+ "_" + activity.getsent());
                list = x.getItems();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<Entity> entities) {
            if(entities!=null)
                for(Entity e : entities){

//                    mWallItem.add(new WallItem((String)e.getProperties().get("ppUrl"),
//                            (String)e.getProperties().get("name")+" "+(String)e.getProperties().get("surname"),
//                            " ", (String) e.getProperties().get("comment"), " ",
//                            (String) e.getProperties().get("commenterID")));
                    if(SplashActivityFragment.mProfile.getId().equals((String)e.getProperties().get("ID"))) {
                        attendButton.setTextColor(getActivity().getResources().getColor(R.color.primary_dark));
                        attendButton.setText("Katıldın");
                    }
                }
        }
    }


//    class LikeUnlikeActivity extends AsyncTask<Void,Void,Void>{
//
//        /*
//            fid : the guy's id who creates activity
//            date: date of activity
//            time: time of activity
//            commenterID: id of commenter
//         */
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            if(myApiService == null){
//                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
//                        .setRootUrl("https://absolute-disk-105007.appspot.com/_ah/api/");
//                myApiService = builder.build();
//            }
//            try {
//                /*if(myApiService.isLiked("123","2015.09.09 14:14","123").execute()==null)*/
//                if(myApiService.isLiked(fid,date + " " + time,commenterID).execute() == null)
//                    Log.i("likeUnlike","not liked");
//                else
//                    Log.i("likeUnlike","liked");
//                /*myApiService.likeUnlikeActivity("123","2015.09.09 14:14","123").execute();*/
//                myApiService.likeUnlikeActivity(fid,date + " " + time,commenterID).execute();
//                Log.i("likeUnlike","liked");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }
}
