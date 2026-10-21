/**
 * CPEN 410 - Project 1
 * Clase: UpdateCartServlet
 * Servlet encargado de actualizar la cantidad de un producto dentro del
 * carrito de compras del usuario en el sistema MiniAmazon. Valida la sesión,
 * obtiene los parámetros necesarios y delega la actualización al CartDAO.
 *
 * Este servlet utiliza:
 *{@link dao.CartDAO} para modificar el carrito del usuario.
 *{@link model.UserClass} para identificar al usuario autenticado.
 */

package controller;

import dao.CartDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.UserClass;

import java.io.IOException;

@WebServlet("/update-cart")
public class UpdateCartServlet extends HttpServlet {

    /** DAO para operaciones relacionadas con el carrito */
    private final CartDAO cartDAO = new CartDAO();

    /**
     * Método: Procesa la solicitud POST para actualizar la cantidad de un
     * producto en el carrito. Verifica la sesión del usuario, obtiene los
     * parámetros productId y quantity, y delega la actualización al CartDAO.
     *
     * @param request  solicitud HTTP con los parámetros productId y quantity.
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
            // Obtener parámetros del formulario
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            // Actualizar cantidad en el carrito
            cartDAO.updateQuantity(user.getId_user(), productId, quantity);

            // Redirigir al carrito actualizado
            response.sendRedirect(request.getContextPath() + "/view-cart");

        } catch (Exception e) {
            throw new ServletException("Error updating cart.", e);
        }
    }
}