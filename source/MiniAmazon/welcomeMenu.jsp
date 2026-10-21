<%@ page import="java.lang.*" %> <!-- Librerías básicas -->

<!DOCTYPE html>
<html>
<head>
    <title>Administrator Menu</title>
</head>

<body style="background-color:#FFFACD; font-family:Arial;">

<%
    /* Obtengo datos de sesión */
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");

    /* Si no está logueado */
    if (username == null) {
        response.sendRedirect("login.html");
        return;
    }

    /* Si no es admin */
    if (!"admin".equalsIgnoreCase(role)) {
        response.sendRedirect("index.html");
        return;
    }
%>

<!-- Título -->
<h2 style="color:darkblue;">Administrator Menu</h2>

<!-- Bienvenida -->
<p>Welcome, <b><%= username %></b></p>

<hr>

<!-- Gestión de productos -->
<h3>Product Management</h3>
<ul>
    <li><a href="addProduct.jsp">Add Product</a></li>
    <li><a href="listProducts.jsp">List Products</a></li>
</ul>

<!-- Órdenes -->
<h3>Orders</h3>
<ul>
    <li><a href="orders.jsp">View Orders</a></li>
</ul>

<!-- Cuenta -->
<h3>Account</h3>
<ul>
    <li><a href="index.html">Go to Store</a></li>
    <li><a href="logout">Logout</a></li>
</ul>

<hr>

<!-- Pie -->
<p>Mini Amazon Admin Panel</p>

</body>
</html>