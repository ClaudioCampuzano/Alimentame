package cl.telematica.android.alimentame.POST;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import cl.telematica.android.alimentame.POST.View.VendedorView;
import cl.telematica.android.alimentame.R;

public class Vendedor extends AppCompatActivity implements VendedorView{
    String User_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor);
        User_ID = "3";
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
