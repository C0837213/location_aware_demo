package com.example.location_aware_app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    LocationListener locationListener;
    LocationManager locationManager;
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        geocoder = new Geocoder(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        TextView lat = findViewById(R.id.lat);
        TextView longitude = findViewById(R.id.myLong);
        TextView alt = findViewById(R.id.alt);
        TextView acc = findViewById(R.id.acc);
        TextView add = findViewById(R.id.add);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latV = location.getLatitude();
                double longitudeV = location.getLongitude();
                lat.setText(String.format("%.3f", latV));
                longitude.setText(String.format("%.3f", longitudeV));
                alt.setText(String.format("%.3f", location.getAltitude()));
                acc.setText(String.format("%.3f", location.getAccuracy()));
                try {
                    add.setText(getAddress(latV, longitudeV));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    private String getAddress(double lat, double longitude) throws IOException {
        List<Address> addresses = geocoder.getFromLocation(lat, longitude, 1);
        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        return String.format("%s, %s, %s, %s, %s, %s",
                address, city, state, country, postalCode, knownName);
    }
}