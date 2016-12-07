package cl.telematica.android.alimentame;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import cl.telematica.android.alimentame.Models.Localizacion;
import cl.telematica.android.alimentame.POST.Models.GPSTracker;
import cl.telematica.android.alimentame.Presenters.Contact.ConectionPresenters;
import cl.telematica.android.alimentame.Presenters.GoogleApi;
import cl.telematica.android.alimentame.Servicio.TransferGoogleApi;

public class UserActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button ver;
    GPSTracker gps;
    private ConectionPresenters conectionPresenters;
    private GoogleApi googleApi;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ver= (Button)findViewById(R.id.button4);
        googleApi = TransferGoogleApi.getGoogleApi();
        conectionPresenters = TransferGoogleApi.getConectionPresenters();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UserActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            gps = new GPSTracker(this, UserActivity.this);
            // Check if GPS enabled
            if (gps.canGetLocation()) {
                if(String.valueOf(gps.getLongitude()).equalsIgnoreCase("0")&&
                        String.valueOf(gps.getLatitude()).equalsIgnoreCase("0"))
                    Toast.makeText(this,"error al encontrar ubicacion",Toast.LENGTH_SHORT).show();
            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gps.showSettingsAlert();
            }
        }
        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(UserActivity.this,TiendaActivity.class);
                startActivity(x);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApi.mGoogleApiClient.connect();
        Toast.makeText(this,"onStart",Toast.LENGTH_LONG);
    }


    @Override
    protected void onStop() {
        super.onStop();
        googleApi.mGoogleApiClient.disconnect();
        Toast.makeText(this,"onStop",Toast.LENGTH_LONG);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(gps.getLatitude(), gps.getLongitude());
        TransferGoogleApi.setUbicacion(sydney);
        mMap.setMinZoomPreference((float) 17);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Tu estas aqui!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if(!(TransferGoogleApi.getLista()==null)) {
            for (int i = 0; i < TransferGoogleApi.getLista().size(); i++) {
                Localizacion objeto = TransferGoogleApi.getLista().get(i);
                LatLng nuevo = new LatLng(objeto.getLatitud(), objeto.getLongitud());
                mMap.addMarker(new MarkerOptions().position(nuevo).title(objeto.getNombre()).icon(BitmapDescriptorFactory.fromResource(R.drawable.open)));

            }
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                if(!(marker.getTitle().equals("Tu estas aqui!"))) {
                    Intent x = new Intent(UserActivity.this, Perfil_tiendaActivity.class);
                    x.putExtra("nombre", marker.getTitle());
                    x.putExtra("latitud", marker.getPosition().latitude);
                    x.putExtra("longitud", marker.getPosition().longitude);
                    x.putExtra("tulatitud", gps.getLatitude());
                    x.putExtra("tulongitud", gps.getLongitude());
                    startActivity(x);
                    return false;
                }
                return false;
            }
        });
        }

    }


