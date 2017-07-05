package com.varemo.panel.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.varemo.R;
import com.varemo.panel.activity.DefaultRoomActivity;
import com.varemo.panel.activity.HouseDashboardActivity;
import com.varemo.panel.helper.PrefManager;
import com.varemo.panel.model.RoomViewItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohit on 24/2/17.
 */
public class RoomViewRecyclerAdapter extends RecyclerView.Adapter<RoomViewRecyclerAdapter.CardHolder> {

    private List<RoomViewItem> roomViewItemList = new ArrayList<>();
    private Context context;
    private static int selectedPos = -1;

    public RoomViewRecyclerAdapter(Context ctx, List<RoomViewItem> list) {
        this.context = ctx;
        this.roomViewItemList = list;
    }

    @Override
    public RoomViewRecyclerAdapter.CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card, parent, false);
        return new CardHolder(view);
    }

    @Override
    public void onBindViewHolder(CardHolder holder, final int position) {
        RoomViewItem roomView = roomViewItemList.get(position);
        Glide.with(context).load(roomView.getImgUrl())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.mItemImage);

        holder.mItemTopDescription.setText(roomView.getImgDescription());
        holder.itemView.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                launchDefaultRoomActivity(position);
            }
        });
        holder.mItemImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                launchDefaultRoomActivity(position);
            }
        });
    }

    private void launchDefaultRoomActivity(int position) {
        Toast.makeText(context, "Item="+position+" clicked!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, DefaultRoomActivity.class);

        intent.putExtra(PrefManager.ROOM_PHOTO_KEY, roomViewItemList.get(position).getImgUrl());
        intent.putExtra(PrefManager.ROOM_TITLE, roomViewItemList.get(position).getImgDescription());
        intent.putExtra(PrefManager.ROOM_CONTENT_XML, roomViewItemList.get(position).getXmlContent());

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return roomViewItemList.size();
    }

    public static class CardHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView mItemImage;
        private TextView mItemTopDescription;
        public CardHolder(View itemView) {
            super(itemView);
            mItemImage = (ImageView) itemView.findViewById(R.id.imgView_thumbnail);
            mItemTopDescription = (TextView) itemView.findViewById(R.id.txtView_roomTitle);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
