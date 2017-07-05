package com.varemo.panel.activity;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.cleveroad.fanlayoutmanager.FanLayoutManager;
import com.cleveroad.fanlayoutmanager.FanLayoutManagerSettings;
import com.varemo.R;
import com.varemo.panel.adapters.RoomViewRecyclerAdapter;
import com.varemo.panel.model.RoomViewItem;

import java.util.ArrayList;
import java.util.List;

public class HouseDashboardActivity extends AppCompatActivity {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RoomViewRecyclerAdapter mAdapter;
    private TextView mVaremo;

    private List<RoomViewItem> roomList;

    private FanLayoutManager fanLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_dashboard);

        mContext = getApplicationContext();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mVaremo = (TextView) findViewById(R.id.txtView_varemo);

        /**
         * Instantiate the linear layout manager
         */
        mLinearLayoutManager = new LinearLayoutManager(this);

        FanLayoutManagerSettings fanLayoutManagerSettings = FanLayoutManagerSettings
                .newBuilder(getApplicationContext())
                .withFanRadius(true)
                .withAngleItemBounce(0)
                .withViewHeightDp(360)
                .withViewWidthDp(240)
                .build();

        fanLayoutManager = new FanLayoutManager(getApplicationContext(), fanLayoutManagerSettings);
        mRecyclerView.setLayoutManager(fanLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        roomList = new ArrayList<>();
        roomList.add(new RoomViewItem("https://s-media-cache-ak0.pinimg.com/736x/6b/8a/bc/6b8abc723bfbe4d6ad3d02260a24d72a.jpg",
                "Master Bedroom"));
        roomList.add(new RoomViewItem("http://www.ikea.com/gb/en/images/rooms/ikea-made-for-long-dinner-parties-and-quick-catch-ups__1364316015294-s4.jpg",
                "Dining Room"));
        roomList.add(new RoomViewItem("http://www.artsclubofwashington.org/wp-content/uploads/2011/03/ACW-Guest-Room-2.jpg",
                "Guest Room"));
        roomList.add(new RoomViewItem("http://housely.com/wp-content/uploads/2016/04/Springy-Colorful-Modern-Kitchen-Decorating-Ideas_15.jpg",
                "Kitchen"));
        roomList.add(new RoomViewItem("http://www.3rdarch.com/img/gallery/home/drawing/3da-drawing_room-004.jpg",
                "Drawing Room"));
        roomList.add(new RoomViewItem("http://wall-ah.com/media/catalog/product/cache/1/thumbnail/9df78eab33525d08d6e5fb8d27136e95/j/e/jetsons-spacecrafts-wall-decals-r2.jpg",
                "Demo Room", "activity_demo_room"));

        mAdapter = new RoomViewRecyclerAdapter(getApplicationContext(), roomList);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();

        /**
         * Set varemo text
         */
        String text = "<font color=#a2c845>va</font><font color=#2a3495>remo</font>";
        mVaremo.setText(Html.fromHtml(text));

        setupFanAdapterForClicks();
    }

    private void setupFanAdapterForClicks() {
    }
}
