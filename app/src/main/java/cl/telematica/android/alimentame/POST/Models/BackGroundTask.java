package cl.telematica.android.alimentame.POST.Models;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Claudio on 27-10-2016.
 */

public class BackGroundTask {

    Context context;
    ArrayList<Datos> arrayList = new ArrayList<>();

    public BackGroundTask(Context context){
        this.context = context;
    }

    public void getList(String json_url, final ListRetriever retriever) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, json_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int count = 0;
                        while (count < response.length()) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(count);
                                Datos datos = new Datos(jsonObject.getString("Nombre"), jsonObject.getString("Descripcion"),
                                        jsonObject.getString("state"), jsonObject.getString("Precio"), jsonObject.getString("Latitud"),
                                        jsonObject.getString("Imagen"), jsonObject.getString("Longitud"));
                                arrayList.add(datos);
                                count++;
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Error1: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                        retriever.onListReceived(arrayList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage(), context);
                retriever.onError(error.getMessage());
            }
        });
        MySingleton.getmInstance(context).addTorequestque(jsonArrayRequest);
    }

    public interface ListRetriever {
        void onListReceived(ArrayList<Datos> arrayList);
        void onError(String message);
    }
}
