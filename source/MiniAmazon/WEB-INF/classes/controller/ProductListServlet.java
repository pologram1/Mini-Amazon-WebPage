/**
 * CPEN 410 - Project 1
 * Clase: ProductListServlet
 * Servlet encargado de mostrar la lista de productos dentro del sistema
 * MiniAmazon. Permite filtrar productos por palabra clave y/o categoría,
 * delegando la búsqueda al ProductDAO.
 * Este servlet utiliza:
 * @link dao.ProductDAO} para obtener y filtrar productos.
 * @link model.ProductClass} como modelo de datos.
 */

package controller;

import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.ProductClass;

import java.io.IOException;
import java.util.List;

@WebServlet("/products")
public class ProductListServlet extends HttpServlet {

    /** DAO para operaciones relacionadas con productos */
    private final ProductDAO productDAO = new ProductDAO();

    /**
     * Método: Procesa la solicitud GET para mostrar la lista de productos.
     * Si se reciben parámetros de búsqueda (q o categoryId), se filtran los
     * productos; de lo contrario, se cargan todos los productos disponibles.
     * @param request  solicitud HTTP con parámetros opcionales de búsqueda.
     * @param response respuesta HTTP para redirección o despliegue de vista.
     * @throws ServletException si ocurre un error interno del servlet.
     * @throws IOException si ocurre un error al reenviar o redirigir.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("q");
        String categoryId = request.getParameter("categoryId");

        try {
            List<ProductClass> products;

            boolean hasKeyword = keyword != null && !keyword.isBlank();
            boolean hasCategory = categoryId != null && !categoryId.isBlank();

            // Determinar si se debe buscar o cargar todos los productos
            if (hasKeyword || hasCategory) {
                Integer category = hasCategory ? Integer.parseInt(categoryId) : null;
                products = productDAO.searchProducts(keyword, category);
            } else {
                products = productDAO.getAllProducts();
            }

            // Enviar lista de productos a la vista
            request.setAttribute("products", products);
            request.getRequestDispatcher("/viewProducts.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error loading products.", e);
        }
    }
}