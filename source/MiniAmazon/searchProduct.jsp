<%@ page import="java.sql.*" %>              <!-- Librerías SQL -->
<%@ page import="ut.MySQLConnector" %>       <!-- Conector MySQL -->

<!DOCTYPE html>
<html>
<head>
    <title>Mini Amazon - Search Products</title>
<link rel="stylesheet" href="project-brand.css?v=1">
</head>
<body style="background-color:#FFFACD; font-family:Arial;">
<a class="project-brand-link" href="index.html" aria-label="MiniAmazon home"><img class="project-brand-logo" src="images/miniamazon-logo.webp" alt="MiniAmazon — by Polo Studios" width="210" height="110"></a>


    <!-- Título -->
    <h1 style="color:#232f3e;">Search Results</h1>

    <!-- Navegación -->
    <a href="index.html">Home</a> |
    <a href="viewProducts.jsp">View Products</a> |
    <a href="cart.jsp">Cart</a>

    <hr>

<%
    /* Obtengo parámetros */
    String productName = request.getParameter("productName");
    String categoryId = request.getParameter("categoryId");

    if (productName == null) productName = "";
    if (categoryId == null) categoryId = "";

    productName = productName.trim();
    categoryId = categoryId.trim();
%>

    <!-- Formulario de búsqueda -->
    <form action="searchProduct.jsp" method="post">
        <input type="text" name="productName" value="<%= productName %>" placeholder="Search product by name" />

        <!-- Categorías -->
        <select name="categoryId">
            <option value="">All Categories</option>

<%
    MySQLConnector catDb = new MySQLConnector();
    ResultSet catRs = null;

    try {
        catDb.doConnection();
        catRs = catDb.doSelect("id_category, category_name", "categories");

        while (catRs.next()) {
            String currentId = String.valueOf(catRs.getInt("id_category"));
%>
            <option value="<%= currentId %>" <%= currentId.equals(categoryId) ? "selected" : "" %>>
                <%= catRs.getString("category_name") %>
            </option>
<%
        }
    } catch (Exception e) {
%>
            <option value="">Error loading categories</option>
<%
    } finally {
        try { if (catRs != null) catRs.close(); } catch (Exception e) {}
        try { catDb.closeConnection(); } catch (Exception e) {}
    }
%>
        </select>

        <input type="submit" value="Search" />
    </form>

    <hr>

    <!-- Resumen de búsqueda -->
    <p>
        <b>Search:</b> <%= productName.equals("") ? "All Products" : productName %>
        |
        <b>Category:</b> <%= categoryId.equals("") ? "All Categories" : categoryId %>
    </p>

    <!-- Tabla de resultados -->
    <table border="1" cellpadding="8" cellspacing="0">
        <tr style="background-color:#dddddd;">
            <th>ID</th>
            <th>Name</th>
            <th>Price</th>
            <th>Action</th>
        </tr>

<%
    MySQLConnector db = new MySQLConnector();
    ResultSet rs = null;

    try {
        db.doConnection();

        String safeName = productName.replace("'", "''");

        String where = "1=1";

        /* Filtro por nombre */
        if (!safeName.equals("")) {
            where += " AND product_name LIKE '%" + safeName + "%'";
        }

        /* Filtro por categoría */
        if (!categoryId.equals("")) {
            where += " AND id_category = " + categoryId;
        }

        rs = db.doSelect("id_product, product_name, price", "products", where);

        boolean found = false;

        /* Recorro productos */
        while (rs.next()) {
            found = true;
%>
        <tr>
            <td><%= rs.getInt("id_product") %></td>
            <td><%= rs.getString("product_name") %></td>
            <td>$<%= rs.getDouble("price") %></td>
            <td>
                <a href="cart.jsp?action=add&id=<%= rs.getInt("id_product") %>">Add to Cart</a>
            </td>
        </tr>
<%
        }

        /* Si no hay resultados */
        if (!found) {
%>
        <tr>
            <td colspan="4">No products found.</td>
        </tr>
<%
        }

    } catch (Exception e) {
%>
        <tr>
            <td colspan="4">Error: <%= e.getMessage() %></td>
        </tr>
<%
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { db.closeConnection(); } catch (Exception e) {}
    }
%>
    </table>

</body>
</html>