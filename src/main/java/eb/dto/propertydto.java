/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eb.dto;

import java.util.List;

/**
 *
 * @author ACER
 */
public class propertydto {

    private String property_id;
    private String barangay_name;
    private String landlord_id;
    private String property_name;
    private String property_address;
    private String property_type;
    private String property_amenity;
    private double property_price;
    private String property_status;
    private String description;
    //media and image;
    private List<imagedto> images;
    private List<mediadto> media;

    public propertydto() {
    }

    public propertydto(String property_id, String barangay_name, String landlord_id, String property_name, String property_address, String property_type, String property_amenity, double property_price, String property_status, String description) {
        this.property_id = property_id;
        this.barangay_name = barangay_name;
        this.landlord_id = landlord_id;
        this.property_name = property_name;
        this.property_address = property_address;
        this.property_type = property_type;
        this.property_amenity = property_amenity;
        this.property_price = property_price;
        this.property_status = property_status;
        this.description = description;
    }

    public propertydto(String property_id, String barangay_name, String landlord_id, String property_name, String property_address, String property_type, String property_amenity, double property_price, String property_status, String description, List<imagedto> images, List<mediadto> media) {
        this.property_id = property_id;
        this.barangay_name = barangay_name;
        this.landlord_id = landlord_id;
        this.property_name = property_name;
        this.property_address = property_address;
        this.property_type = property_type;
        this.property_amenity = property_amenity;
        this.property_price = property_price;
        this.property_status = property_status;
        this.description = description;
        this.images = images;
        this.media = media;
    }

    public String getProperty_id() {
        return property_id;
    }

    public void setProperty_id(String property_id) {
        this.property_id = property_id;
    }

    public String getBarangay_name() {
        return barangay_name;
    }

    public void setBarangay_name(String barangay_name) {
        this.barangay_name = barangay_name;
    }

    public String getLandlord_id() {
        return landlord_id;
    }

    public void setLandlord_id(String landlord_id) {
        this.landlord_id = landlord_id;
    }

    public String getProperty_name() {
        return property_name;
    }

    public void setProperty_name(String property_name) {
        this.property_name = property_name;
    }

    public String getProperty_address() {
        return property_address;
    }

    public void setProperty_address(String property_address) {
        this.property_address = property_address;
    }

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getProperty_amenity() {
        return property_amenity;
    }

    public void setProperty_amenity(String property_amenity) {
        this.property_amenity = property_amenity;
    }

    public double getProperty_price() {
        return property_price;
    }

    public void setProperty_price(double property_price) {
        this.property_price = property_price;
    }

    public String getProperty_status() {
        return property_status;
    }

    public void setProperty_status(String property_status) {
        this.property_status = property_status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<imagedto> getImages() {
        return images;
    }

    public void setImages(List<imagedto> images) {
        this.images = images;
    }

    public List<mediadto> getMedia() {
        return media;
    }

    public void setMedia(List<mediadto> media) {
        this.media = media;
    }

}
