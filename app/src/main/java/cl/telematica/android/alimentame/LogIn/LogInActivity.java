package cl.telematica.android.alimentame.LogIn;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import cl.telematica.android.alimentame.MainActivity;
import cl.telematica.android.alimentame.POST.Models.MySingleton;
import cl.telematica.android.alimentame.R;

import static com.android.volley.Request.Method.POST;

public class LogInActivity extends AppCompatActivity {

    private EditText userT, passT;
    private String user;
    private String pass;
    private String hashPass;
    private Button buttonLogin, buttonSignUp;
    String url = "http://alimentame-multimedios.esy.es/login.php";
    AlertDialog.Builder builder;
    private String Mresponse;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        userT = ((EditText) findViewById(R.id.signinUser));
        passT = ((EditText) findViewById(R.id.signinPass));
        buttonLogin = (Button) findViewById(R.id.signinButton);
        buttonSignUp = (Button)findViewById(R.id.signUp);
        builder = new AlertDialog.Builder(LogInActivity.this);
        Mresponse = "";
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

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
                    String hpass = bin2hex(digest(pass));
                    Toast.makeText(LogInActivity.this, hpass, Toast.LENGTH_LONG).show();
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
                            //Toast.makeText(LogInActivity.this, "La respuesta es: "+ response, Toast.LENGTH_LONG).show();

                            if(!response.equals("false")){
                                //Toast.makeText(LogInActivity.this, ":S -> "+response, Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    editor.putString("name", (String) jsonObject.get("name"));
                                    editor.putString("hpass", hashPass);
                                    editor.putString("desc", (String) jsonObject.get("desc"));
                                    editor.putString("User_ID",(String)jsonObject.get("id"));
                                    editor.commit();
                                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                    startActivity(intent);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                /*
                                Toast.makeText(LogInActivity.this, "Por la reCTM!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                intent.putExtra("id", response);
                                startActivity(intent);*/
                            }else{
                                Toast.makeText(LogInActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                            }
                            //Toast.makeText(v.getContext(), "Insercion exitosa", Toast.LENGTH_LONG).show();
                            //restartFirstActivity();
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
                                params.put("pass", bin2hex(digest(pass)));
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                            /*try {
                                params.put("pass", bin2hex(digest(pass)));
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }*/
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
    static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length*2) + "X", new BigInteger(1, data));
    }

}