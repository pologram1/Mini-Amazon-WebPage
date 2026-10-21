<%@ page import="model.ProductClass" %> <!-- Clase del producto -->

<%
    /* Obtengo el producto enviado desde el servlet */
    ProductClass product = (ProductClass) request.getAttribute("product");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Mini Amazon - Product Details</title>
<link rel="stylesheet" href="project-brand.css?v=1">
</head>
<body style="background-color:#FFFACD; font-family:Arial;">
<a class="project-brand-link" href="index.html" aria-label="MiniAmazon home"><img class="project-brand-logo" src="images/miniamazon-logo.webp" alt="MiniAmazon — by Polo Studios" width="210" height="110"></a>


    <!-- Navegación -->
    <a href="<%= request.getContextPath() %>/products">Back to Products</a> |
    <a href="<%= request.getContextPath() %>/view-cart">Cart</a> |
    <a href="<%= request.getContextPath() %>/logout">Logout</a>

    <hr>

<%
    /* Si no se encontró el producto */
    if (product == null) {
%>
        <h2>Product not found.</h2>
        <p>The product could not be loaded.</p>
<%
    } else {
%>

        <!-- Nombre del producto -->
        <h1 style="color:#232f3e;"><%= product.getName() %></h1>

        <!-- Imagen -->
        <img src="<%= request.getContextPath() %>/images/<%= product.getImage() %>"
             alt="Product Image"
             width="220"
             height="220">

        <!-- Información del producto -->
        <p><b>ID:</b> <%= product.getProductId() %></p>
        <p><b>Description:</b> <%= product.getDescription() %></p>
        <p><b>Manufacturer:</b> <%= product.getManufacturer() %></p>
        <p><b>Fabrication Year:</b> <%= product.getYear() %></p>
        <p><b>Category ID:</b> <%= product.getCategoryId() %></p>
        <p><b>Price:</b> $<%= product.getPrice() %></p>

        <!-- Formulario para agregar al carrito -->
        <form action="<%= request.getContextPath() %>/add-to-cart" method="post">
            <input type="hidden" name="id" value="<%= product.getProductId() %>">

            <% if (product.getStock() > 0) { %>
                <label>Quantity:</label>
                <input type="number" name="quantity" min="1" max="<%= product.getStock() %>" value="1" required>
                <input type="submit" value="Add to Cart">
            <% } else { %>
                <p style="color:red;"><b>Out of Stock</b></p>
            <% } %>
        </form>

<%
    }
%>

</body>
</html>