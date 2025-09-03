<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Landlord Dashboard</title>
</head>
<body>
    <h1>Landlord Dashboard</h1>

    <!-- ================= Create Property Form ================= -->
    <h2>Create a New Property</h2>
    <form action="property_controller" method="post" enctype="multipart/form-data">
        <input type="hidden" name="action" value="createproperty" />

        <table>
            <tr>
                <td>Property Name:</td>
                <td><input type="text" name="property_name" required /></td>
            </tr>
            <tr>
                <td>Address:</td>
                <td><input type="text" name="property_address" required /></td>
            </tr>
            <tr>
                <td>Type:</td>
                <td>
                    <select name="property_type" required>
                        <option value="">-- Select Type --</option>
                        <option value="Apartment">Apartment</option>
                        <option value="Boarding House">Boarding House</option>
                        <option value="Condo">Condo</option>
                        <option value="Room">Room</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>Amenities:</td>
                <td><input type="text" name="property_amenity" placeholder="e.g. WiFi, Parking, Aircon" /></td>
            </tr>
            <tr>
                <td>Price:</td>
                <td><input type="number" step="0.01" name="property_price" required /></td>
            </tr>
            <tr>
                <td>Description:</td>
                <td><textarea name="description" rows="4" cols="40"></textarea></td>
            </tr>
            <tr>
                <td>Upload Images:</td>
                <td><input type="file" name="images" multiple accept="image/*" /></td>
            </tr>
            <tr>
                <td colspan="2">
                    <button type="submit">Create Property</button>
                </td>
            </tr>
        </table>
    </form>

    <!-- ================= Property List ================= -->
    <h2>Your Properties</h2>
    <c:if test="${empty propertyList}">
        <p>You don’t have any properties yet.</p>
    </c:if>

    <c:if test="${not empty propertyList}">
        <table border="1" cellpadding="5" cellspacing="0">
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Address</th>
                <th>Type</th>
                <th>Price</th>
                <th>Amenities</th>
                <th>Description</th>
            </tr>
            <c:forEach var="p" items="${propertyList}">
                <tr>
                    <td>${p.property_id}</td>
                    <td>${p.property_name}</td>
                    <td>${p.property_address}</td>
                    <td>${p.property_type}</td>
                    <td>${p.property_price}</td>
                    <td>${p.property_amenity}</td>
                    <td>${p.description}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>

</body>
</html>
