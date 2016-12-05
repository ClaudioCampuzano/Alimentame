package cl.telematica.android.alimentame.Todolodemas.Presenters;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cl.telematica.android.alimentame.Todolodemas.Presenters.Contact.ConectionPresenters;

/**
 * Created by gerson on 29-11-16.
 */

public class ConectionPresentersImpl implements ConectionPresenters{
    private HashMap<String,LatLng> area;
    private View activity;
    private Activity act;
    private RequestQueue requestQueue;
    private Peticiones peticion;
    private GoogleApi googleApi;
    public ConectionPresentersImpl(View activity, Activity act,
                                   RequestQueue requestQueue, Peticiones peticion, GoogleApi googleApi){
    this.activity=activity;
        this.act = act;
        this.requestQueue = requestQueue;
        this.peticion = peticion;
        this.googleApi=googleApi;
        area =new HashMap<String, LatLng>();
    }
    @Override
    public void makeRequest() {
        String url = "http://alimentame-multimedios.esy.es/obtener_coordenadas.php";
        List<Localizacion> listaLocalizaciones = new ArrayList<Localizacion>();
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                List<Localizacion> lista = new ArrayList<Localizacion>();
                lista = getLista(jsonArray);
                for(int i=0;i<lista.size();i++){
                    String vendedor = lista.get(i).getVendedor();
                    String producto = lista.get(i).getProducto();
                    area.put(producto,
                            new LatLng(lista.get(i).getLatitud(),lista.get(i).getLongitud()));

                }
                if(!area.isEmpty()){
                    googleApi.populateGeofenceList(area);
                    googleApi.removeGeofences();
                    googleApi.addGeofences();
                }
                onConnectionFinished();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onConnectionFailed(volleyError.toString());
            }
        });
        addToQueue(request);
    }

    @Override
    public List<Localizacion> getLista(JSONArray jsonArray) {
        List<Localizacion> listaLocalizaciones = new ArrayList<Localizacion>();
        try {
            int size = jsonArray.length();
            for(int i = 0; i < size; i++){
                Localizacion Localizacion = new Localizacion();
                JSONObject objeto = jsonArray.getJSONObject(i);
                Localizacion.setLatitud(objeto.getDouble("latitud"));
                Localizacion.setLongitud(objeto.getDouble("longitud"));
                Localizacion.setVendedor(objeto.getString("vendedor"));
                Localizacion.setProducto(objeto.getString("producto"));
                listaLocalizaciones.add(Localizacion);
            }
            return listaLocalizaciones;
        } catch (JSONException e) {
            e.printStackTrace();
            return listaLocalizaciones;
        }
    }

    @Override
    public void onPreStartConnection() {
        act.setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void onConnectionFinished() {
        act.setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void onConnectionFailed(String error) {
        act.setProgressBarIndeterminateVisibility(false);
        Toast.makeText(act, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addToQueue(Request request) {
        if (request != null) {
            request.setTag(act);
            if (requestQueue == null)
                requestQueue = peticion.getRequestQueue();
            request.setRetryPolicy(new DefaultRetryPolicy(
                    60000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            onPreStartConnection();
            requestQueue.add(request);
        }
    }




}
