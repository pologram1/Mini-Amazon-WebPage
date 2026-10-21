/**
 * CPEN 410 - Project 1
 * Clase: CheckoutServlet
 * Servlet encargado de manejar el proceso de checkout dentro del sistema
 * MiniAmazon. Valida la sesión del usuario, verifica disponibilidad de stock,
 * actualiza inventarios, genera la orden y finalmente limpia el carrito.
 * Este servlet va a utilizar: 
 * - {@link dao.CartDAO} para obtener y limpiar el carrito.
 * - {@link dao.OrderDAO} para registrar la orden en la base de datos.
 * - {@link dao.ProductDAO} para validar y actualizar el inventario.
 */

package controller;

import dao.CartDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.CartClass;
import model.CartItemClass;
import model.ProductClass;
import model.UserClass;

import java.io.IOException;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    /** DAO para operaciones del carrito */
    private final CartDAO cartDAO = new CartDAO();

    /** DAO para creación de órdenes */
    private final OrderDAO orderDAO = new OrderDAO();

    /** DAO para validación y actualización de productos */
    private final ProductDAO productDAO = new ProductDAO();

    /**
     * Método: Procesa la solicitud POST para realizar el checkout.
     * Verifica la sesión del usuario, valida el carrito, revisa el stock,
     * actualiza inventarios, crea la orden y limpia el carrito.
     * @param request  solicitud HTTP enviada por el usuario.
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
            // Obtener carrito del usuario
            CartClass cart = cartDAO.getCartByUser(user.getId_user());

            if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/view-cart");
                return;
            }

            // Validar stock de cada producto
            for (CartItemClass item : cart.getItems()) {
                ProductClass current = productDAO.getProductById(item.getProduct().getProductId());
                if (current == null || current.getStock() < item.getQuantity()) {
                    response.sendRedirect(request.getContextPath() + "/view-cart");
                    return;
                }
            }

            // Actualizar inventario
            for (CartItemClass item : cart.getItems()) {
                boolean ok = productDAO.updateStock(item.getProduct().getProductId(), item.getQuantity());
                if (!ok) {
                    response.sendRedirect(request.getContextPath() + "/view-cart");
                    return;
                }
            }

            // Crear orden y limpiar carrito
            orderDAO.createOrder(user.getId_user(), cart.getItems());
            cartDAO.clearCart(user.getId_user());

            // Redirigir a historial de órdenes
            response.sendRedirect(request.getContextPath() + "/orders.jsp");

        } catch (Exception e) {
            throw new ServletException("Error during checkout.", e);
        }
    }
}