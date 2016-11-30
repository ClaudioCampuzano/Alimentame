package cl.telematica.android.alimentame.Presenters.Contact;

import android.app.PendingIntent;
import android.view.View;

import com.android.volley.Request;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;

import cl.telematica.android.alimentame.Localizacion;

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

}
