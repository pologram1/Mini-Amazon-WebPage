/**
 * CPEN 410 - Project 1
 * Clase: RemoveFromCartServlet
 * Servlet encargado de manejar la eliminación de productos del carrito
 * dentro del sistema MiniAmazon. Valida la sesión del usuario, obtiene
 * el ID del producto a eliminar y delega la operación al CartDAO.
 *
 * Este servlet utiliza:
 * - {@link dao.CartDAO} para modificar el carrito del usuario.
 * - {@link model.UserClass} para identificar al usuario autenticado.
 */

package controller;

import dao.CartDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.UserClass;

import java.io.IOException;

@WebServlet("/remove-from-cart")
public class RemoveFromCartServlet extends HttpServlet {

    /** DAO para operaciones relacionadas con el carrito */
    private final CartDAO cartDAO = new CartDAO();

    /**
     * Método: Procesa la solicitud POST para eliminar un producto del carrito.
     * Verifica la sesión del usuario, obtiene el ID del producto y delega la
     * eliminación al CartDAO.
     *
     * @param request  solicitud HTTP con el parámetro productId.
     * @param response respuesta HTTP para redirección o manejo de errores.
     * @throws ServletException si ocurre un error interno del servlet.
     * @throws IOException si ocurre un error al redirigir.
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
            // Obtener ID del producto a eliminar
            int productId = Integer.parseInt(request.getParameter("productId"));

            // Eliminar producto del carrito
            cartDAO.removeFromCart(user.getId_user(), productId);

            // Redirigir al carrito actualizado
            response.sendRedirect(request.getContextPath() + "/view-cart");

        } catch (Exception e) {
            throw new ServletException("Error removing from cart.", e);
        }
    }
}