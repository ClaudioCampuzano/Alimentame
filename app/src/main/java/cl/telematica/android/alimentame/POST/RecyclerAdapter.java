package cl.telematica.android.alimentame.POST;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cl.telematica.android.alimentame.POST.Models.Datos;
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

        String yyyyMMdd = datos.get(position).getUpdated_at().split("T")[0];
        String Fecha = yyyyMMdd.split("-")[2]+"-"+yyyyMMdd.split("-")[1]+"-"+yyyyMMdd.split("-")[0];

        holder.Name.setText(datos.get(position).getName()+"\n");
        holder.Description.setText(description(datos.get(position).getDescription()));
        holder.Updated_at.setText("Última actualización: "+Fecha);
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
        TextView Name,Description,Updated_at;
        ArrayList<Datos> datos = new ArrayList<Datos>();
        Context ctx;

        public MyViewHolder(View itemView, Context ctx, ArrayList<Datos> datos) {
            super(itemView);
            this.datos = datos;
            this.ctx = ctx;

            itemView.setOnClickListener(this);
            Name = (TextView) itemView.findViewById(R.id.prod_nombre);
            Description = (TextView) itemView.findViewById(R.id.prod_descripcion);
            Updated_at = (TextView) itemView.findViewById(R.id.prod_precio);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Datos datos = this.datos.get(position);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(datos.getHtml_url()));
            this.ctx.startActivity(intent);
        }
    }
}
