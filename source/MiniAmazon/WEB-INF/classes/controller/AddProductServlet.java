/**
 * CPEN 410 - Project 1
 * Clase: AddProductServlet
 * Servlet encargado de manejar la creación de nuevos productos dentro del sistema
 * MiniAmazon. Procesa datos enviados desde un formulario, incluyendo la carga de
 * imágenes mediante multipart/form-data, y delega la inserción del producto al
 * DAO correspondiente.
 * Este servlet utiliza {@link dao.ProductDAO} para registrar el producto en la
 * base de datos y emplea las capacidades de {@link jakarta.servlet.http.Part}
 * para gestionar archivos subidos por el usuario.
 */

package controller;

import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;

@WebServlet("/AddProductServlet")
@MultipartConfig
public class AddProductServlet extends HttpServlet {

    /**
     * Método: Procesa la solicitud POST para agregar un nuevo producto.
     * Obtiene los datos del formulario, maneja la carga de la imagen y
     * delega la inserción del producto al ProductDAO.
     * @param request  solicitud HTTP con los datos del formulario.
     * @param response respuesta HTTP para redirección o manejo de errores.
     * @throws ServletException si ocurre un error interno del servlet.
     * @throws IOException si ocurre un error al manejar archivos o redirecciones.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener datos del formulario
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        double price = Double.parseDouble(request.getParameter("price"));
        int stock = Integer.parseInt(request.getParameter("stock"));

        // Manejo de archivo de imagen
        Part filePart = request.getPart("image");
        String fileName = null;

        if (filePart != null && filePart.getSubmittedFileName() != null
                && !filePart.getSubmittedFileName().isBlank()) {

            fileName = filePart.getSubmittedFileName();

            // Ruta de almacenamiento dentro del servidor
            String uploadPath = getServletContext().getRealPath("") + File.separator + "images";
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            // Guardar archivo en el servidor
            filePart.write(uploadPath + File.separator + fileName);
        }

        // Insertar producto en la base de datos
        try {
            ProductDAO dao = new ProductDAO();
            dao.insertProduct(name, description, price, stock, fileName);
            response.sendRedirect(request.getContextPath() + "/listProducts.jsp");

        } catch (Exception e) {
            throw new ServletException("Error adding product.", e);
        }
    }
}