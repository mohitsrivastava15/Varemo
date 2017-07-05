package com.varemo.panel.helper;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mohit on 26/2/17.
 */
public class HouseViewCache implements Parcelable {
    public Map<String, Map<String, Boolean>> houseViewCache = new HashMap<>();

    public HouseViewCache() {
        if(this.houseViewCache == null)
            this.houseViewCache = new HashMap<>();
    }

    public void addRoomViewToHouseCache(String roomName, Map<String, Boolean> roomView) {
        if(roomView == null) {
            roomView = new HashMap<>();
        }
        houseViewCache.put(roomName, roomView);
    }

    public Map<String, Boolean> getRoomViewDetails(String roomName) {
        return this.houseViewCache.get(roomName);
    }

    public boolean editSwitchStateInRoomView(String roomName, String switchName, boolean value) {
        if(houseViewCache.get(roomName) == null) {
            return false;
        }
        Map<String, Boolean> roomView = houseViewCache.get(roomName);
        roomView.put(switchName, value);
        return true;
    }

    /**
     * Setup the parcelable functions
     */
    private static final long serialVersionUID = 1L;

    @Override
    public int describeContents() {
        return 0;
    }
    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(houseViewCache.size());
        for(Map.Entry<String, Map<String, Boolean>> entry : houseViewCache.entrySet()) {
            out.writeString(entry.getKey());

            Map<String, Boolean> roomView = entry.getValue();
            out.writeInt(roomView.size());
            /**
             * Write room's entries
             */
            for(Map.Entry<String, Boolean> roomEntry : roomView.entrySet()) {
                out.writeString(roomEntry.getKey());
                out.writeInt(roomEntry.getValue()==true? 1:0);
            }
        }
    }
    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<HouseViewCache> CREATOR = new Parcelable.Creator<HouseViewCache>() {
        public HouseViewCache createFromParcel(Parcel in) {
            return new HouseViewCache(in);
        }

        public HouseViewCache[] newArray(int size) {
            return new HouseViewCache[size];
        }
    };
    private HouseViewCache(Parcel in) {
        this.houseViewCache = new HashMap<>();
        int size = in.readInt();
        for(int i=0; i<size; i++) {
            String keyRoomName = in.readString();
            int roomButtonsSize = in.readInt();
            Map<String, Boolean> roomView = new HashMap<>();

            /**
             * Read room's values
             */
            for (int j = 0; j < roomButtonsSize; j++) {
                String buttonName = in.readString();
                Boolean buttonToggleValue = in.readInt() == 1 ? true : false;

                roomView.put(buttonName, buttonToggleValue);
            }
            houseViewCache.put(keyRoomName, roomView);
        }

    }

}
