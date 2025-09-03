<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Register Tenant</title>
    </head>
    <body>
        <h2>Register_user</h2>

        <!-- This form goes through main_controller -->
        <form action="main_controller" method="post">
            <!-- Tell main_controller which action -->
            <input type="hidden" name="action" value="createtenant" />

            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required />
            <br/><br/>

            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required />
            <br/><br/>

            <label for="email">Email:</label>
            <input type="email" id="email" name="email" placeholder="(Optional)" />
            <br/><br/>

            <label for="phone">Phone:</label>
            <input type="text" id="phone" name="phone" placeholder="(Optional)" />
            <br/><br/>

            <button type="submit">Register</button>
        </form>

        <!-- Show feedback messages -->
        <c:if test="${not empty error}">
            <p style="color:red">${error}</p>
        </c:if>
        <c:if test="${not empty message}">
            <p style="color:green">${message}</p>
        </c:if>
    </body>
</html>
