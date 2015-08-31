package com.intern.tmob.activityextreme;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class WallItemAdapter extends RecyclerView.Adapter<WallItemAdapter.ViewHolder> {
    List<WallItem> wallItemList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView image;
        TextView name;
        TextView sent;
        TextView detail;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.list_item_cardview);
            image = (ImageView) itemView.findViewById(R.id.list_item_image);
            name = (TextView) itemView.findViewById(R.id.list_item_name);
            sent = (TextView) itemView.findViewById(R.id.list_item_sent);
            detail = (TextView) itemView.findViewById(R.id.list_item_detail);
        }
    }


    public WallItemAdapter(List<WallItem> wallItemList){
        this.wallItemList = wallItemList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_wall,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(wallItemList.get(position).getname());
        holder.detail.setText(wallItemList.get(position).getdetail());
        holder.sent.setText(wallItemList.get(position).getsent());
        holder.image.setImageResource(wallItemList.get(position).getimage());
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
