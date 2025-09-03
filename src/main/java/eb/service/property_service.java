/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eb.service;

import eb.dao.imagedao;
import eb.dao.mediadao;
import eb.dao.propertydao;
import eb.dao.web_userdao;
import eb.dto.imagedto;
import eb.dto.propertydto;
import eb.util.helper_util;
import java.util.List;

/**
 *
 * @author ACER
 */
public class property_service {

    private static final propertydao PDAO = new propertydao();
    private static final web_userdao WDAO = new web_userdao();
    private static final imagedao IDAO = new imagedao();
    private static final mediadao MDAO = new mediadao();

    public String handle_property(String barangay_name, String landlord_id, String property_name, String property_address, String property_type, String property_amenity, double property_price, String description) throws Exception {

        String propertyId = PDAO.create_property(barangay_name, landlord_id, property_name, property_address, property_type, property_amenity, property_price, description);

        return propertyId;
    }

    public List<propertydto> handle_retrieve_property(String landlord_id) throws Exception {
        List<propertydto> propertyList = PDAO.retrieve_all_property_bylandlordid(landlord_id);
        for (propertydto prop : propertyList) {
            List<imagedto> images = IDAO.retrieve_image_by_propertyid(prop.getProperty_id());
            prop.setImages(images);
        }

        return propertyList;
    }
}
