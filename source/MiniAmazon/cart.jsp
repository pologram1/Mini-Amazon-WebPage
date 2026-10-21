<%@ page import="model.CartClass" %>        <!-- Clase del carrito -->
<%@ page import="model.CartItemClass" %>    <!-- Items del carrito -->
<%@ page import="model.UserClass" %>        <!-- Usuario -->
<%@ page import="dao.CartDAO" %>            <!-- DAO del carrito -->
<%@ page import="java.util.ArrayList" %>    <!-- Lista -->

<!DOCTYPE html>
<html>
<head>
    <title>Shopping Cart</title>
</head>
<body style="background-color:#FFFACD; font-family:Arial;">

<h2>Your Cart</h2>

<!-- Enlaces de navegación -->
<a href="<%= request.getContextPath() %>/products">Back to Products</a> |
<a href="<%= request.getContextPath() %>/logout">Logout</a>

<hr>

<%
    /* Obtengo la sesión */
    HttpSession sessionObj = request.getSession(false);

    /* Verifico si el usuario está logueado */
    if (sessionObj == null || sessionObj.getAttribute("loggedUser") == null) {
%>
        <p>Please login first.</p>
        <a href="<%= request.getContextPath() %>/login">Login</a>
<%
    } else {

        /* Usuario logueado */
        UserClass user = (UserClass) sessionObj.getAttribute("loggedUser");

        /* DAO del carrito */
        CartDAO cartDAO = new CartDAO();
        CartClass cart = null;

        /* Cargo el carrito desde la BD */
        try {
            cart = cartDAO.getCartByUser(user.getId_user());
        } catch (Exception e) {
            out.println("Error loading cart");
        }

        /* Verifico si el carrito está vacío */
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
%>
            <p>Cart is empty.</p>
            <a href="<%= request.getContextPath() %>/products">Continue Shopping</a>
<%
        } else {

            /* Obtengo los items */
            ArrayList<CartItemClass> items = cart.getItems();
            double total = 0;
%>

<!-- Tabla del carrito -->
<table border="1" cellpadding="8" cellspacing="0">
    <tr style="background-color:#dddddd;">
        <th>Name</th>
        <th>Description</th>
        <th>Price</th>
        <th>Stock</th>
        <th>Quantity</th>
        <th>Subtotal</th>
        <th>Update</th>
        <th>Remove</th>
    </tr>

<%
            /* Recorro los items del carrito */
            for (CartItemClass item : items) {

                /* Calculo subtotal */
                double subtotal = item.getProduct().getPrice() * item.getQuantity();
                total += subtotal;
%>

    <tr>
        <td><%= item.getProduct().getName() %></td>
        <td><%= item.getProduct().getDescription() %></td>
        <td>$<%= item.getProduct().getPrice() %></td>
        <td><%= item.getProduct().getStock() %></td>

        <!-- Form para actualizar cantidad -->
        <td>
            <form action="<%= request.getContextPath() %>/update-cart" method="post">
                <input type="hidden" name="productId" value="<%= item.getProduct().getProductId() %>">
                <input type="number"
                       name="quantity"
                       value="<%= item.getQuantity() %>"
                       min="1"
                       max="<%= item.getProduct().getStock() %>"
                       style="width:60px;">
        </td>

        <td>$<%= subtotal %></td>

        <td>
                <input type="submit" value="Update"
                       style="background-color:#1E90FF; color:white;">
            </form>
        </td>

        <!-- Form para eliminar item -->
        <td>
            <form action="<%= request.getContextPath() %>/remove-from-cart" method="post">
                <input type="hidden" name="productId" value="<%= item.getProduct().getProductId() %>">
                <input type="submit" value="Remove"
                       style="background-color:red; color:white;">
            </form>
        </td>
    </tr>

<%
            } /* Fin del for */
%>

    <!-- Total del carrito -->
    <tr>
        <td colspan="5" align="right"><strong>Total:</strong></td>
        <td colspan="3"><strong>$<%= total %></strong></td>
    </tr>
</table>

<br>

<!-- Botón de checkout -->
<form action="<%= request.getContextPath() %>/checkout" method="post">
    <input type="submit" value="Checkout"
           style="background-color:green; color:white; padding:10px; font-size:16px;">
</form>

<br>
<a href="<%= request.getContextPath() %>/products">Continue Shopping</a>

<%
        } /* Fin carrito con items */
    } /* Fin usuario logueado */
%>

</body>
</html>