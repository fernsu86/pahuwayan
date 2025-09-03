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

    private static final user_service user = new user_service();

    // ==================== POST (Create, Delete) ====================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if (helper_util.isBlank(action)) {
                sendError(response, "Action is missing");
                return;
            }

            switch (action.toLowerCase()) {
                case "deletelandlordbyid":
                    // TODO: implement delete logic
                    break;
                case "createlandlord":
                    handleLandlord(request, response);
                    break;
                default:
                    sendError(response, "Unsupported POST action: " + action);
            }
        } catch (Exception e) {
            handleServerError(response, e);
        }
    }

    // ==================== GET (Status, View List) ====================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if (helper_util.isBlank(action)) {
                sendError(response, "Action is missing");
                return;
            }

            switch (action.toLowerCase()) {
                case "status":
                    handleStatus(request, response);
                    break;
                case "viewlandlord_list":
                    handleListLandlord(request, response);
                    break;
                default:
                    sendError(response, "Unsupported GET action: " + action);
            }
        } catch (Exception e) {
            handleServerError(response, e);
        }
    }

    // ==================== Handlers ====================
    private void handleLandlord(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, Exception {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String phone = helper_util.normalizePhone(request.getParameter("phone"));
        String email = helper_util.normalizeEmail(request.getParameter("email"));
        String barangay_name = request.getParameter("barangay_name");

        user_service service = new user_service();
        boolean success = service.createLandlord(username, password, barangay_name, email, phone);

        if (helper_util.wantsJson(request)) {
            response.setContentType("application/json");
            if (success) {
                response.getWriter().write("{\"message\":\"User created successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Failed to create user\"}");
            }
        } else {
            if (success) {
                List<web_userdto> landlordList = user.retrieveLandLord("landlord");
                request.setAttribute("landlordList", landlordList);
                RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
                rd.forward(request, response);
            } else {
                request.setAttribute("error", "Failed to create user.");
                request.getRequestDispatcher("admin.jsp").forward(request, response);
            }
        }
    }

    private void handleListLandlord(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, Exception {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            sendError(response, "You must be logged in as an admin.");
            return;
        }

        List<web_userdto> landlordList = user.retrieveLandLord("landlord");
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
            response.getWriter().write(username != null ? "Logged in as " + username : "Not logged in");
        }
    }

    // ==================== Helpers ====================
    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"" + message + "\"}");
    }

    private void handleServerError(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Server error: " + e.getMessage() + "\"}");
        e.printStackTrace();
    }
}
