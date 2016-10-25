package cl.telematica.android.alimentame.POST;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import cl.telematica.android.alimentame.R;

public class Publicar extends AppCompatActivity {
    EditText Vendedor,Producto;
    Button insertar;

    GPSTracker gps;
    Context mContext;

    String url = "http://alimentame-multimedios.esy.es/insert.php";
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_publicar);

        Vendedor = (EditText)findViewById(R.id.vendedor);
        Producto = (EditText) findViewById(R.id.producto);

        builder = new AlertDialog.Builder(Publicar.this);

        mContext = this;
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Publicar.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            gps = new GPSTracker(mContext, Publicar.this);
            // Check if GPS enabled
            if (gps.canGetLocation()) {
                if(String.valueOf(gps.getLongitude()).equalsIgnoreCase("0")&&
                        String.valueOf(gps.getLatitude()).equalsIgnoreCase("0"))
                    restartFirstActivity();
            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gps.showSettingsAlert();
            }
        }
        insertar = (Button) findViewById(R.id.insertar);
        insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String vendedor,producto,longitud,latitud;
                vendedor = Vendedor.getText().toString();
                producto = Producto.getText().toString();
                longitud = String.valueOf(gps.getLongitude());
                latitud = String.valueOf(gps.getLatitude());

                if(!vendedor.equalsIgnoreCase("")&&
                        !producto.equalsIgnoreCase("")){
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            builder.setMessage(response);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Vendedor.setText("");
                                    Producto.setText("");
                                }
                            });
                            Toast.makeText(v.getContext(),"Insercion exitosa",Toast.LENGTH_LONG).show();
                            restartFirstActivity();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Publicar.this,"Error",Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("vendedor",vendedor);
                            params.put("producto",producto);
                            params.put("latitud",latitud);
                            params.put("longitud",longitud);
                            return params;
                        }
                    };
                    MySingleton.getmInstance(Publicar.this).addTorequestque(stringRequest);
                }
                else
                    Toast.makeText(Publicar.this,"Hay informacion por rellenar",Toast.LENGTH_LONG).show();
            }
        });
    }
    private void restartFirstActivity()
    {
        Intent i = getApplicationContext().getPackageManager()
                .getLaunchIntentForPackage(getApplicationContext().getPackageName() );

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(i);
    }

}
