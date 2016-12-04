package cl.telematica.android.alimentame.POST;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import cl.telematica.android.alimentame.POST.Presenters.Contract.PublicarPresenters;
import cl.telematica.android.alimentame.POST.Presenters.GPSTracker;
import cl.telematica.android.alimentame.POST.Presenters.MySingleton;
import cl.telematica.android.alimentame.POST.Presenters.PublicarImpl;
import cl.telematica.android.alimentame.R;

public class Publicar extends AppCompatActivity implements PublicarView {
    private PublicarPresenters mPresenter;
    public EditText Nombre, Precio, Descripcion, Imagen;
    GPSTracker gps;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_publicar);
        mPresenter = new PublicarImpl(this);
        Nombre = (EditText)findViewById(R.id.Nombre_prod);
        Precio = (EditText) findViewById(R.id.Precio);
        Descripcion = (EditText)findViewById(R.id.Descripcion);

        mContext = this;
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Publicar.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            gps = new GPSTracker(mContext, Publicar.this);
            // Check if GPS enabled
            if (gps.canGetLocation()) {
                if(String.valueOf(gps.getLongitude()).equalsIgnoreCase("0")&&
                        String.valueOf(gps.getLatitude()).equalsIgnoreCase("0"))
                    RestaurarActividadPrincipal();
            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gps.showSettingsAlert();
            }
        }
    }

    @Override
    public void publicar(View V) {
        if (!Nombre.getText().toString().equalsIgnoreCase("") &&
                !Precio.getText().toString().equalsIgnoreCase("") && !Descripcion.getText().toString().equalsIgnoreCase("")){
            mPresenter.SetData(Nombre.getText().toString(),Precio.getText().toString(),Descripcion.getText().toString(),
                    "1","1",String.valueOf(gps.getLatitude()),String.valueOf(gps.getLongitude()),"puto.png");
            mPresenter.UploadData();
        }else {
            Toast.makeText(Publicar.this,"Hay informacion por rellenar",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void RestaurarActividadPrincipal() {
        Intent i = getApplicationContext().getPackageManager()
                .getLaunchIntentForPackage(getApplicationContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(i);
    }
}
