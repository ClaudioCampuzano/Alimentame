package cl.telematica.android.alimentame.POST.Models;

/**
 * Created by Claudio on 30-09-2016.
 */

public class Datos {
    private String name;
    private String description;
    private String updated_at;
    private String html_url;

    public Datos(String name, String description, String updated_at, String html_url){
        this.setName(name);
        this.setDescription(description);
        this.setUpdated_at(updated_at);
        this.setHtml_url(html_url);
    }
    public String getName(){return name;}
    public String getDescription(){return description;}
    public String getUpdated_at() {return updated_at;}
    public String getHtml_url(){return html_url;}

    public void setName(String name){this.name=name;}
    public void setDescription(String description) {this.description=description;}
    public void setUpdated_at(String updated_at)  {this.updated_at=updated_at;}
    public void setHtml_url(String html_url){this.html_url=html_url;}
}
