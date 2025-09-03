/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eb.dao;

import eb.dto.imagedto;
import eb.dto.mediadto;
import eb.util.db_util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ACER
 */
public class imagedao {

    public boolean add_image(String propertyid, String image_id, String image_path) throws Exception {

        String sql = "INSERT INTO dbo.image (property_id, image_id, image_path) VALUES (?, ?, ?)";

        try ( Connection con = db_util.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, propertyid);
            ps.setString(2, image_id);
            ps.setString(3, image_path);

            return ps.executeUpdate() > 0;

        }

    }

    public List<imagedto> retrieve_image_by_propertyid(String propertyid) throws Exception {
        List<imagedto> imageList = new ArrayList<>();

        String sql = "SELECT image_id, image_path FROM dbo.image WHERE property_id = ?";

        try ( Connection con = db_util.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, propertyid);

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    imageList.add(new imagedto(
                            "image_id",
                            "image_path",
                            propertyid));
                }
            }
        }
        return imageList;
    }
}
