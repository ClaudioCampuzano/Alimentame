package cl.telematica.android.alimentame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cl.telematica.android.alimentame.LogIn.LogInActivity;
import cl.telematica.android.alimentame.Models.Localizacion;
import cl.telematica.android.alimentame.POST.Models.GPSTracker;
import cl.telematica.android.alimentame.POST.Models.MySingleton;
import cl.telematica.android.alimentame.POST.Publicar;
import cl.telematica.android.alimentame.POST.VerProductos;
import cl.telematica.android.alimentame.Presenters.Contact.ConectionPresenters;
import cl.telematica.android.alimentame.Presenters.GoogleApi;
import cl.telematica.android.alimentame.Servicio.TransferGoogleApi;

public class UserActivity extends FragmentActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private Button ver;
    GPSTracker gps;
    private ConectionPresenters conectionPresenters;
    private GoogleApi googleApi;


    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    AlertDialog.Builder builder;


    MenuItem itemView1;
    MenuItem itemView2;
    MenuItem itemView3;
    MenuItem itemView4;
    MenuItem itemView5;
    MenuItem itemView6;
    MenuItem itemView7;
    MenuItem itemView8;
    MenuItem itemView9;
    MenuItem itemViewMas;

    Menu menu;

    SharedPreferences.Editor editor;

    public boolean checked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checked = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
         editor = sharedpreferences.edit();

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
                        String.valueOf(gps.getLatitude()).equalsIgnoreCase("0"));
                    //Toast.makeText(this,"error al encontrar ubicacion",Toast.LENGTH_SHORT).show();
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
                conectionPresenters.Extraerdatos().execute();
                startActivity(x);
            }
        });


        builder = new AlertDialog.Builder(UserActivity.this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        menu = navigationView.getMenu();
        itemView1 = (MenuItem) menu.findItem(R.id.venta1);
        itemView2 = (MenuItem) menu.findItem(R.id.venta2);
        itemView3 = (MenuItem) menu.findItem(R.id.venta3);
        itemView4 = (MenuItem) menu.findItem(R.id.venta4);
        itemView5 = (MenuItem) menu.findItem(R.id.venta5);
        itemView6 = (MenuItem) menu.findItem(R.id.venta6);
        itemView7 = (MenuItem) menu.findItem(R.id.venta7);
        itemView8 = (MenuItem) menu.findItem(R.id.venta8);
        itemView9 = (MenuItem) menu.findItem(R.id.venta9);
        itemViewMas = (MenuItem) menu.findItem(R.id.ventaMas);
        itemSetter();
        checked = false;

    }


    @Override
    public void onBackPressed() {
        conectionPresenters.makeRequest();
        itemSetter();
    }
   // @SuppressWarnings("StatementWithEmptyBody")
    //@Override
    /*public boolean onNavigationItemSelected(MenuItem item) {
        Toast.makeText(this, "La weaaawwwwwa", Toast.LENGTH_SHORT).show();
        int id = item.getItemId();

        if (id == R.id.settings) {
        } else if (id == R.id.geofenceAct) {

        } else if (id == R.id.logout) {
            Toast.makeText(this, "La weaaaa", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.ventaAdd) {

        } else if (id == R.id.venta1) {

        } else if (id == R.id.venta2) {

        } else if (id == R.id.venta3) {

        } else if (id == R.id.venta4) {

        } else if (id == R.id.venta5) {

        } else if (id == R.id.venta6) {

        } else if (id == R.id.venta7) {

        } else if (id == R.id.venta8) {

        } else if (id == R.id.venta9) {

        } else if (id == R.id.ventaMas) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/

    void itemSetter() {

        String url = "http://alimentame-multimedios.esy.es/productosDrawer.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                builder.setMessage(response);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                int i = 0;
                String c2 = null;
                JSONObject jsonC2 = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    i = Integer.valueOf(jsonObject.getString("length"));
                    c2 = jsonObject.getString("productos");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(UserActivity.this, "El largo es : "+String.valueOf(i), Toast.LENGTH_SHORT).show();
                try {
                    jsonC2 = new JSONObject(c2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                itemView1.setVisible(false);
                itemView2.setVisible(false);
                itemView3.setVisible(false);
                itemView4.setVisible(false);
                itemView5.setVisible(false);
                itemView6.setVisible(false);
                itemView7.setVisible(false);
                itemView8.setVisible(false);
                itemView9.setVisible(false);

                int c = 1;
                for(c=1;c <= i; c++) {
                    if(c==1){
                        itemView1.setVisible(true);
                        try {
                            itemView1.setTitle((String)jsonC2.get("1"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (c==2) {
                        itemView2.setVisible(true);
                        try {
                            itemView2.setTitle((String)jsonC2.getString("2"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (c==3) {
                        itemView3.setVisible(true);
                        try {
                            itemView3.setTitle((String)jsonC2.getString("3"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (c==4) {
                        itemView4.setVisible(true);
                        try {
                            itemView4.setTitle((String)jsonC2.getString("4"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (c==5) {
                        itemView5.setVisible(true);
                        try {
                            itemView5.setTitle((String)jsonC2.getString("5"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (c==6) {
                        itemView6.setVisible(true);
                        try {
                            itemView6.setTitle((String)jsonC2.getString("6"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (c==7) {
                        itemView7.setVisible(true);
                        try {
                            itemView7.setTitle((String)jsonC2.getString("7"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (c==8) {
                        itemView8.setVisible(true);
                        try {
                            itemView8.setTitle((String)jsonC2.getString("8"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (c==9) {
                        itemView9.setVisible(true);
                        try {
                            itemView9.setTitle((String)jsonC2.getString("9"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if (c>=10) {
                        itemViewMas.setVisible(true);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserActivity.this, "Error", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userID", sharedpreferences.getString("User_ID", "0"));
                return params;
            }
        };
        MySingleton.getmInstance(UserActivity.this).addTorequestque(stringRequest);
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
                    conectionPresenters.Extraerdatos().execute();
                    startActivity(x);
                    return false;
                }
                return false;
            }
        });
        }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) {

            Intent intent = new Intent(UserActivity.this, VerProductos.class);
            intent.putExtra("User_ID", sharedpreferences.getString("User_ID", "0"));
            startActivity(intent);

        } else if (id == R.id.geofenceAct) {

        } else if (id == R.id.logout) {
            editor.clear();
            editor.commit();
            Intent intent = new Intent(UserActivity.this, LogInActivity.class);
            startActivity(intent);

        } else if (id == R.id.ventaAdd) {
            Intent explicit_intent = new Intent(UserActivity.this,Publicar.class);
            startActivity(explicit_intent);

        } else if (id == R.id.venta1) {


        } else if (id == R.id.venta2) {

        } else if (id == R.id.venta3) {

        } else if (id == R.id.venta4) {

        } else if (id == R.id.venta5) {

        } else if (id == R.id.venta6) {

        } else if (id == R.id.venta7) {

        } else if (id == R.id.venta8) {

        } else if (id == R.id.venta9) {

        } else if (id == R.id.ventaMas) {
        }
        return false;
    }
}


