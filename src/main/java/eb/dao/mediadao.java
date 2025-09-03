/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eb.dao;

import eb.dto.mediadto;
import eb.dto.web_userdto;
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
public class mediadao {
 public boolean add_image(String propertyid, String media_url, String media_name) throws Exception {

        String sql = "INSERT INTO dbo.media (property_id, media_url, media_name) VALUES (?, ?, ?)";

        try ( Connection con = db_util.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, propertyid);
            ps.setString(2, media_url);
            ps.setString(3, media_name);

            return ps.executeUpdate() > 0;

        }

    }
    public List<mediadto> retrieve_image_by_propertyid(String propertyid) throws Exception {
        List<mediadto> mediaList = new ArrayList<>();

        String sql = "SELECT media_url, media_name FROM dbo.media WHERE property_id = ?";

        try ( Connection con = db_util.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, propertyid);

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mediaList.add(new mediadto(
                            rs.getString("media_url"),
                            rs.getString("media_name"),
                            propertyid
                    ));
                }
            }
        }
        return mediaList;
    }
}
