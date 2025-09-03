<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Boarding House Finder</title>
        <script>
            function toggleLogin() {
                var section = document.getElementById("loginSection");
                section.style.display = (section.style.display === "none") ? "block" : "none";
            }
        </script>
    </head>
    <body>
        <h1>Welcome to Boarding House Finder</h1>

        <c:choose>
            <c:when test="${empty sessionScope.role}">
                <button onclick="toggleLogin()">Login</button>
                <div id="loginSection" style="display:none; margin-top:15px;">
                    <jsp:include page="login_form.jsp" />
                </div>
                <p><a href="registered_form.jsp">Register</a></p>
            </c:when>
            <c:when test="${sessionScope.role eq 'admin'}">
                <h2>Admin Dashboard</h2>
                <ul>
                    <li><a href="main_controller?action=viewlandlord_list">Go to Admin Page</a></li>
                    <form action="${pageContext.request.contextPath}/main_controller" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="logout"/>
                        <button type="submit">Logout</button>
                    </form>
                </ul>
            </c:when>
            <c:when test="${sessionScope.role eq 'landlord'}">
                <h2>Landlord Dashboard</h2>
                <ul>
                    <li><a href="main_controller?action=viewpropertybylandlordid">Go to LandLord Page</a></li>
                    <form action="${pageContext.request.contextPath}/main_controller" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="logout"/>
                        <button type="submit">Logout</button>
                    </form>
                </ul>
            </c:when>
            <c:when test="${sessionScope.role eq 'tenant'}">
                <h2>Tenant Dashboard</h2>
                <ul>
                    <li><a href="tenant.jsp">Go to Tenant Page</a></li>
                    <form action="${pageContext.request.contextPath}/main_controller" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="logout"/>
                        <button type="submit">Logout</button>
                    </form>
                </ul>
            </c:when>
        </c:choose>

    </body>
</html>