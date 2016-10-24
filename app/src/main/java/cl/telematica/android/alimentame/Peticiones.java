package cl.telematica.android.alimentame;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gerson on 23-10-16.
 */

public class Peticiones {
    private static Peticiones singleton;
    private RequestQueue requestQueue;
    private static Context context;

    public Peticiones(Context context) {
        Peticiones.context=context;
        requestQueue = getRequestQueue();
    }
    public static synchronized Peticiones getInstance(Context context) {
        if (singleton == null) {
            singleton = new Peticiones(context);
        }
        return singleton;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public  void addToRequestQueue(Request req) {
        getRequestQueue().add(req);
    }



}
