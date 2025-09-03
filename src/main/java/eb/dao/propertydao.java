/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eb.dao;

import eb.dto.propertydto;
import eb.dto.web_userdto;
import eb.util.db_util;
import eb.util.helper_util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ACER
 */
public class propertydao {

    public List<propertydto> retrieve_all_property_bylandlordid(String landlordid) throws Exception {
        List<propertydto> propertyList = new ArrayList<>();
        String sql = "SELECT * FROM dbo.property WHERE landlord_id = ?";

        try ( Connection con = db_util.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, landlordid); // ✅ bind the parameter

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    propertydto property = new propertydto(
                            rs.getString("property_id"),
                            rs.getString("barangay_name"),
                            rs.getString("landlord_id"),
                            rs.getString("property_name"),
                            rs.getString("property_address"),
                            rs.getString("property_type"),
                            rs.getString("property_amenity"),
                            rs.getDouble("property_price"),
                            rs.getString("property_status"),
                            rs.getString("description")
                    );
                    propertyList.add(property);
                }
            }
        }
        return propertyList;
    }

    public List<propertydto> retrieve_all_property() throws Exception {
        List<propertydto> propertyList = new ArrayList<>();

        String sql = "SELECT * FROM dbo.property";

        try ( Connection con = db_util.getConnection();  PreparedStatement ps = con.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                propertydto property = new propertydto(
                        rs.getString("property_id"),
                        rs.getString("barangay_name"),
                        rs.getString("landlord_id"),
                        rs.getString("property_name"),
                        rs.getString("property_address"),
                        rs.getString("property_type"),
                        rs.getString("property_amenity"),
                        rs.getDouble("property_price"),
                        rs.getString("property_status"),
                        rs.getString("description")
                );
                propertyList.add(property);
            }
        }
        return propertyList;
    }

    public String create_property(
            String barangay_name,
            String landlord_id,
            String property_name,
            String property_address,
            String property_type,
            String property_amenity,
            double property_price,
            String description
    ) throws Exception {
        String sql = "INSERT INTO dbo.property "
                + "(property_id, barangay_name, landlord_id, property_name, property_address, property_type, property_amenity, property_price, property_status, description) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // generate property_id before insert
        String propertyId = helper_util.generate_property_Uid();
        try ( Connection con = db_util.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, propertyId);
            ps.setString(2, barangay_name);
            ps.setString(3, landlord_id);
            ps.setString(4, property_name);
            ps.setString(5, property_address);
            ps.setString(6, property_type);
            ps.setString(7, property_amenity);
            ps.setDouble(8, property_price);
            ps.setString(9, "pending");
            ps.setString(10, description);

            int rows = ps.executeUpdate();
            System.out.println("DEBUG: Insert rows=" + rows);

            return rows > 0 ? propertyId : null;

        } catch (SQLException e) {
            System.err.println("❌ SQL Error inserting property: " + e.getMessage());
            throw e;
        }
    }

}
