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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import cl.telematica.android.alimentame.MainActivity;
import cl.telematica.android.alimentame.POST.MySingleton;
import cl.telematica.android.alimentame.R;

import static com.android.volley.Request.Method.POST;

public class LogInActivity extends AppCompatActivity {

    private EditText userT, passT;
    private String user;
    private String pass;
    private String hashPass;
    private Button buttonLogin, buttonSignUp;
    private String url = "http://alimentame-multimedios.esy.es/login.php";
    private AlertDialog.Builder builder;
    private UsuarioLogged Usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        userT = ((EditText) findViewById(R.id.signinUser));
        passT = ((EditText) findViewById(R.id.signinPass));
        buttonLogin = (Button) findViewById(R.id.signinButton);
        buttonSignUp = (Button)findViewById(R.id.signUp);
        builder = new AlertDialog.Builder(LogInActivity.this);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this, SignUp.class);
                startActivity(intent);
            }
        }
        );
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String user, pass;
                user = userT.getText().toString();
                pass = passT.getText().toString();
                try {
                    String hpass = new String(digest(pass), "UTF-8");
                    Toast.makeText(LogInActivity.this, hpass, Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                if (!user.equalsIgnoreCase("") &&
                        !pass.equalsIgnoreCase("")) {
                    StringRequest stringRequest = new StringRequest(POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            builder.setMessage(response);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    userT.setText("");
                                    passT.setText("");
                                }
                            }
                            );
                            if(response != "false"){
                                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                intent.putExtra("logged", getUsuario(response));
                                intent.putExtra("id", response);
                                startActivity(intent);
                            }
                            Toast.makeText(LogInActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LogInActivity.this, "Error", Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("user", user);
                            try {
                                params.put("pass", new String(digest(pass), "UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                            return params;
                        }
                    };
                    MySingleton.getmInstance(LogInActivity.this).addTorequestque(stringRequest);
                } else
                    Toast.makeText(LogInActivity.this, "Hay informacion por rellenar", Toast.LENGTH_LONG).show();
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
    public String getUsuario(final String ID) {
        final String n_url = "http://alimentame-multimedios.esy.es/getuser.php";
        final String[] respuesta = new String[1];
        final UsuarioLogged[] r_user = new UsuarioLogged[1];
        StringRequest stringRequest = new StringRequest(POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                builder.setMessage(response);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                respuesta[0] = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LogInActivity.this, "Error", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", ID);
                return params;
            }
        };
        MySingleton.getmInstance(LogInActivity.this).addTorequestque(stringRequest);
        return respuesta[0];
    }
}