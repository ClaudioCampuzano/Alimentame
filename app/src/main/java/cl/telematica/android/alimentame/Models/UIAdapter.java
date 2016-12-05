package cl.telematica.android.alimentame.Models;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cl.telematica.android.alimentame.R;

/**
 * Created by gerson on 03-12-16.
 */

public class UIAdapter extends RecyclerView.Adapter<UIAdapter.ViewHolder> {
    private List<Localizacion> lista;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView TextTitulo;
        public TextView TextDescrip;
        public TextView Textprecio;
        public ImageView imagen;
        public CardView cardView;
        List<Localizacion> list;
        Context mContext;


        public ViewHolder(View v,List<Localizacion> list,Context mContext) {
            super(v);
            this.list=list;
            this.mContext = mContext;
            v.setOnClickListener(this);
            TextTitulo = (TextView) v.findViewById(R.id.textTitulo);
            TextDescrip = (TextView) v.findViewById(R.id.textDescricion);
            Textprecio = (TextView) v.findViewById(R.id.textprecio);
            cardView = (CardView) v.findViewById(R.id.cv);
            imagen = (ImageView) v.findViewById(R.id.imagen);


        }

        @Override
        public void onClick(View view) {
            /*int position = getAdapterPosition();
            Localizacion dato = list.get(position);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            ntent.setData(Uri.parse(dato.getUrl()));
            this.mContext.startActivity(intent);*/
            Toast.makeText(mContext,"abrir perfil del tienda",Toast.LENGTH_SHORT).show();
        }
    }
    public UIAdapter(List<Localizacion> datos,Context context) {
        lista = datos;
        this.context = context;
    }

    @Override
    public UIAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vista_tienda, parent, false);
        return new ViewHolder(v,lista,context);
    }

    @Override
    public void onBindViewHolder(UIAdapter.ViewHolder holder, int position) {

        final Localizacion dato = lista.get(position);
        holder.TextTitulo.setText(dato.getNombre());
        if (dato.getDescripcion().equals("null")) {
            holder.TextDescrip.setText("No hay informacion");
        } else {
            holder.TextDescrip.setText(dato.getDescripcion());
        }
        holder.Textprecio.setText(dato.getPrecio());
        holder.imagen.getResources().getDrawable(R.drawable.tocomple);
        //downloadFile(dato.getImagen(),holder.imagen);

    }


    @Override
    public int getItemCount() {
        return lista.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    void downloadFile(String imageHttpAddress,ImageView imagen) {
        Bitmap loadedImage;
        URL imageUrl = null;
        try {
            imageUrl = new URL(imageHttpAddress);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            loadedImage = BitmapFactory.decodeStream(conn.getInputStream());
            imagen.setImageBitmap(loadedImage);
        } catch (IOException e) {
            Toast.makeText(context.getApplicationContext(), "Error cargando la imagen: "+e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}

