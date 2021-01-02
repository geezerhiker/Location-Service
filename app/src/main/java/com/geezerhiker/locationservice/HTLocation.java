package com.geezerhiker.locationservice;

import android.location.Location;

public class HTLocation extends Location {

    public HTLocation(Location l) {
        super(l);
    }

    @Override
    public float distanceTo(Location dest) {
        return super.distanceTo(dest);
        //MARK: TODO - Implement haversine method (and dump super.)
    }
}
