package cl.telematica.android.alimentame.POST.Presenters.Contract;

/**
 * Created by gerson on 07-12-16.
 */

public interface PublicarPresenters {
    void SetData(String Nombre, String Precio, String Descripcion, String User_ID, String State, String Imagen);
    void UploadData();
    void InicializarGps();
}
