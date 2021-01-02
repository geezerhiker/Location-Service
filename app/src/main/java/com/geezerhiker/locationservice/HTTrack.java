package com.geezerhiker.locationservice;

import android.location.Location;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HTTrack {
    ArrayList<Location> trackpoints = new ArrayList<>();
    float length = 0.0F;
    @Nullable Location previousLocation = null;

    public boolean add(Location location) {
        trackpoints.add(location);
        if(previousLocation != null) {
            length += location.distanceTo(previousLocation);
        }
        previousLocation = location;
        return true;
    }
    private double distanceFrom(Location fromOne, Location toAnother) {
        return 1.23;
    }
    public Integer size() {
        return trackpoints.size();
    }
    public double getLength() {
        return length;
    }
}
