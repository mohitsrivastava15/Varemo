package com.varemo.panel.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.SwitchCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.iconics.view.IconicsImageView;
import com.varemo.R;
import com.varemo.panel.helper.ComplexPreferences;
import com.varemo.panel.helper.HouseViewCache;
import com.varemo.panel.helper.PrefManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DefaultRoomActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String roomImgUrl;
    private String roomTitle;

    private ImageView mRoomImageView;
    private TextClock mTime;
    private TextView mRoomHeading;
    private SwitchCompat mBtnToggleSwitch;
    private TextView mDay;

    private Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_default_room);
        setContentView(getIDForLayout(getIntent().getStringExtra(PrefManager.ROOM_CONTENT_XML)));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.appContext = getApplicationContext();

        this.roomImgUrl = getIntent().getStringExtra(PrefManager.ROOM_PHOTO_KEY);
        this.roomTitle = getIntent().getStringExtra(PrefManager.ROOM_TITLE);

        instantiateXmlElements();
        getRoomViewDetailsFromComplexPreferences();

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        /**
         * Set the image on top
         */
        Glide.with(appContext).load(this.roomImgUrl)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mRoomImageView);


        setCurrentDay();
        setupRoomHeading();
        setupIconicsViewSwitches();

        //IconicsImageView test = (IconicsImageView) findViewById(R.id.icon_light1);
        //test.setColor(getResources().getColor(R.color.colorAccent));
    }

    public int getIDForLayout(String xmlFileString) {
        return getResources().getIdentifier(xmlFileString, "layout", getPackageName());

    }

    public void recursiveLoopChildren(ViewGroup parent) {
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            final View child = parent.getChildAt(i);
            if (child instanceof ViewGroup) {

                recursiveLoopChildren((ViewGroup) child);
                // DO SOMETHING WITH VIEWGROUP, AFTER CHILDREN HAS BEEN LOOPED
            } else if(child instanceof IconicsImageView) {
                //validate your EditText here
                final IconicsImageView toggleSwitch = (IconicsImageView) child;
                toggleSwitch.setClickable(true);
                final String iconId = getResources().getResourceEntryName(toggleSwitch.getId());
                if(roomView.get(iconId) == null) {
                    roomView.put(iconId, false);
                }
                if(roomView.get(iconId) == true) {
                    toggleSwitch.setColor(getResources().getColor(R.color.colorAccent));

                } else {
                    toggleSwitch.setColor(getResources().getColor(R.color.greyDark));
                }
                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String icon = v.getResources().getResourceEntryName(v.getId());
                        String url = "http://192.168.1.12:8090/kitchen";
                        String relayId = "10";
                        if(iconId.split("__").length > 1) {
                            relayId = iconId.split("__")[1];
                        }

                        if(roomView.get(icon) == false) {
                            /**
                             * Call the http get URL of the raspberry pi
                             * If success (response contains string "request was"), do all the following
                             */
                            url = url + "/" + relayId + "/1";
                            RequestQueue queue = Volley.newRequestQueue(appContext);
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            // Display the first 500 characters of the response string.
                                            toggleSwitch.setColor(getResources().getColor(R.color.colorAccent));
                                            roomView.put(icon, true);
                                            saveToComplexPreferences();
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    int test = 1;

                                }
                            });
// Add the request to the RequestQueue.
                            queue.add(stringRequest);

                        } else {

                            /**
                             * Call the http get URL of the raspberry pi
                             * If success (response contains string "request was"), do all the following
                             */
                            url = url + "/" + relayId + "/0";
                            RequestQueue queue = Volley.newRequestQueue(appContext);
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            // Display the first 500 characters of the response string.
                                            toggleSwitch.setColor(getResources().getColor(R.color.greyDark));
                                            roomView.put(icon, false);
                                            saveToComplexPreferences();
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
// Add the request to the RequestQueue.
                            queue.add(stringRequest);

                        }
                    }
                });
            } else {
                if (child != null) {
                    // DO SOMETHING WITH VIEW
                }
            }
        }
    }

    private void setupIconicsViewSwitches() {
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.relativeLayout_Parent);
        recursiveLoopChildren(layout);
    }

    private void setupRoomHeading() {
        this.mRoomHeading.setText(this.roomTitle);
    }

    private void setCurrentDay() {
        String daysArray[] = {"Sun","Mon","Tue", "Wed","Thu","Fri", "Sat"};

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String formattedDate = daysArray[day-1];
        SimpleDateFormat df = new SimpleDateFormat("dd MMM");
        String date = df.format(calendar.getTime());
        formattedDate +=" | "+date;
        this.mDay.setText(formattedDate);
    }


    private void instantiateXmlElements() {
        this.mRoomImageView = (ImageView) findViewById(R.id.imgView_defaultRoomImage);
        this.mRoomImageView.setBackgroundColor(Color.parseColor("#dd000000"));
        this.mRoomImageView.setColorFilter(Color.parseColor("#33ffffff"));
        this.mTime = (TextClock) findViewById(R.id.txtClock_time);
        this.mTime.setFormat12Hour("KK:mm a");

        this.mRoomHeading = (TextView) findViewById(R.id.txtView_roomHeading);
        this.mBtnToggleSwitch = (SwitchCompat) findViewById(R.id.btn_roomToggle);
        this.mDay = (TextView) findViewById(R.id.txtView_day);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.default_room, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private HouseViewCache houseViewCache;
    private Map<String, Boolean> roomView;
    private ComplexPreferences complexPreferences;

    public void getRoomViewDetailsFromComplexPreferences() {
        complexPreferences = ComplexPreferences.getComplexPreferences(appContext, "", 0);
        this.houseViewCache = complexPreferences.getObject(PrefManager.HOUSE_VIEW_CACHE, HouseViewCache.class);

        if(this.houseViewCache == null) {
            this.houseViewCache = new HouseViewCache();
            complexPreferences.putObject(PrefManager.HOUSE_VIEW_CACHE, this.houseViewCache);
        }

        if(this.houseViewCache.getRoomViewDetails(roomTitle) == null) {
            roomView = new HashMap<>();
            this.houseViewCache.addRoomViewToHouseCache(roomTitle, roomView);
        } else {
            this.roomView = this.houseViewCache.getRoomViewDetails(roomTitle);
        }
    }

    private void saveToComplexPreferences() {
        houseViewCache.addRoomViewToHouseCache(roomTitle, roomView);
        complexPreferences.putObject(PrefManager.HOUSE_VIEW_CACHE, houseViewCache);
        complexPreferences.commit();
    }
}
