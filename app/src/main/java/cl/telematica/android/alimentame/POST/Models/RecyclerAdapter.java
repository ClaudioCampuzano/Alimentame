package cl.telematica.android.alimentame.POST.Models;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Nombre,Descripcion,Precio;
        Button modificar, eliminar;
        ImageView State;
        ArrayList<Datos> datos = new ArrayList<Datos>();
        Context ctx;

        public MyViewHolder(View itemView, Context ctx, ArrayList<Datos> datos) {
            super(itemView);
            this.datos = datos;
            this.ctx = ctx;

            Nombre = (TextView) itemView.findViewById(R.id.prod_nombre);
            Descripcion = (TextView) itemView.findViewById(R.id.prod_descripcion);
            Precio = (TextView) itemView.findViewById(R.id.prod_precio);
            State = (ImageView) itemView.findViewById(R.id.ESTADOFOTO);
            modificar= (Button) itemView.findViewById(R.id.Modificar);
            eliminar= (Button) itemView.findViewById(R.id.Eliminar);
        }
    }

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
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.Nombre.setText("Producto: " + datos.get(position).getNombre() + "\n");
        holder.Descripcion.setText("Descripcion: " + description(datos.get(position).getDescripcion()));
        holder.Precio.setText("$" + datos.get(position).getPrecio());
        if(datos.get(position).getState().equals("0"))
            holder.State.setImageResource(R.drawable.rojo);
        else
            holder.State.setImageResource(R.drawable.verde);
        holder.modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx,datos.get(position).getProd_ID(),Toast.LENGTH_SHORT).show();
            }
        });
        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ctx,datos.get(position).getProd_ID(),Toast.LENGTH_SHORT).show();
            }
        });
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
}