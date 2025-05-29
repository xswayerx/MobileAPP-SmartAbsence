package com.example.profattendaceemsi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST = 100;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng emsi = new LatLng(33.58931956959172, -7.605327086230095);
        Marker emsiMarker = mMap.addMarker(new MarkerOptions()
                .position(emsi)
                .title("Marqueur à emsi centre"));
        LatLng emsiCentre = new LatLng(33.58931956959172, -7.605327086230895);
        Marker markerCentre = mMap.addMarker(new MarkerOptions().position(emsiCentre).title("Marqueur à EMSI Centre"));
        markerCentre.setTag("centre");

        LatLng emsiMaarif = new LatLng(33.582504, -7.628001);
        Marker markerMaarif = mMap.addMarker(new MarkerOptions().position(emsiMaarif).title("Marqueur à EMSI Maârif"));
        markerMaarif.setTag("maarif");

        LatLng emsiRoudani = new LatLng(33.579896, -7.634926);
        Marker markerRoudani = mMap.addMarker(new MarkerOptions().position(emsiRoudani).title("Marqueur à EMSI Roudani"));
        markerRoudani.setTag("roudani");

        LatLng emsiLesOranges = new LatLng(33.573135, -7.589389);
        Marker markerLesOranges = mMap.addMarker(new MarkerOptions().position(emsiLesOranges).title("Marqueur à EMSI Les Oranges"));
        markerLesOranges.setTag("les_oranges");

        LatLng emsiMoulayYoussef = new LatLng(33.587408, -7.611464);
        Marker markerMoulay = mMap.addMarker(new MarkerOptions().position(emsiMoulayYoussef).title("Marqueur à EMSI Moulay Youssef"));
        markerMoulay.setTag("moulay");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(emsi, 10));
        if (emsiMarker != null) emsiMarker.setTag("destination");

        // Permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
            return;
        }

        mMap.setMyLocationEnabled(true);

        // Marker click -> draw route from current position
        mMap.setOnMarkerClickListener(marker -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) return false;

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                            LatLng destination = marker.getPosition();

                            mMap.addPolyline(new PolylineOptions()
                                    .add(current, destination)
                                    .width(5)
                                    .color(Color.BLUE));
                        } else {
                            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
                        }
                    });
            return false;
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Reload map with permission
            onMapReady(mMap);
        } else {
            Toast.makeText(this, "Permission denied. Please enable location permission in settings for full functionality.", Toast.LENGTH_LONG).show();
        }        }
    }
