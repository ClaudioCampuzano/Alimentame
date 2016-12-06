package cl.telematica.android.alimentame.Servicio;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import cl.telematica.android.alimentame.Presenters.Contact.ConectionPresenters;
import cl.telematica.android.alimentame.Presenters.GoogleApi;

/**
 * Created by gerson on 06-12-16.
 */

public class ServiceUpdate extends Service {
    private static final String TAG = ServiceUpdate.class.getSimpleName();
    TimerTask timerTask;
    GoogleApi googleApi;
    private ConectionPresenters conectionPresenters;



    public ServiceUpdate() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate(){
        Log.d(TAG,"Servicio creado...");



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        googleApi = TransferGoogleApi.getGoogleApi();
        conectionPresenters = TransferGoogleApi.getConectionPresenters();
        Log.d(TAG, "Servicio iniciado...");
        Timer timer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                conectionPresenters.makeRequest();
                System.out.println("actulizando..");

            }
        };

        timer.scheduleAtFixedRate(timerTask, 0, 300000);

        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        timerTask.cancel();
        Log.d(TAG, "Servicio destruido...");
    }


}

