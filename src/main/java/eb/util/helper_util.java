package eb.util;

import eb.dao.propertydao;
import eb.dao.web_userdao;
import eb.dto.imagefiledto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * GOD HELPER – contains all cross-cutting helpers in one place. Grouped into
 * logical sections for readability.
 */
public class helper_util {

    // ==== DAO singletons ====================================================
    private static final web_userdao USERDAO = new web_userdao();
    private static final propertydao PROPERTYDAO = new propertydao();

    // ==== Security / Password ===============================================
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }

    // ==== ID Generation =====================================================
    public static String generate_blank_Uid() throws Exception {
        return "BLid" + USERDAO.retrieve_all_user().size();
    }

    public static String generate_tenant_Uid() throws Exception {
        return "TNid" + generate_blank_Uid();
    }

    public static String generate_landlord_Uid() throws Exception {
        return "LLid" + generate_blank_Uid();
    }

    public static String generate_property_Uid() {
        return "Prty" + UUID.randomUUID().toString();
    }

    // ==== File Upload Helpers ===============================================
    public static List<imagefiledto> fromParts(Collection<Part> parts) throws IOException {
        List<imagefiledto> files = new ArrayList<>();
        for (Part filePart : parts) {
            if (!"images".equals(filePart.getName()) || filePart.getSize() == 0) {
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

    public static String getSubmittedFileName(Part part) {
        String header = part.getHeader("content-disposition");
        if (header == null) {
            return null;
        }

        for (String cd : header.split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1)
                        .substring(fileName.lastIndexOf('\\') + 1); // IE/Edge safe
            }
        }
        return null;
    }

    // ==== Request Helpers ===================================================
    public static boolean wantsJson(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        return accept != null && accept.contains("application/json");
    }

    public static String requireParam(HttpServletRequest request, HttpServletResponse response, String name)
            throws IOException {
        String value = request.getParameter(name);
        if (isBlank(value)) {
            sendBadRequest(response, "Missing required parameter: " + name);
            return null;
        }
        return value.trim();
    }

    public static String optParam(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return isBlank(value) ? null : value.trim();
    }

    public static String requireLoggedInLandlord(HttpSession session, HttpServletResponse response)
            throws IOException {
        if (session == null || session.getAttribute("userId") == null) {
            sendBadRequest(response, "You must be logged in as a landlord.");
            return null;
        }
        return (String) session.getAttribute("userId");
    }

    // ==== Validation ========================================================
    public static String normalizeEmail(String email) {
        if (isBlank(email)) {
            return null;
        }
        return email.matches("^[A-Za-z0-9._%+-]+@gmail\\.com$") ? email : null;
    }

    public static String normalizePhone(String phone) {
        if (isBlank(phone)) {
            return null;
        }
        return phone.matches("^[0-9]{9,11}$") ? phone : null;
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    // ==== Parsing Helpers ===================================================
    public static double parseDoubleOrDefault(String value, double defaultValue) {
        try {
            if (isBlank(value)) {
                return defaultValue;
            }
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // ==== Response Helpers ==================================================
    public static void forward(HttpServletRequest request, HttpServletResponse response, String jsp)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher(jsp);
        rd.forward(request, response);
    }

    public static void sendBadRequest(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"error\":\"" + escapeJson(message) + "\"}");
    }

    public static void sendNotFound(HttpServletResponse response, String messageJson) throws IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(messageJson);
    }

    public static void sendOk(HttpServletResponse response, String messageJson) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(messageJson);
    }

    public static void handleServerError(HttpServletResponse response, Exception e) throws IOException {
        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"error\":\"Server error: " + escapeJson(e.getMessage()) + "\"}");
    }

    // ==== Internal Helpers ==================================================
    private static String escapeJson(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
    // ==== String Helpers ====================================================

    public static String safeLower(String s) {
        return (s == null) ? null : s.toLowerCase().trim();
    }

    public static boolean containsIgnoreCase(String src, String target) {
        if (src == null || target == null) {
            return false;
        }
        return src.toLowerCase().contains(target.toLowerCase());
    }

    // ==== File Upload (controller shortcut) =================================
    public static List<imagefiledto> extractImages(HttpServletRequest request) throws IOException, ServletException {
        List<imagefiledto> files = new ArrayList<>();
        for (Part part : request.getParts()) {
            if ("images".equals(part.getName()) && part.getSize() > 0) {
                files.add(new imagefiledto(
                        getSubmittedFileName(part),
                        part.getContentType(),
                        part.getSize(),
                        part.getInputStream()
                ));
            }
        }
        return files;
    }

}
