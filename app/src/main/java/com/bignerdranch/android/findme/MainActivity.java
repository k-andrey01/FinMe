package com.bignerdranch.android.findme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    StringBuilder sbGPS = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        Button btnFind = findViewById(R.id.find_btn);
        btnFind.setOnClickListener(new Button.OnClickListener(){
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10, locationListener);

                String messageText = showLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
                System.out.println(messageText);
                SmsManager.getDefault()
                        .sendTextMessage("+79273527207", null, messageText, null, null);
            }
        });
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private String showLocation(Location location) {
        return String.format(
                "Широта: %1$.4f\nДолгота: %2$.4f\nВремя: %3$tF %3$tT",
                location.getLatitude(), location.getLongitude(), new Date(
                        location.getTime()));
    }
}