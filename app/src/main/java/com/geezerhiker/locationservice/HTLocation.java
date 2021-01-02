package com.geezerhiker.locationservice;

import android.location.Location;

public class HTLocation extends Location {

    public HTLocation(Location l) {
        super(l);
    }

    @Override
    public float distanceTo(Location dest) {
        //return super.distanceTo(dest);
        return 1.234F;
    }
}
