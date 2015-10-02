package com.jayway.waytravel;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.jayway.waytravel.dto.PinDTO;
import com.jayway.waytravel.dto.PinsDTO;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    Gson gson = new Gson();
    private GoogleMap mMap;
    private LocationManager mLm;

    private boolean mHasCentered;


    private double mLongitude;
    private double mLatitude;
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mLongitude = location.getLongitude();
            mLatitude = location.getLatitude();

            Log.d("TAG", "saker");

            if (!mHasCentered) {
                mHasCentered = true;

                LatLng center;
                center = new LatLng(mLatitude, mLongitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(center));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
            }

            invokePostJob();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
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

        mLm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

        final android.os.Handler handler = new android.os.Handler();
        handler.post(invokeGetJob(handler));
    }

    @NonNull
    private Runnable invokePostJob() {
        return new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(JSON, "{\n" +
                        "\"latitude\": \"" + mLatitude + "\",\n" +
                        "\"longitude\": \"" + mLongitude + "\"\n" +
                        "}");
                Request request = new Request.Builder()
                        .url("http://travelway-server.herokuapp.com/travellers/0")
                        .post(body)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                    }
                });
            }
        };
    }


    public Runnable invokeGetJob(final Handler handler) {
        return new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                final Request request = new Request.Builder().addHeader("Accept", "application/json").url("http://travelway-server.herokuapp.com/travellers").build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(final Response response) throws IOException {

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                PinsDTO persons = null;
                                try {
                                    persons = gson.fromJson("{\"pins\":" + response.body().string() + "}", PinsDTO.class);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                mMap.clear();

                                boolean flag = false;

                                for (PinDTO p : persons.pins) {
                                    LatLng latlan = new LatLng(p.latitude, p.longitude);

                                    MarkerOptions icon;

                                    if (!flag) {
                                        flag = true;

                                        icon = new MarkerOptions().position(latlan).title(p.name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                                    } else {
                                        icon = new MarkerOptions().position(latlan).title(p.name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                    }

                                    mMap.addMarker(icon);
                                }

                                handler.postDelayed(invokeGetJob(handler), 10 * 1000);
                            }
                        });
                    }
                });
            }
        };
    }
}





