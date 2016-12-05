package cl.telematica.android.alimentame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import cl.telematica.android.alimentame.Presenters.ConectionPresentersImpl;
import cl.telematica.android.alimentame.Presenters.Contact.ConectionPresenters;

public class TiendaActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ConectionPresenters conectionPresenters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        conectionPresenters = new ConectionPresentersImpl(this,recyclerView);
        conectionPresenters.Extraerdatos().execute();
    }
}
