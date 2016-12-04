package cl.telematica.android.alimentame.LogIn;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jc on 03-12-2016.
 */

public class UsuarioLogged {
    private String user, description, image;
    private int ID;

    public UsuarioLogged(JSONObject response, String ID){
        try {
            setID(Integer.parseInt(ID));
            setDescription(response.getString("desc"));
            setUser(response.getString("user"));
            setImage(response.getString("img"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
