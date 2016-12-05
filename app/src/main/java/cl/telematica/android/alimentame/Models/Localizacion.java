package cl.telematica.android.alimentame.Models;

/**
 * Created by gerson on 23-10-16.
 */

public class Localizacion {
    private double latitud;
    private double longitud;
    private String producto;
    private String vendedor;//borrar
    private String nombre;
    private String precio;
    private String descripcion;
    private String imagen;
    private boolean states;

    public boolean isStates() {
        return states;
    }

    public void setStates(boolean states) {
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
}
