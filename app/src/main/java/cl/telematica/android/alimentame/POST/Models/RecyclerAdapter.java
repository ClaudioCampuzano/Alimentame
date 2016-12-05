package cl.telematica.android.alimentame.POST.Models;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import cl.telematica.android.alimentame.R;

/**
 * Created by Claudio on 27-10-2016.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    ArrayList<Datos> datos = new ArrayList<>();
    Context ctx;

    public RecyclerAdapter(ArrayList<Datos> datos, Context ctx){
        this.datos=datos;
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view, ctx, datos);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.Nombre.setText(datos.get(position).getNombre()+"\n");
        holder.Descripcion.setText(description(datos.get(position).getDescripcion()));
        holder.Precio.setText(datos.get(position).getPrecio());
    }
    public String description (String description){
        if (description == "null")
            return "Sin descripción disponible\n";
        else
            return description+"\n";
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView Nombre,Descripcion,Precio;
        ArrayList<Datos> datos = new ArrayList<Datos>();
        Context ctx;

        public MyViewHolder(View itemView, Context ctx, ArrayList<Datos> datos) {
            super(itemView);
            this.datos = datos;
            this.ctx = ctx;

            itemView.setOnClickListener(this);
            Nombre = (TextView) itemView.findViewById(R.id.prod_nombre);
            Descripcion = (TextView) itemView.findViewById(R.id.prod_descripcion);
            Precio = (TextView) itemView.findViewById(R.id.prod_precio);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(ctx,"Gracias por hacer click",Toast.LENGTH_LONG).show();
        }
    }
}
