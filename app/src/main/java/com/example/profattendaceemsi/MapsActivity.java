package com.example.profattendaceemsi;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync((com.google.android.gms.maps.OnMapReadyCallback) this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // EMSI Coordinates
        LatLng emsi = new LatLng(33.58931956959172, -7.605327086230895);

        // Add marker
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(emsi)
                .title("Marqueur à emsi centre"));

        // Move camera and zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(emsi, 10));

        // Tag the marker (useful for future interaction)
        if (marker != null) {
            marker.setTag("destination");
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

}
