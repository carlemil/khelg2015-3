package com.jayway.waytravel;

import android.content.Context;
import android.graphics.RectF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.jayway.waytravel.dto.PersonDTO;
import com.jayway.waytravel.dto.PersonsDTO;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Gson gson = new Gson();
    private double longitude = Double.NaN;
    private double latitude = Double.NaN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = location.getLongitude();
        latitude = location.getLatitude();
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        PersonsDTO persons = gson.fromJson(MockData.data, PersonsDTO.class);

        RectF r = new RectF(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

        for (PersonDTO p : persons.persons) {
            LatLng latlan = new LatLng(p.latitude, p.longitude);
            mMap.addMarker(new MarkerOptions().position(latlan).title(p.title));
            if (p.latitude < r.top) {
                r.top = p.latitude;
            }
            if (p.latitude > r.bottom) {
                r.top = p.latitude;
            }
            if (p.longitude < r.right) {
                r.right = p.longitude;
            }
            if (p.longitude > r.left) {
                r.left = p.longitude;
            }
            System.out.println(r);

            System.out.println(p);
        }

        LatLng center = new LatLng((r.top + r.bottom) / 2, (r.left + r.right) / 2);
        System.out.println("---\n" + r);

        System.out.println(center);
        if (latitude != Double.NaN && longitude != Double.NaN) {
            center = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(center));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        }
    }
}
