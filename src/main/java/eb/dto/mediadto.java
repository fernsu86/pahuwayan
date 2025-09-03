/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eb.dto;

/**
 *
 * @author ACER
 */
public class mediadto {
   private String media_url;
   private String media_name;
   private String property_id;

    public mediadto() {
    }

    public mediadto(String media_url, String media_name, String property_id) {
        this.media_url = media_url;
        this.media_name = media_name;
        this.property_id = property_id;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getMedia_name() {
        return media_name;
    }

    public void setMedia_name(String media_name) {
        this.media_name = media_name;
    }

    public String getProperty_id() {
        return property_id;
    }

    public void setProperty_id(String property_id) {
        this.property_id = property_id;
    }
   
}
