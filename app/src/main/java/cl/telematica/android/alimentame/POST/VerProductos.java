package cl.telematica.android.alimentame.POST;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import cl.telematica.android.alimentame.POST.Models.Datos;
import cl.telematica.android.alimentame.POST.Models.RecyclerAdapter;
import cl.telematica.android.alimentame.POST.Presenters.Contract.VerProductosPresenters;
import cl.telematica.android.alimentame.POST.Presenters.VerProductosImpl;
import cl.telematica.android.alimentame.POST.View.VerProductosView;
import cl.telematica.android.alimentame.R;

public class VerProductos extends AppCompatActivity implements VerProductosView {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private VerProductosPresenters mPresenter;
    private String id_importado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_productos);
        id_importado = getIntent().getStringExtra("User_ID");
        mPresenter = new VerProductosImpl(this,id_importado);
        mPresenter.showProd();

    }

    @Override
    public void mostrarResultados(ArrayList<Datos> arreglo_datos) {
        recyclerView = (RecyclerView) findViewById(R.id.recycleyView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(arreglo_datos, this);
        recyclerView.setAdapter(adapter);
    }
}
