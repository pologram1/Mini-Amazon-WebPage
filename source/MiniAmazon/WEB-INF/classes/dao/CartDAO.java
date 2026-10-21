/**
 * CPEN 410 - Project 1
 * Clase: CartDAO
 * Clase encargada de manejar todas las operaciones relacionadas con el carrito
 * de compras del usuario dentro del sistema MiniAmazon. Permite crear o recuperar
 * el carrito, agregar productos, actualizar cantidades, eliminar productos,
 * obtener el carrito completo y limpiarlo.
 * Esta clase utiliza la clase {@link ut.MySQLConnector} para ejecutar consultas SQL
 * y se comunica con las clases del paquete model para construir objetos de carrito.
 */

package dao;

import model.CartClass;
import model.CartItemClass;
import model.ProductClass;
import ut.MySQLConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CartDAO {

    /**
     * Método: Obtiene el id del carrito asociado al usuario.
     * Si no existe, crea un nuevo carrito y devuelve su id.
     * @param userId identificador del usuario.
     * @return id del carrito existente o recién creado.
     * @throws Exception si ocurre un error en la consulta o creación.
     */
    public int getOrCreateCartId(int userId) throws Exception {
        MySQLConnector db = new MySQLConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            db.doConnection();

            String findSql = "SELECT id_cart FROM cart WHERE id_user = ?";
            ps = db.getConnection().prepareStatement(findSql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_cart");
            }

            rs.close();
            ps.close();

            String insertSql = "INSERT INTO cart (id_user) VALUES (?)";
            ps = db.getConnection().prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, userId);
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

            throw new Exception("Could not create cart.");

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            db.closeConnection();
        }
    }

    /**
     * Método: Actualiza la cantidad de un producto dentro del carrito del usuario.
     * Si la cantidad es menor o igual a cero, elimina el producto del carrito.
     * Si la cantidad excede el stock disponible, ajusta la cantidad al máximo permitido.
     * @param userId id del usuario.
     * @param productId id del producto.
     * @param quantity nueva cantidad solicitada.
     * @throws Exception si ocurre un error durante la operación.
     */
    public void updateQuantity(int userId, int productId, int quantity) throws Exception {
        MySQLConnector db = new MySQLConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            int cartId = getOrCreateCartId(userId);
            db.doConnection();

            if (quantity <= 0) {
                String deleteSql = "DELETE FROM cart_detail WHERE id_cart = ? AND id_product = ?";
                ps = db.getConnection().prepareStatement(deleteSql);
                ps.setInt(1, cartId);
                ps.setInt(2, productId);
                ps.executeUpdate();
                return;
            }

            String stockSql = "SELECT stock FROM products WHERE id_product = ?";
            ps = db.getConnection().prepareStatement(stockSql);
            ps.setInt(1, productId);
            rs = ps.executeQuery();

            if (!rs.next()) {
                throw new Exception("Product not found.");
            }

            int stock = rs.getInt("stock");
            if (quantity > stock) {
                quantity = stock;
            }

            rs.close();
            ps.close();

            String updateSql = "UPDATE cart_detail SET quantity = ? WHERE id_cart = ? AND id_product = ?";
            ps = db.getConnection().prepareStatement(updateSql);
            ps.setInt(1, quantity);
            ps.setInt(2, cartId);
            ps.setInt(3, productId);
            ps.executeUpdate();

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            db.closeConnection();
        }
    }

    /**
     * Método: Elimina un producto específico del carrito del usuario.
     * @param userId id del usuario.
     * @param productId id del producto a eliminar.
     * @throws Exception si ocurre un error durante la eliminación.
     */
    public void removeFromCart(int userId, int productId) throws Exception {
        MySQLConnector db = new MySQLConnector();
        PreparedStatement ps = null;

        try {
            int cartId = getOrCreateCartId(userId);
            db.doConnection();

            String sql = "DELETE FROM cart_detail WHERE id_cart = ? AND id_product = ?";
            ps = db.getConnection().prepareStatement(sql);
            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            ps.executeUpdate();

        } finally {
            if (ps != null) ps.close();
            db.closeConnection();
        }
    }

    /**
     * Método: Agrega un producto al carrito del usuario.
     * Si el producto ya existe, aumenta la cantidad.
     * @param userId id del usuario.
     * @param productId id del producto.
     * @param quantity cantidad a agregar.
     * @throws Exception si ocurre un error durante la operación.
     */
    public void addToCart(int userId, int productId, int quantity) throws Exception {
        MySQLConnector db = new MySQLConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            int cartId = getOrCreateCartId(userId);
            db.doConnection();

            String checkSql = "SELECT quantity FROM cart_detail WHERE id_cart = ? AND id_product = ?";
            ps = db.getConnection().prepareStatement(checkSql);
            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            rs = ps.executeQuery();

            if (rs.next()) {
                int currentQty = rs.getInt("quantity");
                rs.close();
                ps.close();

                String updateSql = "UPDATE cart_detail SET quantity = ? WHERE id_cart = ? AND id_product = ?";
                ps = db.getConnection().prepareStatement(updateSql);
                ps.setInt(1, currentQty + quantity);
                ps.setInt(2, cartId);
                ps.setInt(3, productId);
                ps.executeUpdate();
            } else {
                rs.close();
                ps.close();

                String insertSql = "INSERT INTO cart_detail (id_cart, id_product, quantity) VALUES (?, ?, ?)";
                ps = db.getConnection().prepareStatement(insertSql);
                ps.setInt(1, cartId);
                ps.setInt(2, productId);
                ps.setInt(3, quantity);
                ps.executeUpdate();
            }

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            db.closeConnection();
        }
    }

    /**
     * Método: Obtiene el carrito completo del usuario,
     * incluyendo los productos y sus cantidades.
     * @param userId id del usuario.
     * @return objeto CartClass con los ítems del carrito.
     * @throws Exception si ocurre un error durante la consulta.
     */
    public CartClass getCartByUser(int userId) throws Exception {
        MySQLConnector db = new MySQLConnector();
        PreparedStatement ps = null;
        ResultSet rs = null;

        CartClass cart = new CartClass();

        try {
            db.doConnection();

            String sql =
                "SELECT cd.quantity, p.id_product, p.product_name, p.description, " +
                "p.price, p.stock, p.id_category, p.manufacturer, p.fabrication_year, p.image " +
                "FROM cart c " +
                "JOIN cart_detail cd ON c.id_cart = cd.id_cart " +
                "JOIN products p ON cd.id_product = p.id_product " +
                "WHERE c.id_user = ?";

            ps = db.getConnection().prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            while (rs.next()) {
                ProductClass product = new ProductClass();
                product.setProductId(rs.getInt("id_product"));
                product.setName(rs.getString("product_name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setStock(rs.getInt("stock"));
                product.setCategoryId(rs.getInt("id_category"));
                product.setManufacturer(rs.getString("manufacturer"));
                product.setYear(rs.getInt("fabrication_year"));
                product.setImage(rs.getString("image"));

                cart.getItems().add(new CartItemClass(product, rs.getInt("quantity")));
            }

            return cart;

        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            db.closeConnection();
        }
    }

    /**
     * Método: Elimina todos los productos del carrito del usuario.
     * @param userId id del usuario.
     * @throws Exception si ocurre un error durante la eliminación.
     */
    public void clearCart(int userId) throws Exception {
        MySQLConnector db = new MySQLConnector();
        PreparedStatement ps = null;

        try {
            db.doConnection();

            String sql =
                "DELETE cd FROM cart_detail cd " +
                "JOIN cart c ON cd.id_cart = c.id_cart " +
                "WHERE c.id_user = ?";

            ps = db.getConnection().prepareStatement(sql);
            ps.setInt(1, userId);
            ps.executeUpdate();

        } finally {
            if (ps != null) ps.close();
            db.closeConnection();
        }
    }
}