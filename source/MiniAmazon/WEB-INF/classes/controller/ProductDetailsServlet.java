/**
 * CPEN 410 - Project 1
 * Clase: ProductDetailsServlet
 * Servlet encargado de mostrar los detalles de un producto específico dentro
 * del sistema MiniAmazon. Recibe el ID del producto mediante parámetro,
 * valida su existencia y envía la información a la vista correspondiente.
 * Este servlet utiliza:
 *@link dao.ProductDAO} para obtener información del producto.
 *{@link model.ProductClass} como modelo de datos.
 */

package controller;

import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.ProductClass;

import java.io.IOException;

@WebServlet("/product-details")
public class ProductDetailsServlet extends HttpServlet {

    /** DAO para operaciones relacionadas con productos */
    private final ProductDAO productDAO = new ProductDAO();

    /**
     * Método: Procesa la solicitud GET para mostrar los detalles de un producto.
     * Valida el parámetro id, obtiene el producto desde la base de datos y
     * reenvía la información a productDetails.jsp.
     * @param request  solicitud HTTP con el parámetro id del producto.
     * @param response respuesta HTTP para redirección o despliegue de vista.
     * @throws ServletException si ocurre un error interno del servlet.
     * @throws IOException si ocurre un error al redirigir o reenviar.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        // Validar parámetro id
        if (idParam == null || idParam.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        try {
            int productId = Integer.parseInt(idParam);

            // Obtener producto desde la base de datos
            ProductClass product = productDAO.getProductById(productId);

            if (product == null) {
                response.sendRedirect(request.getContextPath() + "/products");
                return;
            }

            // Enviar producto a la vista
            request.setAttribute("product", product);
            request.getRequestDispatcher("/productDetails.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            // Si el id no es numérico
            response.sendRedirect(request.getContextPath() + "/products");

        } catch (Exception e) {
            throw new ServletException("Error loading product details.", e);
        }
    }
}