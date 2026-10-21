<%@ page import="model.ProductClass" %> <!-- Clase del producto -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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

    /* Obtengo el producto a editar */
    ProductClass product = (ProductClass) request.getAttribute("product");
    if (product == null) {
        response.sendRedirect("listProducts.jsp");
        return;
    }
%>

<html>
<head>
    <title>Edit Product</title>
<link rel="stylesheet" href="project-brand.css?v=1">
</head>
<body>
<a class="project-brand-link" href="index.html" aria-label="MiniAmazon home"><img class="project-brand-logo" src="images/miniamazon-logo.webp" alt="MiniAmazon — by Polo Studios" width="210" height="110"></a>


<!-- Título -->
<h2>Edit Product</h2>

<!-- Formulario para editar producto -->
<form action="EditProductServlet" method="post" enctype="multipart/form-data">

    <!-- ID oculto -->
    <input type="hidden" name="id" value="<%= product.getProductId() %>">

    <!-- Nombre -->
    Name:
    <input type="text" name="name" value="<%= product.getName() %>" required>
    <br><br>

    <!-- Descripción -->
    Description:
    <input type="text" name="description" value="<%= product.getDescription() %>" required>
    <br><br>

    <!-- Precio -->
    Price:
    <input type="number" step="0.01" name="price" value="<%= product.getPrice() %>" required>
    <br><br>

    <!-- Stock -->
    Stock:
    <input type="number" name="stock" value="<%= product.getStock() %>" required>
    <br><br>

    <!-- Imagen actual -->
    Current Image:
    <br>
    <% if (product.getImage() != null && !product.getImage().isEmpty()) { %>
        <img src="images/<%= product.getImage() %>" width="120">
    <% } else { %>
        No image
    <% } %>
    <br><br>

    <!-- Nueva imagen -->
    New Image:
    <input type="file" name="image" accept="image/*">
    <br><br>

    <!-- Botón -->
    <button type="submit">Update Product</button>
</form>

<br>

<!-- Volver a la lista -->
<a href="listProducts.jsp">Back to Product List</a>

</body>
</html>