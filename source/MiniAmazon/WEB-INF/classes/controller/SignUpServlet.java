/**
 * CPEN 410 - Project 1
 * Clase: SignUpServlet
 * Servlet encargado de manejar el proceso de registro de nuevos usuarios
 * dentro del sistema MiniAmazon. Valida los datos enviados desde el formulario,
 * verifica que el nombre de usuario no exista, genera el hash de la contraseña
 * y delega la inserción del nuevo usuario al UserDAO.
 * Este servlet va a utilizar:
 *{@link dao.UserDAO} para operaciones relacionadas con usuarios.
 *{@link model.UserClass} como modelo de datos.
 *{@link ut.PasswordUtil} para hashing seguro de contraseñas.
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

@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {

    /** DAO para operaciones relacionadas con usuarios */
    private final UserDAO userDAO = new UserDAO();

    /**
     * Método: Procesa la solicitud POST enviada desde el formulario de registro.
     * Valida los campos obligatorios, verifica si el nombre de usuario ya existe,
     * genera el hash de la contraseña y crea un nuevo usuario en la base de datos.
     * @param request  solicitud HTTP con los datos del formulario.
     * @param response respuesta HTTP para redirección o mensajes de error.
     * @throws ServletException si ocurre un error interno del servlet.
     * @throws IOException si ocurre un error al escribir la respuesta o redirigir.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("userName");
        String password = request.getParameter("userPass");
        String name = request.getParameter("completeName");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String telephone = request.getParameter("telephone");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Validación de campos obligatorios
            if (username == null || username.isBlank() ||
                password == null || password.isBlank()) {

                out.println("<html><body>");
                out.println("<h2>Username and password are required.</h2>");
                out.println("<a href='" + request.getContextPath() + "/signUp.html'>Back</a>");
                out.println("</body></html>");
                return;
            }

            // Verificar si el usuario ya existe
            if (userDAO.existsByUsername(username)) {
                out.println("<html><body>");
                out.println("<h2>Username already exists.</h2>");
                out.println("<a href='" + request.getContextPath() + "/signUp.html'>Back</a>");
                out.println("</body></html>");
                return;
            }

            // Generar hash de la contraseña
            String passwordHash = PasswordUtil.hashPassword(password);

            // Crear objeto UserClass
            UserClass user = new UserClass();
            user.setUsername(username);
            user.setPasswordhash(passwordHash);
            user.setName(name);
            user.setEmail(email);
            user.setAddress(address);
            user.setTelephone(telephone);
            user.setRole("user");

            // Insertar usuario en la base de datos
            boolean inserted = userDAO.insertUser(user);

            if (inserted) {
                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                out.println("<html><body>");
                out.println("<h2>Could not create account.</h2>");
                out.println("<a href='" + request.getContextPath() + "/signUp.html'>Back</a>");
                out.println("</body></html>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<html><body>");
            out.println("<h2>Error creating user.</h2>");
            out.println("<p>" + e.toString() + "</p>");
            out.println("<a href='" + request.getContextPath() + "/signUp.html'>Back</a>");
            out.println("</body></html>");
        }
    }

    /**
     * Método: Redirige al formulario de registro cuando se accede mediante GET.
     * @param request  solicitud HTTP.
     * @param response respuesta HTTP para redirección.
     * @throws ServletException si ocurre un error interno.
     * @throws IOException si ocurre un error al redirigir.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/signUp.html");
    }
}