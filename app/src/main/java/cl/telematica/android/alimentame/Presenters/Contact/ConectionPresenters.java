package cl.telematica.android.alimentame.Presenters.Contact;

import android.os.AsyncTask;

import com.android.volley.Request;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;

import cl.telematica.android.alimentame.Models.Localizacion;

/**
 * Created by gerson on 29-11-16.
 */

public interface ConectionPresenters {
    void makeRequest();
    List<Localizacion> getLista(JSONArray jsonArray);
    void onPreStartConnection();
    void onConnectionFinished();
    void onConnectionFailed(String error);
    void addToQueue(Request request);
    List<Localizacion> getListado();
    AsyncTask<Void, Void, String> Extraerdatos();
    void ConnectarAdapter(String result);
    AsyncTask<Void, Void, String> ListaTienda();
}
