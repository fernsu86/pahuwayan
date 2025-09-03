package eb.dao;

import eb.dto.web_userdto;
import eb.util.db_util;
import eb.util.helper_util;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class web_userdao {
    
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Authenticate user
    public web_userdto authenticate(String username, String password) throws SQLException, ClassNotFoundException {
        String sql = "SELECT user_id, username, password, status, role_name, barangay_name FROM dbo.web_user WHERE username = ?";
        
        try ( Connection conn = db_util.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String dbPassword = rs.getString("password");
                    String status = rs.getString("status");

                    // Compare raw input vs. stored hash
                    if (encoder.matches(password, dbPassword) && "active".equalsIgnoreCase(status)) {
                        return new web_userdto(
                                rs.getString("user_id"),
                                rs.getString("username"),
                                null, // don’t expose password
                                status,
                                rs.getString("role_name"),
                                rs.getString("barangay_name")
                        );
                    }
                }
            }
        }
        return null; // invalid login
    }
    
    public web_userdto authenticateTest(String username, String password) throws SQLException, ClassNotFoundException {
        String sql = "SELECT user_id, username, password, status, role_name, barangay_name FROM dbo.web_user WHERE username = ?";
        
        try ( Connection conn = db_util.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String dbPassword = rs.getString("password");
                    String status = rs.getString("status");

                    // Compare raw input vs. stored hash
                    if ("active".equalsIgnoreCase(status)) {
                        return new web_userdto(
                                rs.getString("user_id"),
                                rs.getString("username"),
                                null, // don’t expose password
                                status,
                                rs.getString("role_name"),
                                rs.getString("barangay_name")
                        );
                    }
                }
            }
        }
        return null; // invalid login
    }

    // Retrieve all users
    public List<web_userdto> retrieve_all_user() throws Exception {
        List<web_userdto> userList = new ArrayList<>();
        
        String sql = "SELECT user_id, username, status FROM dbo.web_user";
        
        try ( Connection con = db_util.getConnection();  PreparedStatement ps = con.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                userList.add(new web_userdto(
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("status")
                ));
            }
        }
        return userList;
    }

    // Create blank user
    public boolean create_blank_user(String username, String password) throws Exception {
        String sql = "INSERT INTO dbo.web_user (user_id, username, password, status) VALUES (?, ?, ?, ?)";
        
        try ( Connection con = db_util.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, helper_util.generate_blank_Uid());
            ps.setString(2, username);
            ps.setString(3, encoder.encode(password)); // hash password
            ps.setString(4, "active");
            
            return ps.executeUpdate() > 0;
        }
    }

    // Create tenant user
    public boolean create_tenant_user(String username, String password, String email, String phone) throws Exception {
        String sql = "INSERT INTO dbo.web_user (user_id, username, password, status, role_name, email, phone) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try ( Connection con = db_util.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, helper_util.generate_blank_Uid());
            ps.setString(2, username);
            ps.setString(3, encoder.encode(password)); // hash password
            ps.setString(4, "active");
            ps.setString(5, "tenant");
            ps.setString(6, email);
            ps.setString(7, phone);
            
            return ps.executeUpdate() > 0;
        }
    }

    // Create landlord user
    public boolean create_landlord_user(String username, String password, String barangay_name, String email, String phone) throws Exception {
        String sql = "INSERT INTO dbo.web_user (user_id, username, password, status, role_name, barangay_name, email, phone) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try ( Connection con = db_util.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, helper_util.generate_blank_Uid());
            ps.setString(2, username);
            ps.setString(3, encoder.encode(password));
            ps.setString(4, "active");
            ps.setString(5, "landlord");
            ps.setString(6, barangay_name);
            ps.setString(7, email);
            ps.setString(8, phone);
            
            return ps.executeUpdate() > 0;
        }
    }
    
    public List<web_userdto> retrive_landlord_list(String role_name) throws ClassNotFoundException, SQLException {
        List<web_userdto> LandlordList = new ArrayList<>();
        String sql = "SELECT * FROM dbo.web_user WHERE role_name = ?";
        try ( Connection conn = db_util.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role_name);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    web_userdto landlord = new web_userdto(rs.getString("user_id"), rs.getString("username"), null, rs.getString("status"), rs.getString("role_name"), rs.getString("barangay_name"), rs.getString("email"), rs.getString("phone"));
                    LandlordList.add(landlord);
                }
            }
        }
        return LandlordList;
    }
}
