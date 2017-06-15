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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class MapPosCatcher extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Funciones func=new Funciones();
    private Marker inicio;
    private Marker fin;
    private Marker subo;
    private Marker bajo;
    private Polyline recorrido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_pos_catcher);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            case R.id.action_favorite:
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-22.406530, -41.842921),(float)(13.0)));
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
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
        LatLng posInicio = new LatLng(-22.406530, -41.842921);
        LatLng posFin=new LatLng(-22.314725, -41.720222);
        inicio=mMap.addMarker(new MarkerOptions()
                .position(posInicio)
                .title("Inicio")
                .icon(BitmapDescriptorFactory.defaultMarker((float)(60))));
        fin=mMap.addMarker(new MarkerOptions()
                .position(posFin)
                .title("Fin")
                .icon(BitmapDescriptorFactory.defaultMarker((float)(60))));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posInicio,(float)(15.0)));
        //Para cuando ya tengamos los punstos reales designados del recorrido
        //final Polyline recorrido=mMap.addPolyline(new PolylineOptions().add(List<LatLng> Puntos).width(5).color(Color.Blue);
            recorrido = mMap.addPolyline(new PolylineOptions()
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

        //Me bajo
        //Simple click para detectar la bajada
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng clickencito) {
                //MENOR
                float min=(float) (999999999.9);
                float[] results = new float[1];
                //BUSCAR MENOR
                for (LatLng recoCoords : recorrido.getPoints()) {
                    Location.distanceBetween(clickencito.latitude, clickencito.longitude, recoCoords.latitude, recoCoords.longitude, results);
                    min=func.menor(min,results[0]);
                }
                //SE BUSCA LA POS IGUAL A MENOR
                for (LatLng recoCoords : recorrido.getPoints()) {
                    Location.distanceBetween(clickencito.latitude, clickencito.longitude, recoCoords.latitude, recoCoords.longitude, results);
                    if (results[0]==min) {
                        bajo.remove();
                        bajo=mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(recoCoords.latitude,recoCoords.longitude))
                                .title("Me Bajo"));
                        break;
                    }
                }
            }
        });
        //Me subo
        //Long click para detectar la subida
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener(){
            @Override
            public void onMapLongClick(LatLng longClick){
                //MENOR
                float min=(float) (999999999.9);
                float[] results = new float[1];

                //BUSCAR MENOR
                for (LatLng recoCoords : recorrido.getPoints()) {
                    Location.distanceBetween(longClick.latitude, longClick.longitude, recoCoords.latitude, recoCoords.longitude, results);
                    min=func.menor(min,results[0]);
                }
                //SE BUSCA LA POS IGUAL A MENOR
                for (LatLng recoCoords : recorrido.getPoints()) {
                    Location.distanceBetween(longClick.latitude, longClick.longitude, recoCoords.latitude, recoCoords.longitude, results);
                    if (results[0]==min) {
                        subo.remove();
                        subo=mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.defaultMarker((float)(150)))
                                .position(new LatLng(recoCoords.latitude,recoCoords.longitude))
                                .title("Me Subo"));
                        break;
                    }
                }
            }
        });
    }
}
