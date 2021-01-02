package com.geezerhiker.locationservice;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service {

    private static int Count = 0;

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location location;
            if(locationResult != null && (location = locationResult.getLastLocation()) != null) {
                // MARK: TODO - debug only next 3 lines
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                Log.d("LOCATION_UPDATE", (++Count) + " [" + latitude + ", " + longitude + "]");
                //
                Intent intent = new Intent(getString(R.string.locationBroadcastCode));
                intent.putExtra(getString(R.string.locationBroadcast), location);
                sendBroadcast(intent);

            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @SuppressLint("MissingPermission")
    private void startLocationService() {
        String channelId = "location_notification_channel";
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(),
                channelId
        );
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(getString(R.string.locationServiceTitle));
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("Running: [" + Constants.LONG_UPDATE_INTERVAL/1000 + " seconds; " + Constants.MINIMUM_DISPLACEMENT + " meters]");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(notificationManager != null
            && notificationManager.getNotificationChannel(channelId) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        channelId,
                        getString(R.string.locationServiceTitle),
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription(getString(R.string.notificationDescription));
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(Constants.LONG_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(Constants.SHORT_UPDATE_INTERVAL);
        locationRequest.setSmallestDisplacement(Constants.MINIMUM_DISPLACEMENT);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        startForeground(Constants.LOCATION_SERVICE_ID, builder.build());
    }

    private void stopLocationService() {
        LocationServices.getFusedLocationProviderClient(this)
                .removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null) {
            String action = intent.getAction();
            if(action != null) {
                if(action.equals(Constants.ACTION_START_LOCATION_SERVICE)) {
                    startLocationService();
                } else if(action.equals((Constants.ACTION_STOP_LOCATION_SERVICE))) {
                    stopLocationService();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
