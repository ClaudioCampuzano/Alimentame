package cl.telematica.android.alimentame;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cl.telematica.android.alimentame.Models.Localizacion;
import cl.telematica.android.alimentame.Models.Peticiones;
import cl.telematica.android.alimentame.POST.Publicar;
import cl.telematica.android.alimentame.Presenters.ConectionPresentersImpl;
import cl.telematica.android.alimentame.Presenters.Contact.ConectionPresenters;
import cl.telematica.android.alimentame.Presenters.GoogleApi;
import cl.telematica.android.alimentame.servicios.ServiceUpdate;
import cl.telematica.android.alimentame.servicios.TransferGoogleApi;

public class MenuActivity extends AppCompatActivity {
    protected GoogleApiClient mGoogleApiClient;
    //Lista con las locaciones de ejemplo.
    protected ArrayList<Geofence> mGeofenceList;
    //Se utiliza para realizar un seguimiento de si se han añadido geofences.
    //private boolean mGeofencesAdded;
    //Se utiliza cuando se solicita para agregar o quitar geofences.
    private PendingIntent mGeofencePendingIntent;
    //Used to persist application state about whether geofences were added
    //private SharedPreferences mSharedPreferences;
    // Botones que se aprietan y que hacen saltar la accion descrita.
    private Button mAddGeofencesButton;
    private Button actualizarDatos;
    private Button agregarZona;
    private HashMap<String,LatLng> area;
    private RequestQueue requestQueue;
    private Peticiones peticion;
    private ConectionPresenters conectionPresenters;
    private GoogleApi googleApi;
    private View activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        mAddGeofencesButton = (Button) findViewById(R.id.add_geofences_button);
        actualizarDatos = (Button) findViewById(R.id.actualizar);
        agregarZona = (Button) findViewById(R.id.agregar);
        mGeofenceList = new ArrayList<Geofence>();
        mGeofencePendingIntent = null;
        googleApi = new GoogleApi(mGoogleApiClient,getApplicationContext(),mGeofencePendingIntent,mGeofenceList);
        googleApi.populateGeofenceList(Constants.BAY_AREA_LANDMARKS);
        googleApi.buildGoogleApiClient();
        area =new HashMap<String, LatLng>();
        peticion = Peticiones.getInstance(this.getApplicationContext());
        requestQueue = peticion.getRequestQueue();
        conectionPresenters = new ConectionPresentersImpl(activity,this,
                requestQueue,peticion,googleApi);
        conectionPresenters.Extraerdatos().execute();
        actualizarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(MenuActivity.this,UserActivity.class);

                startActivity(x);
            }
        });
        agregarZona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent variable_aux  = new Intent(MenuActivity.this,Publicar.class);
                startActivity(variable_aux);
            }
        });
        mAddGeofencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleApi.addGeofences();
            }
        });
        Intent x = new Intent(getApplicationContext(),ServiceUpdate.class);
        TransferGoogleApi.setGoogleApi(googleApi);
        TransferGoogleApi.setConectionPresenters(conectionPresenters);
        startService(x);

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
}
