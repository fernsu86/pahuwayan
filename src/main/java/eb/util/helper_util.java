/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eb.util;

import eb.dao.propertydao;
import eb.dao.web_userdao;
import eb.dto.imagefiledto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author ACER
 */
public class helper_util {

    private static final web_userdao userdao = new web_userdao();
    private static final propertydao propertydao = new propertydao();

    // hashing password
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Convert uploaded parts into imagefiledto list
    public static List<imagefiledto> fromParts(Collection<Part> parts) throws IOException {
        List<imagefiledto> files = new ArrayList<>();
        for (Part filePart : parts) {
            if (!"image".equals(filePart.getName()) || filePart.getSize() == 0) {
                continue;
            }
            files.add(new imagefiledto(
                    getSubmittedFileName(filePart),
                    filePart.getContentType(),
                    filePart.getSize(),
                    filePart.getInputStream()
            ));
        }
        return files;
    }

    // Extract submitted file name from multipart data
    public static String getSubmittedFileName(Part part) {
        String header = part.getHeader("content-disposition");
        if (header == null) return null;

        for (String cd : header.split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1)
                               .substring(fileName.lastIndexOf('\\') + 1); // handle IE/Edge
            }
        }
        return null;
    }

    // Hash password
    public static String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    // Verify password
    public static boolean verifyPassword(String password, String hashedpassword) {
        return passwordEncoder.matches(password, hashedpassword);
    }

    // Check if request wants JSON response
    public static boolean wantsJson(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        return accept != null && accept.contains("application/json");
    }

    // Generate blank_id (do not delete users, mark them instead)
    public static String generate_blank_Uid() throws Exception {
        return "BLid" + userdao.retrieve_all_user().size();
    }

    // tenant_id
    public static String generate_tenant_Uid() throws Exception {
        return "TNid" + generate_blank_Uid();
    }

    // landlord_id
    public static String generate_landlord_Uid() throws Exception {
        return "LLid" + generate_blank_Uid();
    }

    // property_id
    public static String generate_property_Uid() throws Exception {
        return "Prty" + UUID.randomUUID().toString();
    }

    // Normalize Gmail-only emails
    public static String normalizeEmail(String email) {
        if (isBlank(email)) return null;
        return email.matches("^[A-Za-z0-9._%+-]+@gmail\\.com$") ? email : null;
    }

    // Normalize phone numbers (9-11 digits only)
    public static String normalizePhone(String phone) {
        if (isBlank(phone)) return null;
        return phone.matches("^[0-9]{9,11}$") ? phone : null;
    }

    // Check blank string
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
