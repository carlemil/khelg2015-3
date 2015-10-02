package com.jayway.waytravel;

import android.graphics.RectF;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


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
        center = new LatLng(persons.persons.get(0).latitude, persons.persons.get(0).longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(center));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
    }
}
