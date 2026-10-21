<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Add Product</title>
</head>
<body>
<!-- Formulario para agregar un producto -->
<h2>Add New Product</h2>
<form action="AddProductServlet" method="post" enctype="multipart/form-data">
    <!-- Nombre del producto -->
	Name: <input type="text" name="name" required><br><br>
    <!-- Descripción -->
	Description: <input type="text" name="description" required><br><br>
    <!-- Precio -->
    Price: <input type="number" step="0.01" name="price" required><br><br>
    <!-- Stock -->
	Stock: <input type="number" name="stock" required><br><br>
	<!-- Imagen -->
    Image: <input type="file" name="image" accept="image/*"><br><br>

    <button type="submit">Add Product</button><!-- Botón para enviar -->
</form>

</body>
</html>

