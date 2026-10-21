/**
 * CPEN 410 - Project 1
 * Clase: AddToCartServlet
 * Servlet encargado de manejar la acción de agregar productos al carrito
 * dentro del sistema MiniAmazon. Valida la sesión del usuario, obtiene los
 * parámetros enviados desde el formulario y delega la operación al CartDAO.
 * Este servlet utiliza {@link dao.ProductDAO} para obtener información del
 * producto y {@link dao.CartDAO} para registrar la acción dentro del carrito.
 */

package controller;

import dao.CartDAO;
import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.ProductClass;
import model.UserClass;

import java.io.IOException;

@WebServlet("/add-to-cart")
public class AddToCartServlet extends HttpServlet {

    /** DAO para obtener información de productos */
    private final ProductDAO productDAO = new ProductDAO();

    /** DAO para manejar operaciones del carrito */
    private final CartDAO cartDAO = new CartDAO();

    /**
     * Método: Procesa la solicitud POST para agregar un producto al carrito.
     * Verifica que el usuario esté autenticado, valida los parámetros recibidos,
     * ajusta la cantidad según el stock disponible y delega la operación al CartDAO.
     *
     * @param request  solicitud HTTP con los datos enviados desde el formulario.
     * @param response respuesta HTTP para redirección o manejo de errores.
     * @throws ServletException si ocurre un error interno del servlet.
     * @throws IOException si ocurre un error al redirigir o procesar la solicitud.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Validar sesión del usuario
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("loggedUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        UserClass user = (UserClass) session.getAttribute("loggedUser");

        try {
            // Obtener parámetros del formulario
            int productId = Integer.parseInt(request.getParameter("id"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            // Obtener información del producto
            ProductClass product = productDAO.getProductById(productId);

            // Validaciones básicas
            if (product == null || quantity <= 0 || product.getStock() == 0) {
                response.sendRedirect(request.getContextPath() + "/products");
                return;
            }

            // Ajustar cantidad si excede el stock
            if (quantity > product.getStock()) {
                quantity = product.getStock();
            }

            // Agregar al carrito
            cartDAO.addToCart(user.getId_user(), productId, quantity);

            // Redirigir al carrito
            response.sendRedirect(request.getContextPath() + "/view-cart");

        } catch (Exception e) {
            throw new ServletException("Error adding to cart.", e);
        }
    }
}