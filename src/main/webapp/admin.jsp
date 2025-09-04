<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Admin Dashboard - Manage Landlords</title>
</head>
<body>
    <h1>Admin Dashboard</h1>

    <!-- ================= Create Landlord Form ================= -->
    <h2>Create a New Landlord</h2>
    <form action="landlord_controller" method="post">
        <input type="hidden" name="action" value="createlandlord" />

        <table>
            <tr>
                <td>Username:</td>
                <td><input type="text" name="username" required /></td>
            </tr>
            <tr>
                <td>Password:</td>
                <td><input type="password" name="password" required /></td>
            </tr>
            <tr>
                <td>Email:</td>
                <td><input type="email" name="email" required /></td>
            </tr>
            <tr>
                <td>Phone:</td>
                <td><input type="text" name="phone" required /></td>
            </tr>
            <tr>
                <td>Barangay:</td>
                <td><input type="text" name="barangay_name" required /></td>
            </tr>
            <tr>
                <td colspan="2">
                    <button type="submit">Create Landlord</button>
                </td>
            </tr>
        </table>
    </form>

    <!-- ================= Landlord List ================= -->
    <h2>Landlord Accounts</h2>
    <c:if test="${empty landlordList}">
        <p>No landlords found.</p>
    </c:if>

    <c:if test="${not empty landlordList}">
        <table border="1" cellpadding="5" cellspacing="0">
            <tr>
                <th>User ID</th>
                <th>Username</th>
                <th>Status</th>
                <th>Barangay</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Update</th>
            </tr>
            <c:forEach var="l" items="${landlordList}">
                <tr>
                    <form action="landlord_controller" method="post">
                        <input type="hidden" name="action" value="updatelandlord"/>
                        <input type="hidden" name="user_id" value="${l.user_id}"/>

                        <td>${l.user_id}</td>
                        <td><input type="text" name="username" value="${l.username}"/></td>
                        <td><input type="text" name="status" value="${l.status}"/></td>
                        <td><input type="text" name="barangay_name" value="${l.barangay_name}"/></td>
                        <td><input type="email" name="email" value="${l.email}"/></td>
                        <td><input type="text" name="phone" value="${l.phone}"/></td>
                        <td>
                            <button type="submit">Update</button>
                        </td>
                    </form>
                </tr>
            </c:forEach>
        </table>
    </c:if>

    <!-- ================= Messages ================= -->
    <c:if test="${not empty message}">
        <p style="color:green;">${message}</p>
    </c:if>
    <c:if test="${not empty error}">
        <p style="color:red;">${error}</p>
    </c:if>

</body>
</html>
<%-- 
    Document   : admin
    Created on : Sep 2, 2025
    Author     : ACER
--%>
