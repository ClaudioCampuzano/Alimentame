package cl.telematica.android.alimentame.POST;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.FileNotFoundException;
import java.io.IOException;

import cl.telematica.android.alimentame.POST.Presenters.Contract.PublicarPresenters;
import cl.telematica.android.alimentame.POST.Presenters.PublicarImpl;
import cl.telematica.android.alimentame.POST.View.PublicarView;
import cl.telematica.android.alimentame.R;

public class Publicar extends AppCompatActivity implements PublicarView {
    private PublicarPresenters mPresenter;
    public EditText Nombre, Precio, Descripcion;
    String User_ID;

    ImageView ivImagen;
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    final int CAMERA_REQUEST = 13323;
    final int GALLERY_REQUEST = 22131;
    //private final String TAG = this.getClass().getName();
    String SelectedPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_publicar);

        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());
        ivImagen = (ImageView) findViewById(R.id.foto_seleccion);


        User_ID = getIntent().getStringExtra("User_ID");
        mPresenter = new PublicarImpl(this);
        Nombre = (EditText) findViewById(R.id.Nombre);
        Precio = (EditText) findViewById(R.id.Precio);
        Descripcion = (EditText) findViewById(R.id.Descripcion);
        mPresenter.InicializarGps();
    }


    @Override
    public void camera(View V) {
        try {
            startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
            cameraPhoto.addToGallery();
        } catch (IOException e) {
            Toast.makeText(this, "Algo esta mal, imposibru sacar fotos", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void gallery(View V) {
        startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                String photoPath = cameraPhoto.getPhotoPath();
                SelectedPhoto = photoPath;
                Bitmap bitmap = null;
                try {
                    bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    ivImagen.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Algo anda mal", Toast.LENGTH_SHORT).show();
                }
            }
            else if (requestCode == GALLERY_REQUEST){
                galleryPhoto.setPhotoUri(data.getData());
                String photoPath = galleryPhoto.getPath();
                SelectedPhoto =photoPath;
                Bitmap bitmap = null;
                try {
                    bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    ivImagen.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Algo anda mal", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void publicar(View V) {
            if (!Nombre.getText().toString().equalsIgnoreCase("") &&
                    !Precio.getText().toString().equalsIgnoreCase("") && !Descripcion.getText().toString().equalsIgnoreCase("")) {

                mPresenter.SetData(Nombre.getText().toString(), Precio.getText().toString(), Descripcion.getText().toString(),
                        User_ID, "1", "puto.png");
                mPresenter.UploadData();

            } else {
                Toast.makeText(Publicar.this, "Hay informacion por rellenar", Toast.LENGTH_LONG).show();
            }
    }
}
