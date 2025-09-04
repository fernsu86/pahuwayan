package eb.service;

import eb.dao.web_userdao;
import eb.dto.web_userdto;
import eb.util.token_util;
import java.sql.SQLException;
import java.util.List;

public class user_service {

    private final web_userdao userDao = new web_userdao();

    public String login(String username, String password) throws Exception {
        web_userdto user = userDao.authenticate(username, password);
        if (user != null) {
            // include more claims if token_util allows
            return token_util.generateToken(
                    String.valueOf(user.getUser_id()),
                    user.getRole_name()
            );
        } else {
            throw new Exception("Invalid username or password");
        }
    }

    public boolean register(String username, String password, String email, String phone) throws Exception {
        web_userdao dao = new web_userdao();
        return dao.create_tenant_user(username, password, email, phone);
    }

    public boolean createLandlord(String username, String password, String barangay_name, String email, String phone) throws Exception {
        boolean success = userDao.create_landlord_user(username, password, barangay_name, email, phone);
        return success;
    }

    public List<web_userdto> retrieveLandLord(String role_name) throws ClassNotFoundException, SQLException {
        List<web_userdto> landlordList = userDao.retrive_landlord_list(role_name);
        return landlordList;
    }

    public boolean update(
            String user_id,
            String username,
            String password,
            String status,
            String role_name,
            String barangay_name,
            String email,
            String phone) throws ClassNotFoundException, SQLException {

        // Get current user from DB
        web_userdto user = userDao.retrive_user_byid(user_id);
        if (user == null) {
            return false; // no such user
        }

        // Only update if parameter is not null
        if (username != null) {
            user.setUsername(username);
        }
        if (password != null) {
            user.setPassword(password);
        }
        if (status != null) {
            user.setStatus(status);
        }
        if (role_name != null) {
            user.setRole_name(role_name);
        }
        if (barangay_name != null) {
            user.setBarangay_name(barangay_name);
        }
        if (email != null) {
            user.setEmail(email);
        }
        if (phone != null) {
            user.setPhone(phone);
        }

        // Save updated user
        return userDao.update_landlord_user(user);
    }

}
