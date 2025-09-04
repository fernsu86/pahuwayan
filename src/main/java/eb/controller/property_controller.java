package eb.controller;

import eb.dto.imagefiledto;
import eb.dto.propertydto;
import eb.service.property_service;
import eb.service.uploadfile_service;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
@WebServlet(name = "property_controller", urlPatterns = {"/property_controller"})
public class property_controller extends HttpServlet {

    private final static property_service property = new property_service();

    // ==================== POST (Create/Update/Delete) ====================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if (action == null) {
                sendError(response, "Action is missing");
                return;
            }

            switch (action.toLowerCase()) {
                case "createproperty":
                    handleCreateProperty(request, response);
                    break;

                case "deletepropertybyid":
                    handleDeleteProperty(request, response);
                    break;

                case "updatepropertybyid":
                    handleUpdateProperty(request, response);
                    break;

                default:
                    sendError(response, "Unsupported POST action: " + action);
            }

        } catch (Exception e) {
            handleServerError(response, e);
        }
    }

    // ==================== GET (Read operations) ====================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if (action == null) {
                sendError(response, "Action is missing");
                return;
            }

            switch (action.toLowerCase()) {
                case "viewpropertybylandlordid":
                    handleViewPropertyByLandlord(request, response);
                    break;

                case "searchbyusernamewithfilter":
                    handleSearchByUsername(request, response);
                    break;

                case "viewproperty_list":
                    handleViewPropertyList(request, response);
                    break;

                default:
                    sendError(response, "Unsupported GET action: " + action);
            }

        } catch (Exception e) {
            handleServerError(response, e);
        }
    }

    // ==================== Action Handlers ====================
    private void handleCreateProperty(HttpServletRequest request, HttpServletResponse response)
            throws IOException, Exception {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            sendError(response, "You must be logged in as a landlord.");
            return;
        }

        // Session values
        String barangay_name = (String) session.getAttribute("barangay_name");
        String landlord_id = (String) session.getAttribute("userId");

        // Form values
        String property_name = request.getParameter("property_name");
        String property_address = request.getParameter("property_address");
        String property_type = request.getParameter("property_type");
        String property_amenity = request.getParameter("property_amenity");
        String description = request.getParameter("description");

        double property_price = 0.0;
        String priceParam = request.getParameter("property_price");
        if (priceParam != null && !priceParam.isEmpty()) {
            try {
                property_price = Double.parseDouble(priceParam);
            } catch (NumberFormatException e) {
                sendError(response, "Invalid property price format.");
                return;
            }
        }

        // Step A: Save property first (generate propertyId)
        String propertyId = property.handle_property(
                barangay_name,
                landlord_id,
                property_name,
                property_address,
                property_type,
                property_amenity,
                property_price,
                description
        );

        // Step B: Handle image uploads
        List<imagefiledto> fileDtos = new ArrayList<>();

        for (Part part : request.getParts()) {
            if (part.getName().equals("images") && part.getSize() > 0) {
                String originalFileName = Paths.get(part.getSubmittedFileName())
                        .getFileName().toString();
                fileDtos.add(new imagefiledto(
                        originalFileName,
                        part.getContentType(),
                        part.getSize(),
                        part.getInputStream()
                ));
            }
        }

        if (!fileDtos.isEmpty()) {
            // DEV: use the webapp/uploads folder (absolute path)
            String uploadDir = getServletContext().getRealPath("/uploads");
            if (uploadDir == null || uploadDir.isEmpty()) {
                // fallback (shouldn't happen) but safer
                throw new ServletException("Cannot resolve webapp uploads folder.");
            }

            // IMPORTANT: pass false because uploadDir is an absolute path
            uploadfile_service uploadService = new uploadfile_service(uploadDir, false);
            uploadService.saveImages(propertyId, fileDtos);
        }

        // Fetch updated property list
        List<propertydto> propertyList = property.handle_retrieve_property(landlord_id);

        // Attach it to request
        request.setAttribute("propertyList", propertyList);

        // Forward back to landlord dashboard JSP
        RequestDispatcher rd = request.getRequestDispatcher("landlord.jsp");
        rd.forward(request, response);

    }

    private void handleDeleteProperty(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // TODO: implement DB delete
        response.getWriter().write("Property deleted!");
    }

    private void handleUpdateProperty(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // TODO: implement DB update

    }

    private void handleViewPropertyByLandlord(HttpServletRequest request, HttpServletResponse response)
            throws IOException, Exception {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            sendError(response, "You must be logged in as a landlord.");
            return;
        }
        //Session values
        String landlord_id = (String) session.getAttribute("userId");
        List<propertydto> propertyList = property.handle_retrieve_property(landlord_id);
        request.setAttribute("propertyList", propertyList);
        RequestDispatcher rd = request.getRequestDispatcher("landlord.jsp");
        rd.forward(request, response);
        // TODO: fetch landlord properties from DB

    }

    private void handleSearchByUsername(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // TODO: implement search
        response.getWriter().write("Search results for username!");
    }

    private void handleViewPropertyList(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            List<propertydto> propertyList = property.handle_retrieve_all_properties();
            request.setAttribute("properties", propertyList);

            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request, response);
        } catch (Exception e) {
            handleServerError(response, e);
        }
    }

    // ==================== Error Helpers ====================
    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("{\"error\":\"" + message + "\"}");
    }

    private void handleServerError(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write("{\"error\":\"Server error: " + e.getMessage() + "\"}");
        e.printStackTrace();
    }
}
