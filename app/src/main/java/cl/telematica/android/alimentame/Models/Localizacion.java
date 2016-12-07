package cl.telematica.android.alimentame.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gerson on 23-10-16.
 */

public class Localizacion implements Parcelable {
    private double latitud;
    private double longitud;
    private String producto;
    private String vendedor;//borrar
    private String nombre;
    private String precio;
    private String descripcion;
    private String imagen;
    private int states;

    public int isStates() {
        return states;
    }

    public void setStates(int states) {
        this.states = states;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public double getLongitud() {
        return longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public String getProducto() {
        return producto;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }
    public Localizacion(){

    }
    public Localizacion(Parcel in) {
        latitud = in.readDouble();
        longitud = in.readDouble();
        producto = in.readString();
        vendedor = in.readString();
        nombre = in.readString();
        precio = in.readString();
        descripcion = in.readString();
        imagen = in.readString();
        states = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitud);
        dest.writeDouble(longitud);
        dest.writeString(producto);
        dest.writeString(vendedor);
        dest.writeString(nombre);
        dest.writeString(precio);
        dest.writeString(descripcion);
        dest.writeString(imagen);
        dest.writeInt(states);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Localizacion> CREATOR = new Parcelable.Creator<Localizacion>() {
        @Override
        public Localizacion createFromParcel(Parcel in) {
            return new Localizacion(in);
        }

        @Override
        public Localizacion[] newArray(int size) {
            return new Localizacion[size];
        }
    };
}
