package eb.controller;

import eb.dto.imagefiledto;
import eb.dto.propertydto;
import eb.service.property_service;
import eb.service.uploadfile_service;
import eb.util.helper_util;  // <-- added

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

    // ---- Services -------------------------------------------------------------------------------
    private static final property_service PROPERTY_SERVICE = new property_service();

    // ---- HTTP Methods ---------------------------------------------------------------------------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        final String action = helper_util.safeLower(request.getParameter("action"));

        if (action == null) {
            helper_util.sendBadRequest(response, "Action is missing");
            return;
        }

        try {
            switch (action) {
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
                    helper_util.sendBadRequest(response, "Unsupported POST action: " + action);
            }
        } catch (Exception e) {
            helper_util.handleServerError(response, e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        final String action = helper_util.safeLower(request.getParameter("action"));

        if (action == null) {
            helper_util.sendBadRequest(response, "Action is missing");
            return;
        }

        try {
            switch (action) {
                case "viewpropertybylandlordid":
                    handleViewPropertyByLandlord(request, response);
                    break;
                case "searchbypropertynamewithfilter":
                    handleSearchByPropertyName(request, response);
                    break;
                case "viewproperty_list":
                    handleViewPropertyList(request, response);
                    break;
                default:
                    helper_util.sendBadRequest(response, "Unsupported GET action: " + action);
            }
        } catch (Exception e) {
            helper_util.handleServerError(response, e);
        }
    }

    // ---- Action Handlers ------------------------------------------------------------------------
    private void handleCreateProperty(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, Exception {

        HttpSession session = request.getSession(false);
        String landlordId = helper_util.requireLoggedInLandlord(session, response);
        if (landlordId == null) {
            return;
        }

        String barangayName = (String) session.getAttribute("barangay_name");

        // Required fields
        String propertyName = helper_util.requireParam(request, response, "property_name");
        if (propertyName == null) {
            return;
        }

        String propertyAddress = helper_util.requireParam(request, response, "property_address");
        if (propertyAddress == null) {
            return;
        }

        // Optional fields
        String propertyType = helper_util.optParam(request, "property_type");
        String propertyAmenity = helper_util.optParam(request, "property_amenity");
        String description = helper_util.optParam(request, "description");
        double propertyPrice = helper_util.parseDoubleOrDefault(
                request.getParameter("property_price"), 0.0
        );

        // Step A: Create property
        String propertyId = PROPERTY_SERVICE.handle_property(
                barangayName,
                landlordId,
                propertyName,
                propertyAddress,
                propertyType,
                propertyAmenity,
                propertyPrice,
                description
        );

        // Step B: Image uploads (optional)
        List<imagefiledto> files = new ArrayList<>();
        for (Part part : request.getParts()) {
            if ("images".equals(part.getName()) && part.getSize() > 0) {
                String originalFileName = Paths.get(part.getSubmittedFileName())
                        .getFileName()
                        .toString();
                files.add(new imagefiledto(
                        originalFileName,
                        part.getContentType(),
                        part.getSize(),
                        part.getInputStream()
                ));
            }
        }

        String uploadDir = getServletContext().getInitParameter("uploadDir");
        if (uploadDir == null || uploadDir.isEmpty()) {
            throw new ServletException("uploadDir not configured in web.xml");
        }

        if (!files.isEmpty()) {
            uploadfile_service uploadService = new uploadfile_service(uploadDir, false);
            uploadService.saveImages(propertyId, files);
        }

        // Refresh landlord properties and forward to dashboard
        List<propertydto> propertyList = PROPERTY_SERVICE.handle_retrieve_property(landlordId);
        request.setAttribute("propertyList", propertyList);
        helper_util.forward(request, response, "landlord.jsp");
    }

    private void handleDeleteProperty(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String propertyId = helper_util.requireParam(request, response, "property_id");
        if (propertyId == null) {
            return;
        }

        try {
            boolean success = PROPERTY_SERVICE.handle_delete_property(propertyId);
            if (success) {
                helper_util.sendOk(response,
                        "{\"success\":true,\"message\":\"Property deleted successfully!\"}");
            } else {
                helper_util.sendNotFound(response,
                        "{\"success\":false,\"message\":\"Property not found or already deactivated!\"}");
            }
        } catch (Exception e) {
            helper_util.handleServerError(response, e);
        }
    }

    private void handleUpdateProperty(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        helper_util.sendBadRequest(response, "Update handler not implemented yet.");
    }

    private void handleViewPropertyByLandlord(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, Exception {

        HttpSession session = request.getSession(false);
        String landlordId = helper_util.requireLoggedInLandlord(session, response);
        if (landlordId == null) {
            return;
        }

        List<propertydto> propertyList = PROPERTY_SERVICE.handle_retrieve_property(landlordId);
        request.setAttribute("propertyList", propertyList);
        helper_util.forward(request, response, "landlord.jsp");
    }

    private void handleSearchByPropertyName(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, Exception {

        String propertyName = helper_util.optParam(request, "property_name");
        String propertyType = helper_util.optParam(request, "property_type");
        String amenityType = helper_util.optParam(request, "amenity_type");

        double minPrice = helper_util.parseDoubleOrDefault(
                request.getParameter("min_price"), 0.0
        );
        double maxPrice = helper_util.parseDoubleOrDefault(
                request.getParameter("max_price"), Double.MAX_VALUE
        );

        List<propertydto> propertyList = PROPERTY_SERVICE.handle_retrieve_by_property_name(propertyName);
        List<propertydto> filtered = new ArrayList<>();

        for (propertydto p : propertyList) {
            boolean matches = true;

            if (!helper_util.isBlank(propertyType) && p.getProperty_type() != null
                    && !p.getProperty_type().toLowerCase().contains(propertyType.toLowerCase())) {
                matches = false;
            }
            if (!helper_util.isBlank(amenityType) && p.getProperty_amenity() != null
                    && !p.getProperty_amenity().toLowerCase().contains(amenityType.toLowerCase())) {
                matches = false;
            }
            if (matches && p.getProperty_price() >= minPrice && p.getProperty_price() <= maxPrice) {
                filtered.add(p);
            }
        }

        request.setAttribute("properties", filtered);
        helper_util.forward(request, response, "index.jsp");
    }

    private void handleViewPropertyList(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        try {
            List<propertydto> propertyList = PROPERTY_SERVICE.handle_retrieve_all_properties();
            request.setAttribute("properties", propertyList);
            helper_util.forward(request, response, "index.jsp");
        } catch (Exception e) {
            helper_util.handleServerError(response, e);
        }
    }
}
