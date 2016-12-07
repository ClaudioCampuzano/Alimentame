package cl.telematica.android.alimentame;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
import cl.telematica.android.alimentame.Models.Peticiones;
import cl.telematica.android.alimentame.POST.Publicar;
import cl.telematica.android.alimentame.POST.Vendedor;
import cl.telematica.android.alimentame.Presenters.ConectionPresentersImpl;
import cl.telematica.android.alimentame.Presenters.Contact.ConectionPresenters;
import cl.telematica.android.alimentame.Presenters.GoogleApi;
import cl.telematica.android.alimentame.Servicio.ServiceUpdate;
import cl.telematica.android.alimentame.Servicio.TransferGoogleApi;

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
    private Button actualizarDatos;
    private Button agregarZona;

    /**** LOGIN INTERVENTION ****/
    private Button logOutButton;
    private TextView usuario;
    /**** LOGIN INTERVENTION ****/

    private HashMap<String,LatLng> area;
    private RequestQueue requestQueue;
    private Peticiones peticion;
    private ConectionPresenters conectionPresenters;
    private GoogleApi googleApi;



    /**** LOGIN INTERVENTION ****/
    //Variables para saber si se está logeado o no
    public static final String MyPREFERENCES = "MyPrefs" ;


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        if(sharedpreferences.getString("name", "name") == "name") {
            checkLog();
        }
    }


    /**** LOGIN INTERVENTION ****/

    @Override
    public void onCreate(Bundle savedInstanceState) {

        /**** LOGIN INTERVENTION ****/
        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        Toast.makeText(this, "Bienvenido: "+sharedpreferences.getString("name", "name"), Toast.LENGTH_SHORT).show();
        // Intervención para comprobar el login
        if(sharedpreferences.getString("name", "name") == "name") {
            checkLog();
        }
        /**** LOGIN INTERVENTION ****/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list_view);
        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        mAddGeofencesButton = (Button) findViewById(R.id.add_geofences_button);
        actualizarDatos = (Button) findViewById(R.id.actualizar);
        agregarZona = (Button) findViewById(R.id.agregar);
        TransferGoogleApi.setLista(null);


        /**** LOGIN INTERVENTION ****/
        logOutButton = (Button)findViewById(R.id.logOut);
        usuario =(TextView)findViewById(R.id.usuario);
        /**** LOGIN INTERVENTION ****/
        usuario.setText(sharedpreferences.getString("name","name"));
        mGeofenceList = new ArrayList<Geofence>();
        mGeofencePendingIntent = null;
        googleApi = new GoogleApi(mGoogleApiClient,this,mGeofencePendingIntent,mGeofenceList);
        googleApi.populateGeofenceList(Constants.BAY_AREA_LANDMARKS);
        googleApi.buildGoogleApiClient();
        peticion = Peticiones.getInstance(this.getApplicationContext());
        requestQueue = peticion.getRequestQueue();
        conectionPresenters = new ConectionPresentersImpl(activity,this,
                requestQueue,peticion,googleApi);

        /* Servicio */
        Intent x = new Intent(MainActivity.this,ServiceUpdate.class);
        TransferGoogleApi.setGoogleApi(googleApi);
        TransferGoogleApi.setConectionPresenters(conectionPresenters);
        startService(x);

        actualizarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conectionPresenters.Extraerdatos().execute();
                Intent x = new Intent(MainActivity.this,UserActivity.class);
                startActivity(x);
            }
        });
        agregarZona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent variable_aux  = new Intent(MainActivity.this,Vendedor.class);
                startActivity(variable_aux);
            }
        });
        mAddGeofencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conectionPresenters.makeRequest();
                googleApi.addGeofences();
            }
        });

        /**** LOGIN INTERVENTION ****/
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.commit();
                Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });
        /**** LOGIN INTERVENTION ****/

       // startActivity(new Intent(MainActivity.this,UserActivity.class));

    }


    @Override
    protected void onStart() {
        /**** LOGIN INTERVENTION ****/
        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        if(sharedpreferences.getString("name", "name") == "name") {
            checkLog();
        }
        /**** LOGIN INTERVENTION ****/

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

    /**** LOGIN INTERVENTION ****/
    public void checkLog(){
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);

    }

    AlertDialog.Builder builder;
    /*public String getUserInfo(final String ID) {
        final String n_url = "http://alimentame-multimedios.esy.es/getuser.php";
        final String[] respuesta = new String[1];
        StringRequest stringRequest = new StringRequest(POST, n_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                builder.setMessage(response);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                respuesta[0] = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", ID);
                return params;
            }
        };
        MySingleton.getmInstance(MainActivity.this).addTorequestque(stringRequest);
        return respuesta[0];

    }*/
    /**** LOGIN INTERVENTION ****/


}
