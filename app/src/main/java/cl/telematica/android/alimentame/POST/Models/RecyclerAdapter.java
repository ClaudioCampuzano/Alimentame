package cl.telematica.android.alimentame.POST.Models;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cl.telematica.android.alimentame.POST.Modificar;
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.Nombre.setText("Producto: " + datos.get(position).getNombre() + "\n");
        holder.Descripcion.setText("Descripcion: " + description(datos.get(position).getDescripcion()));
        holder.Precio.setText("$" + datos.get(position).getPrecio());
        final String url = "http://alimentame-multimedios.esy.es/Borrar.php";
        if(datos.get(position).getState().equals("0"))
            holder.State.setImageResource(R.drawable.rojo);
        else
            holder.State.setImageResource(R.drawable.verde);
        holder.modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, Modificar.class);
                intent.putExtra("Nombre",datos.get(position).getNombre());
                intent.putExtra("Descripcion",datos.get(position).getDescripcion());
                intent.putExtra("Precio",datos.get(position).getPrecio());
                intent.putExtra("state",datos.get(position).getState());
                intent.putExtra("Imagen",datos.get(position).getImagen());
                intent.putExtra("Prod_ID",datos.get(position).getProd_ID());
                ctx.startActivity(intent);
            }
        });
        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
                alertDialogBuilder.setTitle("Eliminacion de tienda").setMessage("¿Esta seguro?")
                        .setCancelable(true)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HashMap<String, String> params = new HashMap<String, String>();
                                params.put("Prod_ID", datos.get(position).getProd_ID());
                                JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(params),
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    VolleyLog.v("Response:%n %s", response.toString(4));
                                                    Toast.makeText(ctx,"Eliminado",Toast.LENGTH_LONG).show();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        VolleyLog.e("Error: ", error.getMessage());
                                    }
                                });
                                MySingleton.getmInstance(ctx).addTorequestque(req);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(ctx,"No eliminado",Toast.LENGTH_LONG).show();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
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