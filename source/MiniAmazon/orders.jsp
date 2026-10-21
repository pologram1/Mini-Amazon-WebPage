<%@ page import="java.sql.*" %>              <!-- Librerías SQL -->
<%@ page import="ut.MySQLConnector" %>       <!-- Conector MySQL -->
<%@ page import="model.UserClass" %>         <!-- Clase del usuario -->

<!DOCTYPE html>
<html>
<head>
    <title>My Orders</title>
</head>
<body style="background-color:#FFFACD; font-family:Arial;">

<%
    /* Verifico sesión */
    HttpSession s = request.getSession(false);
    if (s == null || s.getAttribute("loggedUser") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    /* Usuario logueado */
    UserClass user = (UserClass) s.getAttribute("loggedUser");
%>

<!-- Título -->
<h1>My Orders</h1>

<!-- Navegación -->
<a href="<%= request.getContextPath() %>/products">Products</a> |
<a href="<%= request.getContextPath() %>/view-cart">Cart</a> |
<a href="<%= request.getContextPath() %>/logout">Logout</a>

<hr>

<%
    /* Conexión a BD */
    MySQLConnector db = new MySQLConnector();
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        db.doConnection();

        /* Consulta de órdenes */
        String sql =
            "SELECT o.id_order, o.order_date, o.total, o.status, " +
            "p.product_name, od.quantity, od.unit_price " +
            "FROM orders o " +
            "JOIN order_detail od ON o.id_order = od.id_order " +
            "JOIN products p ON od.id_product = p.id_product " +
            "WHERE o.id_user = ? " +
            "ORDER BY o.id_order DESC, p.product_name ASC";

        ps = db.getConnection().prepareStatement(sql);
        ps.setInt(1, user.getId_user());
        rs = ps.executeQuery();

        int currentOrderId = -1;
        boolean hasOrders = false;

        /* Recorro las órdenes */
        while (rs.next()) {
            hasOrders = true;

            int orderId = rs.getInt("id_order");

            /* Cambio de orden */
            if (orderId != currentOrderId) {
                if (currentOrderId != -1) {
%>
                    </ul>
                    <hr>
<%
                }

                currentOrderId = orderId;
%>
                <!-- Encabezado de la orden -->
                <h3>Order #<%= rs.getInt("id_order") %></h3>
                <p><b>Date:</b> <%= rs.getTimestamp("order_date") %></p>
                <p><b>Status:</b> <%= rs.getString("status") %></p>
                <p><b>Total:</b> $<%= rs.getDouble("total") %></p>
                <p><b>Products:</b></p>
                <ul>
<%
            }
%>
            <!-- Productos dentro de la orden -->
            <li>
                <%= rs.getString("product_name") %>
                | Qty: <%= rs.getInt("quantity") %>
                | Unit Price: $<%= rs.getDouble("unit_price") %>
            </li>
<%
        }

        /* Si no tiene órdenes */
        if (!hasOrders) {
%>
            <p>No orders yet.</p>
<%
        } else {
%>
            </ul>
<%
        }

    } catch (Exception e) {
        out.println("<p>Error loading orders.</p>");
        e.printStackTrace();
    } finally {
        /* Cierro recursos */
        if (rs != null) rs.close();
        if (ps != null) ps.close();
        db.closeConnection();
    }
%>

</body>
</html>