package cl.telematica.android.alimentame;

/**
 * Created by gerson on 23-10-16.
 */

public class localizacion {
    private double latitud;
    private double longitud;
    private String producto;
    private String vendedor;


    public void setLatitud(double latitud){
        this.latitud=latitud;
    }
    public void setLongitud(double longitud){
        this.longitud=longitud;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getVendedor() {
        return vendedor;
    }

    public String getProducto() {
        return producto;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }
}
