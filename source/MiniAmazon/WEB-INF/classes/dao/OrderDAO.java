/**
 * CPEN 410 - Project 1
 * Clase: OrderDAO
 * Clase encargada de manejar la creación de órdenes dentro del sistema MiniAmazon.
 * Genera un registro en la tabla orders y sus detalles correspondientes en la tabla
 * order_detail, utilizando los productos contenidos en el carrito del usuario.
 * Esta clase utiliza {@link ut.MySQLConnector} para ejecutar consultas SQL y
 * se apoya en la clase {@link model.CartItemClass} para obtener la información
 * de los productos y cantidades seleccionadas por el usuario.
 */

package dao;

import model.CartItemClass;
import ut.MySQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class OrderDAO {

    /**
     * Método: Crea una nueva orden para el usuario especificado.
     * Calcula el total de la orden basado en los productos del carrito,
     * inserta el registro principal en la tabla orders y luego inserta
     * cada producto en la tabla order_detail.
     * @param userId id del usuario que realiza la orden.
     * @param items lista de productos del carrito con sus cantidades.
     * @throws Exception si ocurre un error durante la creación de la orden.
     */
    public void createOrder(int userId, ArrayList<CartItemClass> items) throws Exception {
        MySQLConnector db = new MySQLConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            db.doConnection();
            Connection conn = db.getConnection();

            // Calcular total de la orden
            double total = 0.0;
            for (CartItemClass item : items) {
                total += item.getProduct().getPrice() * item.getQuantity();
            }

            // Insertar orden principal
            String orderSql = "INSERT INTO orders (id_user, total, status) VALUES (?, ?, ?)";
            ps = conn.prepareStatement(orderSql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, userId);
            ps.setDouble(2, total);
            ps.setString(3, "Completed");
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();

            int orderId = 0;
            if (rs.next()) {
                orderId = rs.getInt(1);
            } else {
                throw new Exception("Could not create order.");
            }

            rs.close();
            ps.close();

            // Insertar detalles de la orden
            String detailSql = "INSERT INTO order_detail (id_order, id_product, quantity, unit_price) VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(detailSql);

            for (CartItemClass item : items) {
                ps.setInt(1, orderId);
                ps.setInt(2, item.getProduct().getProductId());
                ps.setInt(3, item.getQuantity());
                ps.setDouble(4, item.getProduct().getPrice());
                ps.executeUpdate();
            }

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            db.closeConnection();
        }
    }
}