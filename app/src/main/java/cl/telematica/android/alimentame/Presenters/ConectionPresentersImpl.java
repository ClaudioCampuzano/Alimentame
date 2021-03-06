package cl.telematica.android.alimentame.Presenters;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cl.telematica.android.alimentame.Models.HttpServerConnection;
import cl.telematica.android.alimentame.Models.Localizacion;
import cl.telematica.android.alimentame.Models.Peticiones;
import cl.telematica.android.alimentame.Models.UIAdapter;
import cl.telematica.android.alimentame.Presenters.Contact.ConectionPresenters;
import cl.telematica.android.alimentame.Servicio.TransferGoogleApi;

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
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    public ConectionPresentersImpl(View activity, Activity act,
                                   RequestQueue requestQueue, Peticiones peticion, GoogleApi googleApi){
        this.activity=activity;
        this.act = act;
        this.requestQueue = requestQueue;
        this.peticion = peticion;
        this.googleApi=googleApi;
        area =new HashMap<String, LatLng>();
    }
    public ConectionPresentersImpl(Activity activity,RecyclerView recyclerView){
        this.recyclerView=recyclerView;
        this.act = activity;
    }


    @Override
    public void makeRequest() {
        String url = "http://alimentame-multimedios.esy.es/obtener_productos.php";
        List<Localizacion> listaLocalizaciones = new ArrayList<Localizacion>();
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                List<Localizacion> lista = new ArrayList<Localizacion>();
                lista = getLista(jsonArray);
                TransferGoogleApi.setLista(lista);
                for(int i=0;i<lista.size();i++){
                    String Nombre = lista.get(i).getNombre();
                    area.put(Nombre,
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
                Localizacion.setLatitud(objeto.getDouble("Latitud"));
                Localizacion.setLongitud(objeto.getDouble("Longitud"));
                Localizacion.setNombre(objeto.getString("Nombre"));
                Localizacion.setStates(objeto.getInt("state"));
                Localizacion.setPrecio(objeto.getString("Precio"));
                //Localizacion.setImagen(objeto.getString("Imagen"));
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


    @Override
    public AsyncTask<Void, Void, String> Extraerdatos() {
        AsyncTask<Void,Void,String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String repositorios = new HttpServerConnection().
                        connectToServer("http://alimentame-multimedios.esy.es/obtener_productos.php", 15000);
                return repositorios;
            }

            @Override
            protected void onPostExecute(String s) {
                List<Localizacion> lista = getproducto(s);
                TransferGoogleApi.setLista(lista);
            }
        };
        return task;
    }
    @Override
    public AsyncTask<Void, Void, String> ListaTienda() {
        AsyncTask<Void,Void,String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String repositorios = new HttpServerConnection().
                        connectToServer("http://alimentame-multimedios.esy.es/obtener_productos.php", 15000);
                return repositorios;
            }

            @Override
            protected void onPostExecute(String s) {
                ConnectarAdapter(s);
                TransferGoogleApi.setLista(getproducto(s));
            }
        };
        return task;
    }


   public void ConnectarAdapter(String result){
        if (result != null) {
            this.adapter = new UIAdapter(getproducto(result), act);
            recyclerView.setAdapter(adapter);

        }

    }
    List<Localizacion> getproducto(String Result) {
        List<Localizacion> listadatos = new ArrayList<Localizacion>();
        try {
            JSONArray lista = new JSONArray(Result);

            int size = lista.length();
            for (int i = 0; i < size; i++) {
                Localizacion dt = new Localizacion();
                JSONObject objeto = lista.getJSONObject(i);

                dt.setNombre(objeto.getString("Nombre"));
                dt.setDescripcion(objeto.getString("Descripcion"));
                //dt.setImagen(objeto.getString("Imagen"));
                dt.setPrecio(objeto.getString("Precio"));
                dt.setLatitud(objeto.getDouble("Latitud"));
                dt.setLongitud(objeto.getDouble("Longitud"));


                listadatos.add(dt);
            }
            return listadatos;
        } catch (JSONException e) {
            e.printStackTrace();
            return listadatos;
        }
    }





}
