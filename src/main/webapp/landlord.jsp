<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Landlord Dashboard</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 20px;
            }
            h1, h2 {
                color: #333;
            }
            .property-grid {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
                gap: 20px;
                margin-top: 20px;
            }
            .property-card {
                border: 1px solid #ccc;
                border-radius: 10px;
                padding: 15px;
                background: #fafafa;
                box-shadow: 0 2px 6px rgba(0,0,0,0.1);
            }
            .property-images {
                display: flex;
                gap: 10px;
                overflow-x: auto;
                margin-bottom: 10px;
            }
            .property-images img {
                height: 120px;
                width: auto;
                border-radius: 8px;
                flex-shrink: 0;
            }
            .property-title {
                font-size: 18px;
                font-weight: bold;
                margin: 5px 0;
            }
            .property-type {
                font-size: 14px;
                color: #666;
            }
            .property-price {
                font-size: 16px;
                color: #009900;
                margin: 10px 0;
            }
            .property-desc {
                font-size: 14px;
                color: #444;
            }
        </style>
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

        <!-- ================= Property Cards ================= -->
        <h2>Your Properties</h2>

        <c:if test="${empty propertyList}">
            <p>You don’t have any properties yet.</p>
        </c:if>

        <c:if test="${not empty propertyList}">
            <div class="property-grid">
                <c:forEach var="p" items="${propertyList}">
                    <div class="property-card">

                        <!-- Show multiple images if available -->
                        <c:if test="${not empty p.images}">
                            <div class="property-images">
                                <c:forEach var="img" items="${p.images}">
                                    <img src="${pageContext.request.contextPath}/${img['image_path']}" 
                                         alt="Property Image"/>
                                </c:forEach>
                            </div>
                        </c:if>

                        <!-- Property details -->
                        <div class="property-title">${p.property_name}</div>
                        <div class="property-type">${p.property_type}</div>
                        <div class="property-price">₱ ${p.property_price}</div>
                        <div class="property-desc">${p.description}</div>
                        <div><strong>Amenities:</strong> ${p.property_amenity}</div>
                        <div><strong>Address:</strong> ${p.property_address}</div>

                        <!-- Delete form -->
                        <form action="property_controller" method="post" style="margin-top:10px;">
                            <input type="hidden" name="action" value="deletepropertybyid"/>
                            <input type="hidden" name="property_id" value="${p.property_id}"/>
                            <button type="submit" 
                                    onclick="return confirm('Are you sure you want to delete this property?')">
                                Delete
                            </button>
                        </form>

                    </div>
                </c:forEach>
            </div>
        </c:if>


    </body>
</html>
