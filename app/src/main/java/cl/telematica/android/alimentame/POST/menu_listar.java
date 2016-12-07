package cl.telematica.android.alimentame.POST;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import cl.telematica.android.alimentame.POST.View.PublicarView;
import cl.telematica.android.alimentame.POST.View.menu_listarView;
import cl.telematica.android.alimentame.R;

public class menu_listar extends AppCompatActivity implements menu_listarView {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_listar);



    }

    @Override
    public void Modificar(View V) {
        Toast.makeText(getApplicationContext(),"Modificar",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Eliminar(View V) {
        Toast.makeText(getApplicationContext(),"Eliminar",Toast.LENGTH_SHORT).show();
    }
}
