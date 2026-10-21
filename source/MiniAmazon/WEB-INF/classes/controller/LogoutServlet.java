/**
 * CPEN 410 - Project 1
 * Clase: LogoutServlet
 * Servlet encargado de manejar el cierre de sesión dentro del sistema MiniAmazon.
 * Invalida la sesión activa del usuario y redirige a la página principal.
 * Este servlet utiliza {@link jakarta.servlet.http.HttpSession} para gestionar
 * la sesión del usuario.
 */

package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    /**
     * Método: Procesa la solicitud GET para cerrar sesión.
     * Si existe una sesión activa, la invalida y luego redirige al usuario
     * a la página principal del sistema.
     * @param request  solicitud HTTP recibida.
     * @param response respuesta HTTP para redirección.
     * @throws ServletException si ocurre un error interno del servlet.
     * @throws IOException si ocurre un error al redirigir.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener sesión existente sin crear una nueva
        HttpSession session = request.getSession(false);

        // Invalidar sesión si existe
        if (session != null) {
            session.invalidate();
        }

        // Redirigir a la página principal
        response.sendRedirect(request.getContextPath() + "/");
    }
}