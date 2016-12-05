package cl.telematica.android.alimentame;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cl.telematica.android.alimentame.Models.HttpServerConnection;


public class MainActivity extends AppCompatActivity {
    Button boton;
    Button registro;
    EditText user;
    EditText pass;
    String username,password;
    private static String LOGIN_URL ="http://alimentame-multimedios.esy.es/conectar_usuario.php";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = (EditText)findViewById(R.id.signinUser);
        pass = (EditText)findViewById(R.id.signinPass);
        boton = (Button)findViewById(R.id.signinButton);
        registro =(Button)findViewById(R.id.signUp);


        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = user.getText().toString();
                password = pass.getText().toString();
                AsyncTask<Void, Void, String > task = new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        String respuesta = new HttpServerConnection()
                                .connectToServer(LOGIN_URL+"?"+"username="+username
                                        +"&"+"password="+password,15000);
                        return respuesta;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        System.out.println(s);
                        if(s.isEmpty()){
                            Toast.makeText(getApplicationContext(),"usuario erroneo",Toast.LENGTH_SHORT).show();
                        }else{
                            Intent intent = new Intent(MainActivity.this,MenuActivity.class);
                            startActivity(intent);
                        }
                    }
                }.execute();
            }
        });
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(MainActivity.this,RegistroActivity.class);
                startActivity(x);
            }
        });


    }




}