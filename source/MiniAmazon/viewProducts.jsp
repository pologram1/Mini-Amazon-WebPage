<%@ page import="java.util.List" %>          <!-- Lista -->
<%@ page import="model.ProductClass" %>      <!-- Clase del producto -->

<%
    /* Obtengo la lista de productos */
    List<ProductClass> products = (List<ProductClass>) request.getAttribute("products");

    /* Si no hay productos cargados, redirijo */
    if (products == null) {
        response.sendRedirect(request.getContextPath() + "/products");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Mini Amazon - Products</title>
<link rel="stylesheet" href="project-brand.css?v=1">
</head>
<body style="background-color:#FFFACD; font-family:Arial;">
<a class="project-brand-link" href="index.html" aria-label="MiniAmazon home"><img class="project-brand-logo" src="images/miniamazon-logo.webp" alt="MiniAmazon — by Polo Studios" width="210" height="110"></a>


    <!-- Título -->
    <h1 style="color:#232f3e;">All Products</h1>

    <!-- Navegación -->
    <a href="<%= request.getContextPath() %>/index.html">Home</a> |
    <a href="<%= request.getContextPath() %>/view-cart">Cart</a> |
    <a href="<%= request.getContextPath() %>/orders.jsp">My Orders</a> |
    <a href="<%= request.getContextPath() %>/logout">Logout</a>

    <hr>

    <!-- Formulario de búsqueda -->
    <form action="<%= request.getContextPath() %>/products" method="get">
        <input type="text" name="q" placeholder="Search by product name">

        <!-- Categorías -->
        <select name="categoryId">
            <option value="">All Categories</option>
            <option value="1">Electronics</option>
            <option value="2">Clothing and Accessories</option>
            <option value="3">Home and Kitchen</option>
            <option value="4">Beauty and Personal Care</option>
            <option value="5">Sports and Fitness</option>
            <option value="6">Toys and Games</option>
            <option value="7">Books and Education</option>
            <option value="8">Automotive</option>
        </select>

        <input type="submit" value="Search">
    </form>

    <hr>

<%
    /* Si no hay productos */
    if (products.isEmpty()) {
%>
        <p>No products found.</p>
<%
    } else {
%>

    <!-- Tabla de productos -->
    <table border="1" cellpadding="8" cellspacing="0">
        <tr style="background-color:#dddddd;">
            <th>Name</th>
            <th>Stock</th>
            <th>Image</th>
            <th>Price</th>
            <th>Details</th>
            <th>Add to Cart</th>
        </tr>

<%
        /* Recorro los productos */
        for (ProductClass product : products) {
%>
        <tr>
            <td><%= product.getName() %></td>
            <td><%= product.getStock() %></td>

            <!-- Imagen -->
            <td>
                <img src="<%= request.getContextPath() %>/images/<%= product.getImage() %>"
                     width="90" height="90">
            </td>

            <td>$<%= product.getPrice() %></td>

            <!-- Ver detalles -->
            <td>
                <a href="<%= request.getContextPath() %>/product-details?id=<%= product.getProductId() %>">
                    View Details
                </a>
            </td>

            <!-- Agregar al carrito -->
            <td>
                <% if (product.getStock() > 0) { %>
                    <form action="<%= request.getContextPath() %>/add-to-cart" method="post" style="margin:0;">
                        <input type="hidden" name="id" value="<%= product.getProductId() %>">
                        <input type="number" name="quantity" min="1" max="<%= product.getStock() %>" value="1" required style="width:60px;">
                        <input type="submit" value="Add to Cart">
                    </form>
                <% } else { %>
                    <span style="color:red;"><b>Out of Stock</b></span>
                <% } %>
            </td>
        </tr>
<%
        } /* fin del for */
%>

    </table>

<%
    } /* fin del else */
%>

</body>
</html>