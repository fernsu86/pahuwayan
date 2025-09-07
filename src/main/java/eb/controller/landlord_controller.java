package eb.controller;

import eb.dto.web_userdto;
import eb.service.user_service;
import eb.util.helper_util;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "landlord_controller", urlPatterns = {"/landlord_controller"})
public class landlord_controller extends HttpServlet {

    private static final user_service userService = new user_service();

    // ==================== POST (Create, Update, Delete) ====================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (helper_util.isBlank(action)) {
            sendError(response, "Action is missing");
            return;
        }

        try {
            switch (action.toLowerCase()) {
                case "createlandlord":
                    handleCreateLandlord(request, response);
                    break;
                case "updatelandlord":
                    handleUpdateLandlord(request, response);
                    break;
                default:
                    sendError(response, "Unsupported POST action: " + action);
                    break;
            }
        } catch (Exception e) {
            handleServerError(response, e);
        }
    }

    // ==================== GET (Status, List) ====================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (helper_util.isBlank(action)) {
            sendError(response, "Action is missing");
            return;
        }

        try {
            switch (action.toLowerCase()) {
                case "status":
                    handleStatus(request, response);
                    break;
                case "viewlandlord_list":
                    handleListLandlord(request, response);
                    break;
                default:
                    sendError(response, "Unsupported GET action: " + action);
                    break;
            }
        } catch (Exception e) {
            handleServerError(response, e);
        }
    }

    // ==================== Handlers ====================
    private void handleCreateLandlord(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String phone = helper_util.normalizePhone(request.getParameter("phone"));
        String email = helper_util.normalizeEmail(request.getParameter("email"));
        String barangayName = request.getParameter("barangay_name");

        boolean success = userService.createLandlord(username, password, barangayName, email, phone);

        if (success) {
            // reload list and forward to admin.jsp
            List<web_userdto> landlordList = userService.retrieveLandLord("landlord");
            request.setAttribute("landlordList", landlordList);
            request.setAttribute("message", "Landlord created successfully");
        } else {
            request.setAttribute("error", "Failed to create landlord.");
        }

        RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
        rd.forward(request, response);
    }

    private void handleUpdateLandlord(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String userId = request.getParameter("user_id");
        String username = request.getParameter("username");
        String status = request.getParameter("status");
        String barangay = request.getParameter("barangay_name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password"); // may be empty

        if (helper_util.isBlank(password)) {
            password = null;
        }

        boolean updated = userService.update(
                userId, username, password, status,
                "landlord", barangay, email, phone
        );

        if (updated) {
            request.setAttribute("message", "Landlord updated successfully");
        } else {
            request.setAttribute("error", "Failed to update landlord");
        }

        // reload landlord list for admin.jsp
        List<web_userdto> landlordList = userService.retrieveLandLord("landlord");
        request.setAttribute("landlordList", landlordList);

        RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
        rd.forward(request, response);
    }

    private void handleListLandlord(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            sendError(response, "You must be logged in as an admin.");
            return;
        }

        List<web_userdto> landlordList = userService.retrieveLandLord("landlord");
        request.setAttribute("landlordList", landlordList);

        RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
        rd.forward(request, response);
    }

    private void handleStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        Object username = (session != null) ? session.getAttribute("username") : null;
        Object role = (session != null) ? session.getAttribute("role") : null;

        if (helper_util.wantsJson(request)) {
            response.setContentType("application/json");
            if (username != null) {
                response.getWriter().write(
                        "{\"loggedIn\":true,\"username\":\"" + username + "\",\"role\":\"" + role + "\"}"
                );
            } else {
                response.getWriter().write("{\"loggedIn\":false}");
            }
        } else {
            response.getWriter().write(username != null
                    ? "Logged in as " + username
                    : "Not logged in");
        }
    }

    // ==================== Helpers ====================
    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"" + message + "\"}");
    }

    private void handleServerError(HttpServletResponse response, Exception e) throws IOException {
        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Server error: " + e.getMessage() + "\"}");
    }
}
