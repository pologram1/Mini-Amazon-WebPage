/**
 * CPEN 410 - Project 1
 * Clase: LoginServlet
 * Servlet encargado de manejar el proceso de autenticación de usuarios dentro
 * del sistema MiniAmazon. Valida las credenciales enviadas desde el formulario
 * de inicio de sesión, verifica el usuario en la base de datos y crea la sesión
 * correspondiente si las credenciales son correctas.
 * Este servlet utiliza:
 * - {@link dao.UserDAO} para obtener información del usuario.
 * - {@link ut.PasswordUtil} para validar contraseñas mediante hashing seguro.
 */

package controller;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.UserClass;
import ut.PasswordUtil;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    /** DAO para operaciones relacionadas con usuarios */
    private final UserDAO userDAO = new UserDAO();

    /**
     * Método: Procesa la solicitud POST enviada desde el formulario de login.
     * Valida los parámetros, verifica las credenciales, crea la sesión del usuario
     * y redirige según su rol.
     * @param request  solicitud HTTP con las credenciales del usuario.
     * @param response respuesta HTTP para redirección o mensajes de error.
     * @throws ServletException si ocurre un error interno del servlet.
     * @throws IOException si ocurre un error al escribir la respuesta o redirigir.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("userName");
        String password = request.getParameter("userPass");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Validación básica de parámetros
        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            out.println("<html><body>");
            out.println("<h2>Invalid username or password.</h2>");
            out.println("<a href='" + request.getContextPath() + "/login.html'>Back to Login</a>");
            out.println("</body></html>");
            return;
        }

        try {
            // Buscar usuario en la base de datos
            UserClass user = userDAO.findByUsername(username);

            // Validar credenciales
            if (user != null && PasswordUtil.matches(password, user.getPasswordhash())) {

                // Crear sesión
                HttpSession session = request.getSession();
                session.setAttribute("loggedUser", user);
                session.setAttribute("username", user.getUsername());
                session.setAttribute("role", user.getRole());

                // Redirigir según rol
                if ("admin".equalsIgnoreCase(user.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/welcomeMenu.jsp");
                } else {
                    response.sendRedirect(request.getContextPath() + "/index.html");
                }

            } else {
                out.println("<html><body>");
                out.println("<h2>Invalid username or password.</h2>");
                out.println("<a href='" + request.getContextPath() + "/login.html'>Back to Login</a>");
                out.println("</body></html>");
            }

        } catch (Exception e) {
            throw new ServletException("Error during login.", e);
        }
    }

    /**
     * Método: Redirige al formulario de login cuando se accede mediante GET.
     * @param request  solicitud HTTP.
     * @param response respuesta HTTP para redirección.
     * @throws ServletException si ocurre un error interno.
     * @throws IOException si ocurre un error al redirigir.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/login.html");
    }
}