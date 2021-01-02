package com.geezerhiker.locationservice;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private TextView tvCount, tvAccuracy, tvDistance;

    //MARK: model properties
    Integer counter = 0;
    HTTrack track;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.buttonStartLocationUpdates).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE
                    );
                } else {
                    startLocationService();
                }
            }
        });

        findViewById(R.id.buttonStopLocationUpdates).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationService();
            }
        });

        tvCount = findViewById(R.id.tvCount);
        tvAccuracy = findViewById(R.id.tvAccuracy);
        tvDistance = findViewById(R.id.tvdistance);
        track = new HTTrack();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length >0) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            } else {
                Toast.makeText(this, "Permission Denied!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if(activityManager != null) {
            for (ActivityManager.RunningServiceInfo service:
            activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if(LocationService.class.getName().equals(service.service.getClassName())) {
                    if(service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void startLocationService() {
        if(!isLocationServiceRunning()) {
            LocationBroadcastReceiver receiver = new LocationBroadcastReceiver();
            IntentFilter filter = new IntentFilter(getString(R.string.locationBroadcastCode));
            registerReceiver(receiver, filter);

            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this, "Location service has started", Toast.LENGTH_SHORT).show();

        }
    }

    private void stopLocationService() {
        if(isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this, "Location service has stopped", Toast.LENGTH_SHORT).show();
        }
    }

    public class LocationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(getString(R.string.locationBroadcastCode) )) {
                Location location = intent.getExtras().getParcelable(getString(R.string.locationBroadcast));
                track.add(location);
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                double accuracy = location.getAccuracy();
                Toast.makeText(MainActivity.this,
                        "[Latitude = " + latitude + "; Longitude = " + longitude
                                + "; Accuracy = " + accuracy + "]" + " Count = " + track.size(), Toast.LENGTH_LONG)
                        .show();
//                latitudeTextView.setText(String.valueOf(latitude));
//                longitudeTextView.setText(String.valueOf(longitude));
                tvCount.setText(String.valueOf(++counter));
                tvAccuracy.setText(String.valueOf(location.getAccuracy()));
                String value = String.valueOf(track.getLength());
                if(value.length() > 5) value = value.substring(0, 5);
                tvDistance.setText(value);
//                activeTrack.add(location);
            }
        }
    }
}