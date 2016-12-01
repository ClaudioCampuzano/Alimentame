package cl.telematica.android.alimentame.LogIn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cl.telematica.android.alimentame.MainActivity;
import cl.telematica.android.alimentame.R;

public class LogInActivity extends AppCompatActivity {

    private EditText userT, passT;
    private String user;
    private String pass;
    private String hashPass;
    private Button buttonLogin;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        userT = ((EditText)findViewById(R.id.signinUser));
        passT = ((EditText)findViewById(R.id.signinPass));
        buttonLogin = (Button)findViewById(R.id.signinButton);



    }

    public void LogIn(){
        try {
            hashPass = digest(pass).toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        user = userT.getText().toString();
        pass = passT.getText().toString();
        if(checkDB(user, pass) == "true"){
            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
            intent.putExtra("logged", "true");
            startActivity(intent);
        }

    }


    public String checkDB(String user, String psw){
        String data = null;
        try {
            data = URLEncoder.encode("user", "UTF-8")
                    + "=" + URLEncoder.encode(user, "UTF-8");
            data += "&" + URLEncoder.encode("psw", "UTF-8") + "="
                    + URLEncoder.encode(psw, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String text = "";
        BufferedReader reader=null;
        URL url = null;
        try {
            // Defined URL  where to send data
            url = new URL("alimentame-multimedios.esy.es/login.php");
            // Send POST data request
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();
            // Get the server response
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            // Read Server Response
            while((line = reader.readLine()) != null) {
                // Append server response in string
                sb.append(line + "\n");
            }
            text = sb.toString();
        }
        catch(Exception ex) {
        }
        finally {
            try {
                reader.close();
            }
            catch(Exception ex) {}
        }
        // Show response on activity
        return text;
    }


    //Algoritmo para codificar la password
    public byte[] digest(String value) throws NoSuchAlgorithmException {
        MessageDigest digester = MessageDigest.getInstance("SHA-256");

        byte[] stringBytes = value.getBytes();

        digester.update(stringBytes, 0, stringBytes.length);

        return digester.digest();
    }
}
