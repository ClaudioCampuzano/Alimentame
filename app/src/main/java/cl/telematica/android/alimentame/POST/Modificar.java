package cl.telematica.android.alimentame.POST;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import cl.telematica.android.alimentame.POST.Models.GPSTracker;
import cl.telematica.android.alimentame.POST.Models.MySingleton;
import cl.telematica.android.alimentame.POST.Presenters.Contract.PublicarPresenters;
import cl.telematica.android.alimentame.POST.Presenters.PublicarImpl;
import cl.telematica.android.alimentame.POST.View.ModificarView;
import cl.telematica.android.alimentame.R;

public class Modificar extends AppCompatActivity implements ModificarView{
    EditText nombre,precio,descripcion;
    String state;
    private RadioButton radioButton1,radioButton2;
    private GPSTracker gps;

    ImageView ivImagen;
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    final int CAMERA_REQUEST = 13323;
    final int GALLERY_REQUEST = 22131;
    //private final String TAG = this.getClass().getName();
    String SelectedPhoto;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);
        nombre = (EditText) findViewById(R.id.Nombre);
        precio = (EditText) findViewById(R.id.Precio);
        descripcion = (EditText) findViewById(R.id.Descripcion);
        radioButton1 = (RadioButton) findViewById(R.id.Disponible);
        radioButton2 = (RadioButton) findViewById(R.id.Nodisponible);

        if(getIntent().getStringExtra("state").equalsIgnoreCase("1"))
            radioButton1.setChecked(true);
        else
            radioButton2.setChecked(true);

        nombre.setText(getIntent().getStringExtra("Nombre"));
        precio.setText(getIntent().getStringExtra("Precio"));
        descripcion.setText(getIntent().getStringExtra("Descripcion"));
        iniciargps();

        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());
        ivImagen = (ImageView) findViewById(R.id.foto_seleccion);
    }

    @Override
    public void publicarMod(View V) {
        final String url = "http://alimentame-multimedios.esy.es/Actualizar.php";
        if (radioButton1.isChecked())
            state = "1";
        else
            state = "0";

        if (!nombre.getText().toString().equalsIgnoreCase("") &&
                !precio.getText().toString().equalsIgnoreCase("") && !descripcion.getText().toString().equalsIgnoreCase("")) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("Nombre", nombre.getText().toString());
            params.put("Precio", precio.getText().toString());
            params.put("Descripcion", descripcion.getText().toString());
            params.put("state", state);
            params.put("Latitud", String.valueOf(gps.getLatitude()));
            params.put("Longitud", String.valueOf(gps.getLongitude()));
            params.put("Imagen", "http://alimentame-multimedios.esy.es/food_mod.jpg");
            params.put("Prod_ID", getIntent().getStringExtra("Prod_ID"));
            JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                VolleyLog.v("Response:%n %s", response.toString(4));
                                Toast.makeText(getApplicationContext(),"Modificado",Toast.LENGTH_LONG).show();
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
            MySingleton.getmInstance(getApplicationContext()).addTorequestque(req);
        } else {
            Toast.makeText(Modificar.this, "Hay informacion por rellenar", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void cameraMod(View V) {
        try {
            startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
            cameraPhoto.addToGallery();
        } catch (IOException e) {
            Toast.makeText(this, "Algo esta mal, imposibru sacar fotos", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void galleryMod(View V) {
        startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);

    }
    public void iniciargps(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Modificar.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            gps = new GPSTracker(getApplicationContext(), Modificar.this);
            if (gps.canGetLocation()) {
            } else {
                gps.showSettingsAlert();
            }
        }
    }

}
