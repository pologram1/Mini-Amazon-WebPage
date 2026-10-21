/**
 * CPEN 410 - Project 1
 * Clase: RemoveProductServlet
 * Servlet encargado de manejar la eliminación de productos dentro del sistema
 * MiniAmazon. Solo los usuarios con rol administrador pueden ejecutar esta
 * operación. Valida la sesión, verifica el rol, obtiene el ID del producto
 * y delega la eliminación al ProductDAO.
 * Este servlet utiliza:
 * {@link dao.ProductDAO} para eliminar productos de la base de datos.
 */

package controller;

import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/RemoveProductServlet")
public class RemoveProductServlet extends HttpServlet {

    /**
     * Método: Procesa la solicitud GET para eliminar un producto.
     * Verifica que el usuario esté autenticado y tenga rol de administrador.
     * Si el ID del producto es válido, intenta eliminarlo mediante ProductDAO.
     * Si la eliminación falla, muestra un mensaje explicativo.
     * @param request  solicitud HTTP con el parámetro id del producto.
     * @param response respuesta HTTP para redirección o despliegue de mensaje.
     * @throws ServletException si ocurre un error interno del servlet.
     * @throws IOException si ocurre un error al redirigir o escribir la respuesta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Validar sesión del usuario
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getContextPath() + "/login.html");
            return;
        }

        // Validar rol de administrador
        String role = (String) session.getAttribute("role");
        if (role == null || !role.equalsIgnoreCase("admin")) {
            response.sendRedirect(request.getContextPath() + "/index.html");
            return;
        }

        // Validar parámetro id
        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/listProducts.jsp");
            return;
        }

        try {
            int productId = Integer.parseInt(idParam);

            ProductDAO dao = new ProductDAO();
            boolean deleted = dao.deleteProduct(productId);

            if (deleted) {
                response.sendRedirect(request.getContextPath() + "/listProducts.jsp");
            } else {
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().println("<html><body>");
                response.getWriter().println("<h2>Product could not be deleted.</h2>");
                response.getWriter().println("<p>The product may be linked to past orders or may not exist.</p>");
                response.getWriter().println("<a href='" + request.getContextPath() + "/listProducts.jsp'>Back to Product List</a>");
                response.getWriter().println("</body></html>");
            }

        } catch (NumberFormatException e) {
            // ID no numérico
            response.sendRedirect(request.getContextPath() + "/listProducts.jsp");

        } catch (Exception e) {
            throw new ServletException("Error deleting product.", e);
        }
    }
}