package cl.telematica.android.alimentame.POST;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import cl.telematica.android.alimentame.MainActivity;
import cl.telematica.android.alimentame.POST.View.VendedorView;
import cl.telematica.android.alimentame.R;

import static cl.telematica.android.alimentame.MainActivity.MyPREFERENCES;


public class Vendedor extends AppCompatActivity implements VendedorView{
    String User_ID;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor);
        SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        User_ID = sharedpreferences.getString("User_ID", "2");
    }

    @Override
    public void Ir_a_Publicar(View V) {
        Intent explicit_intent = new Intent(Vendedor.this,Publicar.class);
        explicit_intent.putExtra("User_ID",User_ID);
        Vendedor.this.startActivity(explicit_intent);
    }

    @Override
    public void Ir_a_Listar(View v) {
        Intent explicit_intent = new Intent(Vendedor.this,VerProductos.class);
        explicit_intent.putExtra("User_ID",User_ID);
        Vendedor.this.startActivity(explicit_intent);
    }
}
