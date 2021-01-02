package com.geezerhiker.locationservice;

class Constants {
    static final int LOCATION_SERVICE_ID = 175;
    static final String ACTION_START_LOCATION_SERVICE = "startLocationService";
    static final String ACTION_STOP_LOCATION_SERVICE = "stopLocationService";
    static final Long LONG_UPDATE_INTERVAL = 3000L;
    static final Long SHORT_UPDATE_INTERVAL = LONG_UPDATE_INTERVAL / 2;
    static final float MINIMUM_DISPLACEMENT = 10.0F;
}
