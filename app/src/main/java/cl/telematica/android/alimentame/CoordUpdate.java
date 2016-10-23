package cl.telematica.android.alimentame;

import cl.telematica.android.alimentame.MainActivity;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gerson on 23-10-16.
 */

public class CoordUpdate  extends AsyncTask<Void, Void, String>{
    private HashMap<String,LatLng> area;

    public CoordUpdate(HashMap<String,LatLng> a){
        area=a;
    }

    public HashMap<String, LatLng> getArea() {
        return area;
    }

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(Void... params) {
        String resultado = new HttpServerConnection().connectToServer("http://alimentame-multimedios.esy.es/obtener_coordenadas.php", 15000);
        return resultado;
    }
    @Override
    protected void onPostExecute(String result) {
        if(result != null){
            //System.out.println(result);
            List<Localizacion> lista = new ArrayList<Localizacion>();
            lista = getLista(result);
            for(int i=0;i<lista.size();i++){
                String vendedor = lista.get(i).getVendedor();
                String producto = lista.get(i).getProducto();
                area.put(vendedor + " vende: "+producto,
                        new LatLng(lista.get(i).getLatitud(),lista.get(i).getLongitud()));

            }
            // Get the geofences used. Geofence data is hard coded in this sample.
            //populateGeofenceList();
            // Kick off the request to build GoogleApiClient.
            //buildGoogleApiClient();
        }
    }

    private List<Localizacion> getLista(String result){
        List<Localizacion> listaLocalizaciones = new ArrayList<Localizacion>();
        try {
            JSONArray lista = new JSONArray(result);

            int size = lista.length();
            for(int i = 0; i < size; i++){
                Localizacion localizacion = new Localizacion();
                JSONObject objeto = lista.getJSONObject(i);
                localizacion.setLatitud(objeto.getDouble("latitud"));
                localizacion.setLongitud(objeto.getDouble("longitud"));
                localizacion.setVendedor(objeto.getString("vendedor"));
                localizacion.setProducto(objeto.getString("producto"));
                listaLocalizaciones.add(localizacion);
            }
            return listaLocalizaciones;
        } catch (JSONException e) {
            e.printStackTrace();
            return listaLocalizaciones;
        }
    }
}



