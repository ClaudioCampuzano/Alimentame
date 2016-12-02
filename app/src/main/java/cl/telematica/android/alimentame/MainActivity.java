package cl.telematica.android.alimentame;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingApi;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

import cl.telematica.android.alimentame.LogIn.LogInActivity;
import cl.telematica.android.alimentame.POST.Publicar;
import cl.telematica.android.alimentame.Presenters.ConectionPresentersImpl;
import cl.telematica.android.alimentame.Presenters.Contact.ConectionPresenters;
import cl.telematica.android.alimentame.Presenters.GoogleApi;

/**
 * Demonstrates how to create and remove geofences using the GeofencingApi. Uses an IntentService
 * to monitor geofence transitions and creates notifications whenever a device enters or exits
 * a geofence.
 *
 * This sample requires a device's Location settings to be turned on. It also requires
 * the ACCESS_FINE_LOCATION permission, as specified in AndroidManifest.xml.
 *
 * Note that this Activity implements ResultCallback<Status>, requiring that
 * {@code onResult} must be defined. The {@code onResult} runs when the result of calling
 * {@link GeofencingApi#addGeofences(GoogleApiClient, GeofencingRequest, PendingIntent)}  addGeofences()} or
 * {@link com.google.android.gms.location.GeofencingApi#removeGeofences(GoogleApiClient, java.util.List)}  removeGeofences()}
 * becomes available.
 */

public class MainActivity extends AppCompatActivity{
    protected static final String TAG = "MainActivity";
    private View activity;
    ListView listView;
    DrawerLayout drawerLayout;
    //Proporciona el punto de entrada a los servicios de Google Play.
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
    private Button mRemoveGeofencesButton;
    private Button actualizarDatos;
    private Button agregarZona;
    private HashMap<String,LatLng> area;
    private RequestQueue requestQueue;
    private Peticiones peticion;
    private ConectionPresenters conectionPresenters;
    private GoogleApi googleApi;


    //Variables para saber si se está logeado o no
    private boolean logged;
    private int userID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Seteo de variables de log, prontamente conexión a base de datos
        logged = false;
        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("logged") == "true"){
            logged = true;
        }
        userID = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Intervención para comprobar el login
        checkLog();

        listView = (ListView) findViewById(R.id.list_view);
        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        mAddGeofencesButton = (Button) findViewById(R.id.add_geofences_button);
        mRemoveGeofencesButton = (Button) findViewById(R.id.remove_geofences_button);
        actualizarDatos = (Button) findViewById(R.id.actualizar);
        agregarZona = (Button) findViewById(R.id.agregar);
        mGeofenceList = new ArrayList<Geofence>();
        mGeofencePendingIntent = null;
        googleApi = new GoogleApi(mGoogleApiClient,this,mGeofencePendingIntent,mGeofenceList,mRemoveGeofencesButton,mAddGeofencesButton);
        googleApi.setButtonsEnabledState();
        googleApi.populateGeofenceList(Constants.BAY_AREA_LANDMARKS);
        googleApi.buildGoogleApiClient();
        area =new HashMap<String, LatLng>();
        peticion = Peticiones.getInstance(this.getApplicationContext());
        requestQueue = peticion.getRequestQueue();
        conectionPresenters = new ConectionPresentersImpl(activity,this,
                requestQueue,peticion,googleApi);
        actualizarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conectionPresenters.makeRequest();
                Toast.makeText(v.getContext(),"Actualizacion Realizada",Toast.LENGTH_SHORT).show();

            }
        });
        agregarZona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent variable_aux  = new Intent(MainActivity.this,Publicar.class);
                startActivity(variable_aux);
            }
        });
        mAddGeofencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleApi.addGeofences();
            }
        });
        mRemoveGeofencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleApi.removeGeofences();
            }
        });
    }


    @Override
    protected void onStart() {
        checkLog();
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

    public void checkLog(){
        if (!logged){
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */

}
