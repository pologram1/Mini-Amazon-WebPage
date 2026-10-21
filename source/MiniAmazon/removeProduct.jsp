<%@ page import="model.ProductClass" %> <!-- Clase del producto -->

<%
    /* Obtengo el producto enviado desde el servlet */
    ProductClass product = (ProductClass) request.getAttribute("product");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Remove Product</title>
<link rel="stylesheet" href="project-brand.css?v=1">
</head>
<body>
<a class="project-brand-link" href="index.html" aria-label="MiniAmazon home"><img class="project-brand-logo" src="images/miniamazon-logo.webp" alt="MiniAmazon — by Polo Studios" width="210" height="110"></a>


<!-- Título -->
<h2>Remove Product</h2>

<!-- Mensaje de confirmación -->
<p>Are you sure you want to delete the product:</p>

<!-- Nombre e ID del producto -->
<b><%= product.getName() %></b> (ID: <%= product.getId() %>)

<!-- Formulario para confirmar eliminación -->
<form action="RemoveProductServlet" method="post">
    <input type="hidden" name="id" value="<%= product.getId() %>">
    <button type="submit">Yes, Delete</button>
</form>

</body>
</html>