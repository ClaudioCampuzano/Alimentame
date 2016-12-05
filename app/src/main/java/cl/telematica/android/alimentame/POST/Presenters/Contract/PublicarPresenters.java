package cl.telematica.android.alimentame.POST.Presenters.Contract;

/**
 * Created by Claudio on 03-12-2016.
 */

public interface PublicarPresenters {
    void SetData(String Nombre, String Precio, String Descripcion, String User_ID, String State, String Imagen);
    void UploadData();
    void UpListar(String user_id);
    void InicializarGps();

}
