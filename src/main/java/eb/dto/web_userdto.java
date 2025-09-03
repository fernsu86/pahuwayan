package eb.dto;

/**
 *
 * @author ACER
 */
public class web_userdto {

    private String user_id;
    private String username;
    private String password;
    private String status;
    private String role_name;
    private String barangay_name;
    private String email;
    private String phone;

    // Full constructor (with role + barangay name + email + phone)
    public web_userdto(String user_id, String username, String password, String status, String role_name, String barangay_name, String email, String phone) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.status = status;
        this.role_name = role_name;
        this.barangay_name = barangay_name;
        this.email = email;
        this.phone = phone;
    }

    // Full constructor (with role + email + phone)
    public web_userdto(String user_id, String username, String password, String status, String role_name, String email, String phone) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.status = status;
        this.role_name = role_name;
        this.email = email;
        this.phone = phone;
    }

    // Full constructor (with role + barangay name)
    public web_userdto(String user_id, String username, String password, String status, String role_name, String barangay_name) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.status = status;
        this.role_name = role_name;
        this.barangay_name = barangay_name;
    }

    // Without barangay_name
    public web_userdto(String user_id, String username, String password, String status, String role_name) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.status = status;
        this.role_name = role_name;
    }

    // Constructor without role
    public web_userdto(String user_id, String username, String password, String status) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.status = status;
    }

    // Default constructor
    public web_userdto() {
    }

    public web_userdto(String user_id, String username) {
        this.user_id = user_id;
        this.username = username;
    }

    public web_userdto(String user_id, String username, String status) {
        this.user_id = user_id;
        this.username = username;
        this.status = status;
    }

    // Getters and Setters
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getBarangay_name() {
        return barangay_name;
    }

    public void setBarangay_name(String barangay_name) {
        this.barangay_name = barangay_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
