package cl.telematica.android.alimentame.servicios;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cl.telematica.android.alimentame.Constants;
import cl.telematica.android.alimentame.Models.Peticiones;
import cl.telematica.android.alimentame.Presenters.ConectionPresentersImpl;
import cl.telematica.android.alimentame.Presenters.Contact.ConectionPresenters;
import cl.telematica.android.alimentame.Presenters.GoogleApi;

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

        timer.scheduleAtFixedRate(timerTask, 0, 60000);

        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        timerTask.cancel();
        Log.d(TAG, "Servicio destruido...");
    }


}
