package cl.telematica.android.alimentame;

/**
 * Created by gerson on 23-10-16.
 */

public class Localizacion {
    private double latitud;
    private double longitud;
    private String producto;
    private String vendedor;

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
