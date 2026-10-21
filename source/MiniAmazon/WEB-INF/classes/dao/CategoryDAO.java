/**
 * CPEN 410 - Project 1
 * Clase: CategoryDAO
 * Clase encargada de manejar las operaciones relacionadas con las categorías
 * de productos dentro del sistema MiniAmazon. Permite obtener todas las
 * categorías almacenadas en la base de datos en un mapa ordenado.
 * Esta clase utiliza {@link ut.MySQLConnector} para ejecutar consultas SQL
 * y devuelve los resultados en una estructura Map para fácil acceso desde
 * la capa de presentación o lógica de negocio.
 */

package dao;

import ut.MySQLConnector;

import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class CategoryDAO {

    /**
     * Método: Obtiene todas las categorías registradas en la base de datos.
     * Devuelve un mapa ordenado donde la llave es el id de la categoría
     * y el valor es el nombre de la categoría.
     * @return mapa de categorías (id_category → category_name).
     */
    public Map<Integer, String> getAllCategories() {
        Map<Integer, String> categories = new LinkedHashMap<>();
        MySQLConnector db = new MySQLConnector();

        try {
            db.doConnection();

            ResultSet rs = db.doSelect("*", "categories");

            while (rs.next()) {
                categories.put(
                        rs.getInt("id_category"),
                        rs.getString("category_name")
                );
            }

            rs.close();
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return categories;
    }
}