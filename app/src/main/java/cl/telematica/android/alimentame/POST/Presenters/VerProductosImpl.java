package cl.telematica.android.alimentame.POST.Presenters;

import java.util.ArrayList;

import cl.telematica.android.alimentame.POST.Models.BackGroundTask;
import cl.telematica.android.alimentame.POST.Models.Datos;
import cl.telematica.android.alimentame.POST.Presenters.Contract.VerProductosPresenters;
import cl.telematica.android.alimentame.POST.VerProductos;

/**
 * Created by Claudio on 04-12-2016.
 */

public class VerProductosImpl implements VerProductosPresenters {
    private VerProductos mActivity;
    private String json_url,id;

    public VerProductosImpl (VerProductos Activity, String id){
        mActivity = Activity;
        this.id = id;
    }

    @Override
    public void showProd() {
        json_url = "http://alimentame-multimedios.esy.es/QueriesByID.php?id="+ id;
        BackGroundTask backGroundTask = new BackGroundTask(mActivity);
        backGroundTask.getList(json_url, new BackGroundTask.ListRetriever() {
            @Override
            public void onListReceived(ArrayList<Datos> arrayList) {
                mActivity.mostrarResultados(arrayList);
            }
            @Override
            public void onError(String message) {
            }
        });
    }
}
