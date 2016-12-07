package cl.telematica.android.alimentame;

import android.location.LocationListener;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cl.telematica.android.alimentame.Models.HttpServerConnection;
import cl.telematica.android.alimentame.Models.Localizacion;
import cl.telematica.android.alimentame.Servicio.TransferGoogleApi;

public class Perfil_tiendaActivity extends AppCompatActivity {
    private TextView nombre;
    private TextView latitud;
    private TextView longitud;
    private TextView descripcion;
    private TextView precio;
    private TextView dist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_tienda);
        nombre = (TextView)findViewById(R.id.nombre_user);
        latitud = (TextView)findViewById(R.id.latitud1);
        longitud = (TextView)findViewById(R.id.longitud1);
        descripcion =(TextView)findViewById(R.id.detalle);
        precio = (TextView)findViewById(R.id.precio3);
        dist = (TextView)findViewById(R.id.distancia);
        String url ="http://alimentame-multimedios.esy.es/producto_info.php";
        String name = getIntent().getExtras().getString("nombre");
        double lat=getIntent().getExtras().getDouble("latitud");
        double log=getIntent().getExtras().getDouble("longitud");
        double tulat=getIntent().getExtras().getDouble("tulatitud");
        double tulog=getIntent().getExtras().getDouble("tulongitud");
        double distancia =distanciaCoord(lat,log,tulat,tulog);
        String latitude = String.valueOf(lat);
        String longitude = String.valueOf(log);
        DecimalFormat decimal = new DecimalFormat("0.00000");
        String a = decimal.format(distancia);
        dist.setText(String.valueOf(a)+" KM");
        nombre.setText(name);
        latitud.setText(latitude);
        longitud.setText(longitude);
        final String peticion = url+"?"+"nombre="+name+"&"
                +"latitud="+latitude+"&"
                +"longitud="+longitude;
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String repositorios = new HttpServerConnection().
                        connectToServer(peticion, 15000);

                return repositorios;
            }

            @Override
            protected void onPostExecute(String s) {
                try {
                    JSONObject objeto = new JSONObject(s);
                    descripcion.setText(objeto.getString("Descripcion"));
                    //dt.setImagen(objeto.getString("Imagen"));
                    precio.setText(objeto.getString("Precio"));
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }

            }
        }.execute();

    }
    public static double distanciaCoord(double lat1, double lng1, double lat2, double lng2) {
        //double radioTierra = 3958.75;//en millas
        double radioTierra = 6371;//en kilómetros
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));
        double distancia = radioTierra * va2;

        return distancia;
    }
}
