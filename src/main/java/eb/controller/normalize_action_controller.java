package eb.controller;

import eb.dao.web_userdao;
import eb.dto.web_userdto;
import eb.service.user_service;
import eb.util.helper_util;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "normalize_action", urlPatterns = {"/normalize_action"})
public class normalize_action_controller extends HttpServlet {

    // ==================== POST (Login, Register, Logout) ====================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = helper_util.safeLower(request.getParameter("action"));
        if (helper_util.isBlank(action)) {
            helper_util.sendBadRequest(response, "Action is missing");
            return;
        }

        try {
            switch (action) {
                case "login":
                    handleLogin(request, response);
                    break;
                case "logout":
                    handleLogout(request, response);
                    break;
                case "createtenant":
                    handleRegister(request, response);
                    break;
                default:
                    helper_util.sendBadRequest(response, "Unsupported POST action: " + action);
            }
        } catch (Exception e) {
            helper_util.handleServerError(response, e);
        }
    }

    // ==================== GET (Status check) ====================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = helper_util.safeLower(request.getParameter("action"));
        if (helper_util.isBlank(action)) {
            helper_util.sendBadRequest(response, "Action is missing");
            return;
        }

        try {
            switch (action) {
                case "status":
                    handleStatus(request, response);
                    break;
                default:
                    helper_util.sendBadRequest(response, "Unsupported GET action: " + action);
            }
        } catch (Exception e) {
            helper_util.handleServerError(response, e);
        }
    }

    // ==================== Handlers ====================
    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        web_userdto user = new web_userdao().authenticateTest(username, password);

        if (user != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getUser_id());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole_name());
            session.setAttribute("barangay_name",
                    helper_util.isBlank(user.getBarangay_name()) ? null : user.getBarangay_name());

            if (helper_util.wantsJson(request)) {
                String json = String.format(
                        "{\"loggedIn\":true,\"username\":\"%s\",\"role\":\"%s\"}",
                        user.getUsername(), user.getRole_name()
                );
                helper_util.sendOk(response, json);
            } else {
                response.sendRedirect(request.getContextPath() + "/welcome.jsp");
            }
        } else {
            helper_util.sendBadRequest(response, "Invalid username or password");
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        if (helper_util.wantsJson(request)) {
            helper_util.sendOk(response, "{\"message\":\"Logout successful\"}");
        } else {
            response.sendRedirect(request.getContextPath() + "/welcome.jsp");
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String phone = helper_util.normalizePhone(request.getParameter("phone"));
        String email = helper_util.normalizeEmail(request.getParameter("email"));

        user_service service = new user_service();
        boolean success = service.register(username, password, email, phone);

        if (helper_util.wantsJson(request)) {
            if (success) {
                helper_util.sendOk(response, "{\"message\":\"User created successfully\"}");
            } else {
                helper_util.sendBadRequest(response, "Failed to create user");
            }
        } else {
            request.setAttribute(success ? "message" : "error",
                    success ? "User created successfully, please login." : "Failed to create user.");
            request.getRequestDispatcher("registered_form.jsp").forward(request, response);
        }
    }

    private void handleStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        Object username = (session != null) ? session.getAttribute("username") : null;
        Object role = (session != null) ? session.getAttribute("role") : null;

        if (helper_util.wantsJson(request)) {
            if (username != null) {
                String json = String.format(
                        "{\"loggedIn\":true,\"username\":\"%s\",\"role\":\"%s\"}",
                        username, role
                );
                helper_util.sendOk(response, json);
            } else {
                helper_util.sendOk(response, "{\"loggedIn\":false}");
            }
        } else {
            response.getWriter().write(username != null
                    ? "Logged in as " + username
                    : "Not logged in");
        }
    }
}
