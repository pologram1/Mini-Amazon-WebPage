<%@ page import="model.ProductClass" %> <!-- Clase del producto -->

<%
    /* Obtengo el producto enviado desde el servlet */
    ProductClass product = (ProductClass) request.getAttribute("product");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Remove Product</title>
</head>
<body>

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