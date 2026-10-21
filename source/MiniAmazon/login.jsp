<%@ page import="java.sql.*" %> <!-- Librerías SQL -->
<%@ page import="ut.applicationDBAuthenticationGoodComplete" %> <!-- Clase de autenticación -->

<html>
<head>
    <title>Login Validation</title>
</head>
<body>

<%
    /* Obtengo los datos del formulario */
    String userName = request.getParameter("userName");
    String userPass = request.getParameter("userPass");

    applicationDBAuthenticationGoodComplete appDBAuth = null;
    ResultSet res = null;

    try {
        /* Conexión y autenticación */
        appDBAuth = new applicationDBAuthenticationGoodComplete();
        res = appDBAuth.authenticate(userName, userPass);

        /* Si el usuario existe */
        if (res.next()) {

            String roleId = res.getString("RoleId");
            String realUserName = res.getString("UserName");

            /* Guardo datos en sesión */
            session.setAttribute("userName", realUserName);
            session.setAttribute("currentPage", "index.html");

            /* Redirección según rol */
            if ("rol1".equals(roleId)) {
                response.sendRedirect("welcomeMenu.jsp");
            } else if ("rol2".equals(roleId)) {
                response.sendRedirect("viewProducts.jsp");
            } else {
                session.invalidate();
                out.println("<h3>Role not authorized</h3>");
                out.println("<a href='login.html'>Back</a>");
            }

        } else {
            /* Usuario incorrecto */
            session.setAttribute("userName", null);
            session.setAttribute("currentPage", null);
            out.println("<h3>Invalid username or password</h3>");
            out.println("<a href='login.html'>Back</a>");
        }

    } catch (Exception e) {
        /* Error general */
        e.printStackTrace();
        out.println("<h3>Error during login</h3>");
        out.println("<p>" + e.getMessage() + "</p>");
        out.println("<a href='login.html'>Back</a>");

    } finally {
        /* Cierro recursos */
        try { if (res != null) res.close(); } catch(Exception ex) {}
        try { if (appDBAuth != null) appDBAuth.close(); } catch(Exception ex) {}
    }
%>

</body>
</html>