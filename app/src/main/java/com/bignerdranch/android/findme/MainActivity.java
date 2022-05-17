package com.bignerdranch.android.findme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_COARSE_LOCATION},
                123);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        Button btnFind = findViewById(R.id.find_btn);
        btnFind.setOnClickListener(new Button.OnClickListener() {

            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);

                String messageText = showLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
                System.out.println(messageText);
                SmsManager sms = SmsManager.getDefault();
                ArrayList<String> parts = sms.divideMessage(messageText);
                sms.sendMultipartTextMessage("+79273527207", null, parts, null, null);
                //SmsManager.getDefault().sendTextMessage("+79273527207", null, messageText, null, null);
                System.out.println("jjhhghg");
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                0, 0, locationListener);
    }

    private LocationListener locationListener = new LocationListener() {

        @SuppressLint("MissingPermission")
        @Override
        public void onLocationChanged(Location location) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    0, 0, locationListener);
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
        String addressName = null;
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            if (addresses.size() > 0) {
                addressName = addresses.get(0).getAddressLine(0);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        return "Широта: "+location.getLatitude()+"\nДолгота: "+location.getLongitude()+"\n"+addressName;
    }
}

