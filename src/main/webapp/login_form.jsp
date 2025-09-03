<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Login</title>
    </head>
    <body>
        <h2>Login</h2>
        <form action="main_controller" method="post">
            <input type="hidden" name="action" value="login" />

            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required />
            <br/><br/>

            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required />
            <br/><br/>

            <button type="submit">Login</button>
        </form>

        <br/>
        <p>
            Don't have an account? 
            <a href="registered_form.jsp">Register here</a>
        </p>
        <p>
            Forgot your password? 
            <a href="forgotpassword_form.jsp">Click here</a>
        </p>
    </body>
</html>
