/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eb.dto;

/**
 *
 * @author ACER
 */
public class imagedto {
    private String image_id;
    private String image_path;
    private String property_id;

    public imagedto() {
    }

    public imagedto(String image_id, String image_path, String property_id) {
        this.image_id = image_id;
        this.image_path = image_path;
        this.property_id = property_id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getProperty_id() {
        return property_id;
    }

    public void setProperty_id(String property_id) {
        this.property_id = property_id;
    }

  
    
}
