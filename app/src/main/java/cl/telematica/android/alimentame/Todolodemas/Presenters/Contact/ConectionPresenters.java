package cl.telematica.android.alimentame.Todolodemas.Presenters.Contact;

import com.android.volley.Request;

import org.json.JSONArray;

import java.util.List;

import cl.telematica.android.alimentame.Todolodemas.Presenters.Localizacion;

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
