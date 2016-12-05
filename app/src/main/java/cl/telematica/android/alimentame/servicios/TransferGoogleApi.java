package cl.telematica.android.alimentame.servicios;

import cl.telematica.android.alimentame.Presenters.Contact.ConectionPresenters;
import cl.telematica.android.alimentame.Presenters.GoogleApi;

/**
 * Created by gerson on 05-12-16.
 */

public class TransferGoogleApi {
    private static GoogleApi googleApi;
    private static ConectionPresenters conectionPresenters;

    public static GoogleApi getGoogleApi() {
        return googleApi;
    }

    public static void setGoogleApi(GoogleApi googleApi) {
        TransferGoogleApi.googleApi = googleApi;
    }

    public static ConectionPresenters getConectionPresenters() {
        return conectionPresenters;
    }

    public static void setConectionPresenters(ConectionPresenters conectionPresenters) {
        TransferGoogleApi.conectionPresenters = conectionPresenters;
    }
}
