package cl.telematica.android.alimentame.LogIn;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jc on 04-12-2016.
 */

public class UserInfo {
    public String nombre, descripcion,img, id;

    public UserInfo(String info, String id){
        try {
            JSONObject jsonObject = new JSONObject(info);
            this.nombre = jsonObject.getString("name");
            this.descripcion = jsonObject.getString("desc");
            this.img = jsonObject.getString("img");
            this.id = id;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
