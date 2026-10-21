<%@ page import="java.util.List" %>          <!-- Lista -->
<%@ page import="dao.ProductDAO" %>          <!-- DAO de productos -->
<%@ page import="model.ProductClass" %>      <!-- Clase del producto -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    /* Datos de sesión */
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

    /* Obtengo todos los productos */
    ProductDAO dao = new ProductDAO();
    List<ProductClass> products = dao.getAllProducts();
%>

<html>
<head>
    <title>List Products</title>
<link rel="stylesheet" href="project-brand.css?v=1">
</head>
<body>
<a class="project-brand-link" href="index.html" aria-label="MiniAmazon home"><img class="project-brand-logo" src="images/miniamazon-logo.webp" alt="MiniAmazon — by Polo Studios" width="210" height="110"></a>


<!-- Título -->
<h2>Product List</h2>

<!-- Tabla de productos -->
<table border="1" cellpadding="10">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Price</th>
        <th>Stock</th>
        <th>Image</th>
        <th>Actions</th>
    </tr>

    <!-- Recorro los productos -->
    <% for (ProductClass p : products) { %>
    <tr>
        <td><%= p.getProductId() %></td>
        <td><%= p.getName() %></td>
        <td><%= p.getPrice() %></td>
        <td><%= p.getStock() %></td>

        <!-- Imagen -->
        <td>
            <% if (p.getImage() != null && !p.getImage().isEmpty()) { %>
                <img src="images/<%= p.getImage() %>" width="100">
            <% } else { %>
                No image
            <% } %>
        </td>

        <!-- Acciones -->
        <td>
            <a href="EditProductServlet?id=<%= p.getProductId() %>">Edit</a>
            |
            <a href="RemoveProductServlet?id=<%= p.getProductId() %>">Delete</a>
        </td>
    </tr>
    <% } %>
</table>

<br>

<!-- Volver al menú admin -->
<a href="welcomeMenu.jsp">Back to Admin Menu</a>

</body>
</html>