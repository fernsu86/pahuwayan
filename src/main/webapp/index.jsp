<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Boarding House Finder - Home</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }
        .property-container {
            display: flex;
            flex-wrap: wrap;
            gap: 15px;
            margin-top: 20px;
        }
        .property-card {
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 15px;
            width: 280px;
            background: #fafafa;
        }
        .property-card img {
            width: 100%;
            height: 180px;
            object-fit: cover;
            border-radius: 6px;
        }
        .property-card h3 {
            margin: 10px 0 5px;
        }
        .property-card p {
            margin: 3px 0;
        }
    </style>
    <script>
        function toggleLogin() {
            var section = document.getElementById("loginSection");
            section.style.display = (section.style.display === "none") ? "block" : "none";
        }
    </script>
</head>
<body>
    <h1>Welcome to Boarding House Finder</h1>

    <!-- ========== LOGIN / DASHBOARD SECTION ========== -->
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
            </ul>
            <form action="${pageContext.request.contextPath}/main_controller" method="post">
                <input type="hidden" name="action" value="logout"/>
                <button type="submit">Logout</button>
            </form>
        </c:when>
        <c:when test="${sessionScope.role eq 'landlord'}">
            <h2>Landlord Dashboard</h2>
            <ul>
                <li><a href="main_controller?action=viewpropertybylandlordid">Go to Landlord Page</a></li>
            </ul>
            <form action="${pageContext.request.contextPath}/main_controller" method="post">
                <input type="hidden" name="action" value="logout"/>
                <button type="submit">Logout</button>
            </form>
        </c:when>
        <c:when test="${sessionScope.role eq 'tenant'}">
            <h2>Tenant Dashboard</h2>
            <ul>
                <li><a href="tenant.jsp">Go to Tenant Page</a></li>
            </ul>
            <form action="${pageContext.request.contextPath}/main_controller" method="post">
                <input type="hidden" name="action" value="logout"/>
                <button type="submit">Logout</button>
            </form>
        </c:when>
    </c:choose>

    <!-- ========== PROPERTY LISTINGS SECTION ========== -->
    <h2>Available Properties</h2>

    <c:if test="${empty properties}">
        <p>No properties available right now.</p>
    </c:if>

    <div class="property-container">
        <c:forEach var="p" items="${properties}">
            <div class="property-card">
                <c:choose>
                    <c:when test="${not empty p.images}">
                        <div style="display:flex; gap:5px; overflow-x:auto; max-width:100%;">
                            <c:forEach var="img" items="${p.images}">
                                <!-- ✅ Same image handling as landlord page -->
                                <img src="${pageContext.request.contextPath}/uploads/${fn:replace(img.image_path, 'uploads/', '')}" 
                                     alt="Property Image" 
                                     style="width:100px; height:100px; object-fit:cover; border-radius:4px;" />
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <img src="${pageContext.request.contextPath}/assets/no-image.png" alt="No Image"/>
                    </c:otherwise>
                </c:choose>

                <h3>${p.property_name}</h3>
                <p><strong>Address:</strong> ${p.property_address}</p>
                <p><strong>Type:</strong> ${p.property_type}</p>
                <p><strong>Amenities:</strong> ${p.property_amenity}</p>
                <p><strong>Price:</strong> $${p.property_price}</p>
                <p><strong>Description:</strong> ${p.description}</p>
            </div>
        </c:forEach>
    </div>
</body>
</html>
