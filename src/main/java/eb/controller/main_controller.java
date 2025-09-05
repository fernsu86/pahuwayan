package eb.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "main_controller", urlPatterns = {"/main_controller"})
public class main_controller extends HttpServlet {

    private static final String ENTRY_POINT = "index.jsp";

    // ====== Centralized action → controller mapping ======
    private static final Map<String, String> ROUTES = new HashMap<>();

    static {
        // Auth & user-related
        ROUTES.put("login", "/normalize_action");
        ROUTES.put("logout", "/normalize_action");
        ROUTES.put("createtenant", "/normalize_action");
        //   ROUTES.put("updatenewpasswordwithemail", "/normalize_action");

        // Profile
        //    ROUTES.put("retrieveprofilebyuserid", "/user_controller");
        //    ROUTES.put("viewprofile", "/user_controller");
        //    ROUTES.put("updateprofile", "/user_controller");
        // Notifications
        //    ROUTES.put("createnotification", "/notification_controller");
        //    ROUTES.put("deletenotificationbyid", "/notification_controller");
        //    ROUTES.put("updatenotificationbyid", "/notification_controller");
        //    ROUTES.put("viewnotificationlistbyuseridwithfilter", "/notification_controller");
        // Properties
        ROUTES.put("createproperty", "/property_controller");
        //    ROUTES.put("deletepropertybyid", "/property_controller");
        //    ROUTES.put("updatepropertybyid", "/property_controller");
        ROUTES.put("viewpropertybylandlordid", "/property_controller");
        ROUTES.put("searchbypropertynamewithfilter", "/property_controller");
        ROUTES.put("viewproperty_list", "/property_controller");

        // Landlords
        ROUTES.put("createlandlord", "/landlord_controller");
        ROUTES.put("deletelandlordbyid", "/landlord_controller");
        ROUTES.put("updatelandlordbyid", "/landlord_controller");
        //    ROUTES.put("viewlandlordbyid", "/landlord_controller");
        //    ROUTES.put("searchbyusername", "/landlord_controller");
        ROUTES.put("viewlandlord_list", "/landlord_controller");

        // Chat
        //    ROUTES.put("retrieveroomlist", "/chat_controller");
        //    ROUTES.put("connect user", "/chat_controller");
        // Exception testing
        //    ROUTES.put("printouterror", "/exception_controller");
    }

    // ====== Dispatcher Logic ======
    private String mapActionToController(String action) {
        if (action == null || action.isEmpty()) {
            return ENTRY_POINT; // default/fallback
        }
        return ROUTES.getOrDefault(action.toLowerCase(), ENTRY_POINT);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String url = mapActionToController(action);
        request.getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
