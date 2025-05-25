package com.example.profattendaceemsi;

import android.location.Location;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;

public interface OnMapReadyCallback {
    //initialize the map and request location updates.
    void onMapReady(GoogleMap googleMap);

    // is a callback method triggered whenever the location changes. Here, you add a marker for the current location and move the camera to focus on it.
    void onLocationChanged(@NonNull Location location);
}
