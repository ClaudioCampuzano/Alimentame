package cl.telematica.android.alimentame.POST;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cl.telematica.android.alimentame.POST.Presenters.Contract.PublicarPresenters;
import cl.telematica.android.alimentame.POST.Presenters.PublicarImpl;
import cl.telematica.android.alimentame.POST.View.PublicarView;
import cl.telematica.android.alimentame.R;

public class Publicar extends AppCompatActivity implements PublicarView {
    private PublicarPresenters mPresenter;
    public EditText Nombre, Precio, Descripcion, Imagen;
    String User_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_publicar);

        User_ID = getIntent().getStringExtra("User_ID");

        mPresenter = new PublicarImpl(this);
        Nombre = (EditText)findViewById(R.id.Nombre_prod);
        Precio = (EditText) findViewById(R.id.Precio);
        Descripcion = (EditText)findViewById(R.id.Descripcion);
        mPresenter.InicializarGps();
    }

    @Override
    public void publicar(View V) {
        if (!Nombre.getText().toString().equalsIgnoreCase("") &&
                !Precio.getText().toString().equalsIgnoreCase("") && !Descripcion.getText().toString().equalsIgnoreCase("")){
            mPresenter.SetData(Nombre.getText().toString(),Precio.getText().toString(),Descripcion.getText().toString(),
                    User_ID,"1","puto.png");
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

    @Override
    public void TomarFoto(View V) {
        Toast.makeText(Publicar.this,"Tomando foto", Toast.LENGTH_SHORT).show();
    }
}
