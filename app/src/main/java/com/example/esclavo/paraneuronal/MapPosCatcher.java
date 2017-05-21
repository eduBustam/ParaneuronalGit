package com.example.esclavo.paraneuronal;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class MapPosCatcher extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_pos_catcher);
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
        final Polyline recorrido = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-22.406530, -41.842921),
                        new LatLng(-22.406706, -41.837756),
                        new LatLng(-22.404328, -41.828486),
                        new LatLng(-22.406586, -41.823933),
                        new LatLng(-22.405256, -41.818015),
                        new LatLng(-22.408210, -41.810402),
                        new LatLng(-22.402657, -41.795139),
                        new LatLng(-22.398196, -41.790510),
                        new LatLng(-22.392815, -41.778839),
                        new LatLng(-22.384867, -41.775477),
                        new LatLng(-22.371389, -41.778159),
                        new LatLng(-22.369224, -41.775555),
                        new LatLng(-22.353257, -41.768089),
                        new LatLng(-22.328294, -41.729314),
                        new LatLng(-22.314725, -41.720222))
                .width(5)
                .color(Color.GREEN));
        recorrido.setClickable(false);
        // Add a marker in Sydney and move the camera
        LatLng inicio = new LatLng(-22.406530, -41.842921);
        LatLng fin=new LatLng(-22.314725, -41.720222);
        float zoom= (float)(15.0);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng clickCoords) {
                for (LatLng polyCoords : recorrido.getPoints()) {
                    float[] results = new float[1];
                    Location.distanceBetween(clickCoords.latitude, clickCoords.longitude,
                                polyCoords.latitude, polyCoords.longitude, results);
                    if (results[0] < 100) {
                        // If distance is less than 100 meters, this is your polyline
                        mMap.addMarker(new MarkerOptions().position(clickCoords).title("Me Bajo"));

                    }
                }
            }
        });
        /*mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener(){
           @Override
            public void onPolylineClick (final Polyline recorrido){
               mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                   @Override
                   public void onMapClick(LatLng latLng) {
                       mMap.addMarker(new MarkerOptions().position(latLng).title("Me Bajo"));
                   }
               });
           }

        });*/
        mMap.addMarker(new MarkerOptions().position(inicio).title("Inicio"));
        mMap.addMarker(new MarkerOptions().position(fin).title("Fin"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(inicio,zoom));
    }
}
