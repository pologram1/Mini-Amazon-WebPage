/**
 * CPEN 410 - Project 1
 * Clase: ViewCartServlet
 * Servlet encargado de mostrar el carrito de compras del usuario dentro
 * del sistema MiniAmazon. Valida la sesión del usuario, obtiene el carrito
 * asociado y lo envía a la vista correspondiente.
 * Este servlet vaa a utilizar:
 * {@link dao.CartDAO} para obtener la información del carrito.
 * {@link model.CartClass} como modelo del carrito.
 * {@link model.UserClass} para identificar al usuario autenticado.
 */

package controller;

import dao.CartDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.CartClass;
import model.UserClass;

import java.io.IOException;

@WebServlet("/view-cart")
public class ViewCartServlet extends HttpServlet {

    /** DAO para operaciones relacionadas con el carrito */
    private final CartDAO cartDAO = new CartDAO();

    /**
     * Método: Procesa la solicitud GET para visualizar el carrito del usuario.
     * Verifica la sesión, obtiene el carrito desde la base de datos y reenvía
     * la información a cart.jsp.
     * @param request  solicitud HTTP del usuario autenticado.
     * @param response respuesta HTTP para redirección o despliegue de vista.
     * @throws ServletException si ocurre un error interno del servlet.
     * @throws IOException si ocurre un error al reenviar o redirigir.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Validar sesión del usuario
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("loggedUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        UserClass user = (UserClass) session.getAttribute("loggedUser");

        try {
            // Obtener carrito del usuario
            CartClass cart = cartDAO.getCartByUser(user.getId_user());

            // Enviar carrito a la vista
            request.setAttribute("cart", cart);
            request.getRequestDispatcher("/cart.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException("Error loading cart.", e);
        }
    }
}