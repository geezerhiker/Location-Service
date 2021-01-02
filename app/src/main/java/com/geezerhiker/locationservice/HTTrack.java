package com.geezerhiker.locationservice;

import android.location.Location;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HTTrack {
    ArrayList<HTLocation> trackpoints = new ArrayList<>();
    float length = 0.0F;
    @Nullable HTLocation previousLocation = null;

    public boolean add(Location location) {
        HTLocation newLocation = new HTLocation(location);
        trackpoints.add(newLocation);
        if(previousLocation != null) {
            length += newLocation.distanceTo(previousLocation);
        }
        previousLocation = newLocation;
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
