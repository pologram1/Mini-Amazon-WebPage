/**
 * CPEN 410 - Project 1
 * Clase: ProductDAO
 * Clase encargada de manejar todas las operaciones relacionadas con los productos
 * dentro del sistema MiniAmazon. Permite obtener productos, buscarlos por filtros,
 * insertarlos, actualizarlos, eliminarlos y modificar su inventario.
 * En esta clase utiliza {@link ut.MySQLConnector} para ejecutar consultas SQL
 * y construye objetos {@link model.ProductClass} para representar los productos
 * obtenidos desde la base de datos.
 */

package dao;

import ut.MySQLConnector;
import model.ProductClass;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    /**
     * Método: Obtiene todos los productos registrados en la base de datos.
     * @return lista de objetos ProductClass con la información de cada producto.
     */
    public List<ProductClass> getAllProducts() {
        List<ProductClass> products = new ArrayList<>();
        MySQLConnector db = new MySQLConnector();

        try {
            db.doConnection();

            ResultSet rs = db.doSelect("*", "products");

            while (rs.next()) {
                ProductClass product = new ProductClass();
                product.setProductId(rs.getInt("id_product"));
                product.setName(rs.getString("product_name"));
                product.setDescription(rs.getString("description"));
                product.setManufacturer(rs.getString("manufacturer"));
                product.setYear(rs.getInt("fabrication_year"));
                product.setStock(rs.getInt("stock"));
                product.setPrice(rs.getDouble("price"));
                product.setCategoryId(rs.getInt("id_category"));
                product.setImage(rs.getString("image"));
                products.add(product);
            }

            rs.close();
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    /**
     * Método: Obtiene un producto específico según su id.
     * @param id identificador del producto.
     * @return objeto ProductClass si existe, o null si no se encuentra.
     */
    public ProductClass getProductById(int id) {
        MySQLConnector db = new MySQLConnector();
        ProductClass product = null;

        try {
            db.doConnection();

            ResultSet rs = db.doSelect("*", "products", "id_product=" + id);

            if (rs.next()) {
                product = new ProductClass();
                product.setProductId(rs.getInt("id_product"));
                product.setName(rs.getString("product_name"));
                product.setDescription(rs.getString("description"));
                product.setManufacturer(rs.getString("manufacturer"));
                product.setYear(rs.getInt("fabrication_year"));
                product.setStock(rs.getInt("stock"));
                product.setPrice(rs.getDouble("price"));
                product.setCategoryId(rs.getInt("id_category"));
                product.setImage(rs.getString("image"));
            }

            rs.close();
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return product;
    }

    /**
     * Método: Busca productos según un texto clave y opcionalmente por categoría.
     * @param keyword texto a buscar dentro del nombre del producto.
     * @param categoryId id de categoría (puede ser null para no filtrar por categoría).
     * @return lista de productos que coinciden con los criterios.
     */
    public List<ProductClass> searchProducts(String keyword, Integer categoryId) {
        List<ProductClass> products = new ArrayList<>();
        MySQLConnector db = new MySQLConnector();

        try {
            db.doConnection();

            String where = "product_name LIKE '%" + keyword + "%'";
            if (categoryId != null) {
                where += " AND id_category=" + categoryId;
            }

            ResultSet rs = db.doSelect("*", "products", where);

            while (rs.next()) {
                ProductClass product = new ProductClass();
                product.setProductId(rs.getInt("id_product"));
                product.setName(rs.getString("product_name"));
                product.setDescription(rs.getString("description"));
                product.setManufacturer(rs.getString("manufacturer"));
                product.setYear(rs.getInt("fabrication_year"));
                product.setStock(rs.getInt("stock"));
                product.setPrice(rs.getDouble("price"));
                product.setCategoryId(rs.getInt("id_category"));
                product.setImage(rs.getString("image"));
                products.add(product);
            }

            rs.close();
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    /**
     * Método: Inserta un nuevo producto en la base de datos.
     * @param name nombre del producto.
     * @param description descripción del producto.
     * @param price precio del producto.
     * @param stock cantidad disponible.
     * @param image ruta o nombre de la imagen asociada.
     * @throws Exception si ocurre un error durante la inserción.
     */
    public void insertProduct(String name, String description, double price, int stock, String image) throws Exception {
        MySQLConnector db = new MySQLConnector();
        PreparedStatement ps = null;

        try {
            db.doConnection();

            String sql = "INSERT INTO products (product_name, description, price, stock, image) VALUES (?, ?, ?, ?, ?)";
            ps = db.getConnection().prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setDouble(3, price);
            ps.setInt(4, stock);
            ps.setString(5, image);

            ps.executeUpdate();

        } finally {
            if (ps != null) ps.close();
            db.closeConnection();
        }
    }

    /**
     * Método: Actualiza un producto existente.
     * @param productId id del producto.
     * @param name nuevo nombre.
     * @param description nueva descripción.
     * @param price nuevo precio.
     * @param stock nuevo inventario.
     * @param image nueva imagen.
     * @return true si se actualizó correctamente, false si no.
     */
    public boolean updateProduct(int productId, String name, String description, double price, int stock, String image) {
        MySQLConnector db = new MySQLConnector();
        PreparedStatement ps = null;
        boolean updated = false;

        try {
            db.doConnection();

            String sql = "UPDATE products SET product_name = ?, description = ?, price = ?, stock = ?, image = ? WHERE id_product = ?";
            ps = db.getConnection().prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, description);
            ps.setDouble(3, price);
            ps.setInt(4, stock);
            ps.setString(5, image);
            ps.setInt(6, productId);

            int rows = ps.executeUpdate();
            updated = rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                db.closeConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return updated;
    }

    /**
     * Método: Elimina un producto si no está asociado a órdenes previas.
     * También elimina referencias en el carrito antes de eliminar el producto.
     * @param productId id del producto a eliminar.
     * @return true si se eliminó correctamente, false si el producto está ligado a órdenes.
     */
    public boolean deleteProduct(int productId) {
        MySQLConnector db = new MySQLConnector();
        PreparedStatement psCart = null;
        PreparedStatement psOrderCheck = null;
        PreparedStatement psProduct = null;
        ResultSet rs = null;
        boolean deleted = false;

        try {
            db.doConnection();
            db.getConnection().setAutoCommit(false);

            String sqlOrderCheck = "SELECT COUNT(*) FROM order_detail WHERE id_product = ?";
            psOrderCheck = db.getConnection().prepareStatement(sqlOrderCheck);
            psOrderCheck.setInt(1, productId);
            rs = psOrderCheck.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                db.getConnection().rollback();
                return false;
            }

            String sqlCart = "DELETE FROM cart_detail WHERE id_product = ?";
            psCart = db.getConnection().prepareStatement(sqlCart);
            psCart.setInt(1, productId);
            psCart.executeUpdate();

            String sqlProduct = "DELETE FROM products WHERE id_product = ?";
            psProduct = db.getConnection().prepareStatement(sqlProduct);
            psProduct.setInt(1, productId);

            int rows = psProduct.executeUpdate();
            deleted = rows > 0;

            if (deleted) {
                db.getConnection().commit();
            } else {
                db.getConnection().rollback();
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (db.getConnection() != null) {
                    db.getConnection().rollback();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (psOrderCheck != null) psOrderCheck.close();
                if (psCart != null) psCart.close();
                if (psProduct != null) psProduct.close();
                if (db.getConnection() != null) {
                    db.getConnection().setAutoCommit(true);
                }
                db.closeConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return deleted;
    }

    /**
     * Método: Actualiza el inventario restando la cantidad comprada.
     * @param productId id del producto.
     * @param quantityPurchased cantidad comprada.
     * @return true si el stock se actualizó correctamente, false si no había suficiente inventario.
     */
    public boolean updateStock(int productId, int quantityPurchased) {
        MySQLConnector db = new MySQLConnector();
        boolean updated = false;

        try {
            db.doConnection();

            String sql = "UPDATE products " +
                         "SET stock = stock - " + quantityPurchased +
                         " WHERE id_product = " + productId +
                         " AND stock >= " + quantityPurchased;

            int rows = db.doUpdate(sql);
            updated = rows > 0;

            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return updated;
    }
}