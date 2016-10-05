package com.sa.alejandro.ejemplomapas;

import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.cast.Cast;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private Button btnTipo;
    private Button btnMover;
    private Button btnAnimar;
    private Button btnPosicion;
    private Button btnMarcador;
    private Button btnPoligono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtiene el SupportMapFragment y recibe una notificacion cuando el mapa esta listo para ser utilizado.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnTipo = (Button) findViewById(R.id.btnTipo);
        btnMover = (Button) findViewById(R.id.btnMover);
        btnAnimar = (Button) findViewById(R.id.btnAnimar);
        btnPosicion = (Button) findViewById(R.id.btnPosicion);
        btnMarcador = (Button) findViewById(R.id.btnMarcador);
        btnPoligono = (Button) findViewById(R.id.btnPoligono);

        btnMarcador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(-38.5174696, -72.4279322))
                        .title("Mi casa"));
            }
        });

        btnTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mMap.getUiSettings().setZoomControlsEnabled(true);
            }
        });

        btnMover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraUpdate camUpd1 =
                        CameraUpdateFactory
                                .newLatLngZoom(new LatLng(-38.5174696, -72.4279322), 16);
                mMap.moveCamera(camUpd1);
            }
        });

        btnAnimar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng temuco = new LatLng(-38.748851, -72.617314);

                CameraPosition camPos = new CameraPosition.Builder()
                        .target(temuco)   //Centramos el mapa en temuco
                        .zoom(19)         //Establecemos el zoom
                        .bearing(20)      //Establecemos la orientación con el noreste arriba
                        .tilt(65)         //Bajamos el punto de vista de la cámara
                        .build();

                CameraUpdate camUpd3 =
                        CameraUpdateFactory.newCameraPosition(camPos);

                mMap.animateCamera(camUpd3);
            }
        });

        btnPosicion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraPosition camPos = mMap.getCameraPosition();
                LatLng coordenadas = camPos.target;
                double latitud = coordenadas.latitude;
                double longitud = coordenadas.longitude;
                Toast.makeText(MapsActivity.this, "Lat: " + latitud + " | Long: " + longitud, Toast.LENGTH_SHORT).show();
            }
        });

        btnPoligono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PolylineOptions lineas = new PolylineOptions()
                        .add(new LatLng(-38.74, -72.42))
                        .add(new LatLng(-38.74, -65))
                        .add(new LatLng(-34.5, -65))
                        .add(new LatLng(-34.5, -72.42))
                        .add(new LatLng(-38.0, -72.42));

                lineas.width(8);
                lineas.color(Color.RED);

                mMap.addPolyline(lineas);
            }
        });
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

        mMap.getUiSettings().setMapToolbarEnabled(false);
        //si no queremos las herramientas de google

        // Add a marker in Sydney and move the camera
        LatLng ufro = new LatLng(-38.748851,-72.617314);
        mMap.addMarker(new MarkerOptions().position(ufro).title("Marca en la Ufro"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ufro));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng point) {
                Projection proj = mMap.getProjection();
                Point coord = proj.toScreenLocation(point);

                Toast.makeText(
                        MapsActivity.this,
                        "Click\n" +
                                "Lat: " + point.latitude + "\n" +
                                "Lng: " + point.longitude + "\n" +
                                "X: " + coord.x + " - Y: " + coord.y,
                        Toast.LENGTH_SHORT).show();
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            //Called when camera movement has ended, there are no pending animations and the user has stopped interacting with the map
            @Override
            public void onCameraIdle() {
                Toast.makeText(
                        MapsActivity.this,
                        "Cambio Cámara\n" +
                                "Lat: " +  mMap.getCameraPosition().target.latitude + "\n" +
                                "Lng: " + mMap.getCameraPosition().target.longitude + "\n" +
                                "Zoom: " + mMap.getCameraPosition().zoom + "\n" +
                                "Orientación: " + mMap.getCameraPosition().bearing + "\n" +
                                "Ángulo: " + mMap.getCameraPosition().tilt,
                        Toast.LENGTH_SHORT).show();
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(
                        MapsActivity.this,
                        "Marcador pulsado:\n" +
                                marker.getTitle(),
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }
}
