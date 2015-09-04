package com.intern.tmob.activityextreme;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


public class WallItemAdapter extends RecyclerView.Adapter<WallItemAdapter.ViewHolder> {
    List<WallItem> wallItemList;
    int mLayoutId;
    boolean isWall,isPager;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView image;
        TextView name;
        TextView sent;
        TextView detail;
        TextView header;

        public ViewHolder(View itemView) {
            super(itemView);
            if(isPager){
                cardView = (CardView) itemView.findViewById(R.id.pager_item_cardview);
                image = (ImageView) itemView.findViewById(R.id.pager_item_image);
                name = (TextView) itemView.findViewById(R.id.pager_item_name);
                if (isWall) {
                    sent = (TextView) itemView.findViewById(R.id.pager_item_sent);
                    detail = (TextView) itemView.findViewById(R.id.pager_item_detail);
                    header = (TextView) itemView.findViewById(R.id.pager_item_header);
                }
            } else {
                cardView = (CardView) itemView.findViewById(R.id.list_item_cardview);
                image = (ImageView) itemView.findViewById(R.id.list_item_image);
                name = (TextView) itemView.findViewById(R.id.list_item_name);
                if (isWall) {
                    sent = (TextView) itemView.findViewById(R.id.list_item_sent);
                    detail = (TextView) itemView.findViewById(R.id.list_item_detail);
                    header = (TextView) itemView.findViewById(R.id.list_item_header);
                }
            }
        }
    }


    public WallItemAdapter(List<WallItem> wallItemList,int layouId){
        this.wallItemList = wallItemList;
        mLayoutId = layouId;
        isWall = (layouId == R.layout.list_item_wall || layouId == R.layout.pager_item_item);
        isPager = (layouId == R.layout.pager_item_item);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(mLayoutId,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(wallItemList.get(position).getname());
        Log.i("infoinfoinfo",position + " " + wallItemList.get(position).getImageLink());
        if(wallItemList.get(position).getimage()==-1)
            Glide.with(context).load(wallItemList.get(position).getImageLink())
                .into(holder.image);
        else
            Glide.with(context).load(wallItemList.get(position).getimage()).into(holder.image);
        if(isWall) {
            holder.detail.setText(wallItemList.get(position).getdetail());
            holder.sent.setText(wallItemList.get(position).getsent());
            holder.header.setText(wallItemList.get(position).getheader());
        }
    }

    @Override
    public int getItemCount() {
        return wallItemList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
