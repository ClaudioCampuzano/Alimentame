package cl.telematica.android.alimentame.POST.Presenters;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import cl.telematica.android.alimentame.POST.Models.GPSTracker;
import cl.telematica.android.alimentame.POST.Models.MySingleton;
import cl.telematica.android.alimentame.POST.Presenters.Contract.PublicarPresenters;
import cl.telematica.android.alimentame.POST.Publicar;
import cl.telematica.android.alimentame.POST.VerProductos;

/**
 * Created by Claudio on 03-12-2016.
 */

public class PublicarImpl implements PublicarPresenters {
    private Publicar mActivity;
    private String Nombre, Precio, Descripcion, User_ID, State, Latitud, Longitud, Imagen;
    private String url = "http://alimentame-multimedios.esy.es/insert.php";
    private AlertDialog.Builder builder;
    private GPSTracker gps;
    private Context mContext;

    public PublicarImpl(Publicar Activity){
        mActivity = Activity;
    }

    @Override
    public void SetData(String Nombre, String Precio, String Descripcion, String User_ID,
                        String State, String Imagen) {
        this.Precio = Precio;
        this.Descripcion= Descripcion;
        this.User_ID=User_ID;
        this.State=State;
        this.Latitud=String.valueOf(gps.getLatitude());
        this.Longitud=String.valueOf(gps.getLongitude());
        this.Imagen=Imagen;
        this.Nombre=Nombre;
    }

    @Override
    public void UploadData() {
        builder = new AlertDialog.Builder(mActivity);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                builder.setMessage(response);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mActivity.Nombre.setText("");
                        mActivity.Descripcion.setText("");
                        mActivity.Precio.setText("");
                    }
                });
                mActivity.Nombre.setText("");
                mActivity.Descripcion.setText("");
                mActivity.Precio.setText("");
                Toast.makeText(mActivity,"Insercion exitosa",Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mActivity,"Error",Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("nombre",Nombre);
                params.put("precio",Precio);
                params.put("user_id",User_ID);
                params.put("descripcion", Descripcion);
                params.put("state",State);
                params.put("longitud",Longitud);
                params.put("latitud",Latitud);
                params.put("imagen",Imagen);
                return params;
            }
        };
        MySingleton.getmInstance(mActivity).addTorequestque(stringRequest);
    }

    @Override
    public void InicializarGps() {
        mContext = mActivity;
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            gps = new GPSTracker(mContext, mActivity);
            if (gps.canGetLocation()) {
            } else {
                gps.showSettingsAlert();
            }
        }
    }
}
