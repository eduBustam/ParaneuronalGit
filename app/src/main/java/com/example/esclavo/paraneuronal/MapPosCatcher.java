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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
    private int suboCont=0;
    private Marker bajo;
    private int bajoCont=0;
    private Polyline camino;
    private int recorrido;

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
        Context context = getApplicationContext();
        switch (item.getItemId()) {
            case R.id.action_send:
                //TODO:Aca estan los datos listos para ser mandados, subo es el punto de inicio, bajo es el punto de fin
                if(suboCont==1 && bajoCont==1 &&(recorrido==1 || recorrido==2)){
                    float latSubo= (float) subo.getPosition().latitude;
                    float lngSubo= (float) subo.getPosition().longitude;
                    float latBajo= (float) bajo.getPosition().latitude;
                    float lngBajo= (float) bajo.getPosition().longitude;
                    //recorrido
                    GETRequest(latSubo, lngSubo,latBajo, lngBajo);
                    Toast.makeText(context,"Enviado", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context,"Marque recorrido y seleccione puntos", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_reco1:
                recorrido=1;
                Toast.makeText(context, "Recorrido 1", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_reco2:
                recorrido=2;
                Toast.makeText(context, "Recorrido 2", Toast.LENGTH_SHORT).show();
            case R.id.action_extra:
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
            camino = mMap.addPolyline(new PolylineOptions()
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
        camino.setClickable(false);

        //Me bajo
        //Simple click para detectar la bajada
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng clickencito) {
                //MENOR
                float min=(float) (999999999.9);
                float[] results = new float[1];
                //BUSCAR MENOR
                for (LatLng recoCoords : camino.getPoints()) {
                    Location.distanceBetween(clickencito.latitude, clickencito.longitude, recoCoords.latitude, recoCoords.longitude, results);
                    min=func.menor(min,results[0]);
                }
                //SE BUSCA LA POS IGUAL A MENOR
                for (LatLng recoCoords : camino.getPoints()) {
                    Location.distanceBetween(clickencito.latitude, clickencito.longitude, recoCoords.latitude, recoCoords.longitude, results);
                    if (results[0]==min && results[0]<100) {
                        if(bajoCont==1)
                            bajo.remove();

                        bajo=mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(recoCoords.latitude,recoCoords.longitude))
                                .title("Me Bajo"));
                        bajoCont=1;
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
                for (LatLng recoCoords : camino.getPoints()) {
                    Location.distanceBetween(longClick.latitude, longClick.longitude, recoCoords.latitude, recoCoords.longitude, results);
                    min=func.menor(min,results[0]);
                }
                //SE BUSCA LA POS IGUAL A MENOR
                for (LatLng recoCoords : camino.getPoints()) {
                    Location.distanceBetween(longClick.latitude, longClick.longitude, recoCoords.latitude, recoCoords.longitude, results);
                    if (results[0]==min&& results[0]<100) {
                        if(suboCont==1)
                            subo.remove();
                        subo=mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.defaultMarker((float)(150)))
                                .position(new LatLng(recoCoords.latitude,recoCoords.longitude))
                                .title("Me Subo"));
                        suboCont=1;
                    }
                }
            }
        });
    }




    RESPUESTA RESPUESTA;


    //SE DEBE INYECTAR ESTAS VARIABLES A LA FUNCION, LUEGO PREGUNTARA A LA URL Y ENTREGARA UN OBJETO LLAMADO RESPUESTA
    public void GETRequest(float inicio_lat, float inicio_long, float final_lat, float final_long){

        String URL = "http://159.203.59.183/api/datosGPS?lat1="+inicio_lat+"?lon1="+inicio_long+"?lat2="+final_lat+"?lon2="+final_long;



        GsonRequest gsonRequest = new GsonRequest(Request.Method.GET, URL, RESPUESTA.class, null,
                new Response.Listener<RESPUESTA>() {
                    @Override
                    public void onResponse(RESPUESTA response) {
                        RESPUESTA = response;

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                //ERROR EN EL LLAMADO
            }
        });
        gsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the queue
        VolleySingleton.getInstance(this).addToRequestQueue(gsonRequest);
    }
}
