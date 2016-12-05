package cl.telematica.android.alimentame.Todolodemas.Presenters;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


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
