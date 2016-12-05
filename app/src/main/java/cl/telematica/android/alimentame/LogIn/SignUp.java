package cl.telematica.android.alimentame.LogIn;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import cl.telematica.android.alimentame.POST.MySingleton;
import cl.telematica.android.alimentame.R;

import static com.android.volley.Request.Method.POST;

public class SignUp extends AppCompatActivity {

    private EditText userT, passT, descT, imgT;
    private Button btn;
    private String hashPass;

    String url = "http://alimentame-multimedios.esy.es/signup.php";
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        userT = (EditText)findViewById(R.id.signupUser);
        passT = (EditText)findViewById(R.id.signupPass);
        descT = (EditText)findViewById(R.id.signupDesc);
        imgT = (EditText)findViewById(R.id.signupImg);
        btn = (Button)findViewById(R.id.signupButton);
        builder = new AlertDialog.Builder(SignUp.this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String user, pass, desc, img;
                user = userT.getText().toString();
                pass = passT.getText().toString();
                desc = descT.getText().toString();
                img = imgT.getText().toString();
                try {
                    hashPass = bin2hex(digest(pass));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                if (!user.equalsIgnoreCase("") && !pass.equalsIgnoreCase("")) {
                    StringRequest stringRequest = new StringRequest(POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            builder.setMessage(response);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            userT.setText("");
                                            passT.setText("");
                                            descT.setText("");
                                            imgT.setText("");
                                        }
                                    }
                            );
                            Toast.makeText(SignUp.this, "Respuesta = "+response, Toast.LENGTH_LONG).show();
                            if(response == "true"){
                                Intent intent = new Intent(SignUp.this, LogInActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(SignUp.this, "Nombre de usuario en uso", Toast.LENGTH_SHORT).show();
                            }
                            //Toast.makeText(v.getContext(), "Insercion exitosa", Toast.LENGTH_LONG).show();
                            //restartFirstActivity();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(SignUp.this, "Error", Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            try {
                                params.put("pass", bin2hex(digest(pass)));
                            } catch (NoSuchAlgorithmException e) {
                                Toast.makeText(SignUp.this, "D:?", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                            params.put("user", user);
                            params.put("desc", desc);
                            params.put("img", img);
                            return params;
                        }
                    };
                    MySingleton.getmInstance(SignUp.this).addTorequestque(stringRequest);
                } else
                    Toast.makeText(SignUp.this, "Hay informacion por rellenar", Toast.LENGTH_LONG).show();
            }
        });
    }

    //Algoritmo para codificar la password
    public byte[] digest(String value) throws NoSuchAlgorithmException {
        MessageDigest digester = MessageDigest.getInstance("SHA-256");

        byte[] stringBytes = value.getBytes();

        digester.update(stringBytes, 0, stringBytes.length);

        return digester.digest();
    }

    static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length*2) + "X", new BigInteger(1, data));
    }
}
